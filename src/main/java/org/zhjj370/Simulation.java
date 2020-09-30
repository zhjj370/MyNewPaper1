package org.zhjj370;

import org.zhjj370.basic.AdjustmentCoefficients;
import org.zhjj370.basic.Const;
import org.zhjj370.basic.Machine;
import org.zhjj370.basic.WipPart;
import org.zhjj370.container.PartManager;
import org.zhjj370.container.PendingTask;
import org.zhjj370.container.ShopFloor;
import org.zhjj370.container.TaskContainer;
import org.zhjj370.container.element.DataForOverdue;
import org.zhjj370.functions.DataPlotting;
import org.zhjj370.functions.DemoOpenFile;
import org.zhjj370.functions.gantt.DataProcess;
import org.zhjj370.functions.gantt.GanttPlotSvg;
import org.zhjj370.rule.CompareWaitingPart1;
import org.zhjj370.rule.elemp.ElemWaitingPart;
import org.zhjj370.connect.sqlite.Dao2020;

import java.io.IOException;
import java.util.*;

/**
 * 实时决策仿真的主程序
 */
public class Simulation {
    public static void main(String[] args) throws IOException {
        //初始化各容器 Initialize each container
        ShopFloor shopFloor = ShopFloor.getShopFloor();
        PartManager partManager = PartManager.getPartManager();
        TaskContainer taskContainer = TaskContainer.getTaskContainer();
        PendingTask pendingTask = PendingTask.getPendingTask();
        //初始化系数 Initialization coefficient
        AdjustmentCoefficients adjustmentCoefficients = AdjustmentCoefficients.getAdjustmentCoefficients();
        //初始化数据库 Initialize the database
        Dao2020 dao = Dao2020.getDao2020();
        dao.createTable();
        //删除上一次表格中数据 Delete the data in the last table
        dao.deleteTableForRecodring();
        //用于数据库，keyID || For database, keyID
        int keyID = 0;
        //记录时间 Record time
        int countT = 0;
        int lastOrderTime = 0;
        boolean isAllFinished = true;
        //记录总能量消耗 Record total energy consumption
        double allEnergy = 0.0;
        //记录实时评价数据 Record real-time evaluation data
        List<Double> unitEnergyList =new ArrayList<>(); //当前能量 Current energy
        List<Double> ApproachingIndexForDtList = new ArrayList<>();//记录交货期临近指数用来绘图 Record the near-delivery index（NDI） for drawing
        List<DataForOverdue> dataForOverdueList = new ArrayList<>();//记录超期信息 Record expiration information
        List<Double> ApproachingIndexForDtListXX = new ArrayList<>(); //
        double axx = 0.0; //记录交货期临近指数上一次的值 Record the last value of the NDI
        double exx = 0.0;//记录交货期临近指数误差累积值 Accumulated value of recording error for NDI
        //
        while (isAllFinished){
            //时间计数 Time count
            countT += 1;
            //空闲工位清零，每轮都会重新统计 Clear the idle stations from the previous round of statistics
            shopFloor.resetBufferUsageSituationMap();
            //得到车间机器迭代器 Get workshop machine iterator
            Iterator<Machine> iterMachine = shopFloor.machineList.iterator();

            //[1]遍历所有machine Traverse all machines(or functional units)
            //机床选择工件  模拟执行加工 (Machines select parts) and (simulation of processing)
            while (iterMachine.hasNext()) {
                Machine sMachine = (Machine) iterMachine.next();
                //System.out.println(sMachine.getBufferAmount());
                //查看待接受任务waitingToRecive是否到达，到达的话将该缓存区状态改为待加工WaitingForProcessing
                //sMachine.updateTimeOfPartInTran();
                for(int i=0;i<sMachine.getMybuffer().length;i++){
                    if(sMachine.getMybuffer()[i].getState() == Const.waitingToRecive){
                        sMachine.getArrivalTimeCounter()[i] += 1;
                        //如果计数时间满，代表工件已经到达，缓冲区状态更改为Const.WaitingForProcessing
                        //ToDo：这里Const.transportTime是一个固定值，后续将变为一个随机值
                        if(sMachine.getArrivalTimeCounter()[i] == Const.transportTime){
                            sMachine.getMybuffer()[i].setState(Const.WaitingForProcessing);
                            //等待时间计数清零
                            sMachine.getArrivalTimeCounter()[i] = 0;
                            //刷新buffer中工件最新的接收时间
                            partManager.partVector.
                                    get(sMachine.getMybuffer()[i].getPartID()).setLastAcceptedTime(countT);
                        }
                    }
                }
                //查看buffer区状态,得到正在加工的加工件 ID及其所属buffID
                int[] getProcessingID = sMachine.idForPartProcessing();
                if(getProcessingID[0] != -1 ) {
                    //索引正在加工的工件并更新状态
                    int currentStatusOfPartInProcessing = partManager.partVector.get(getProcessingID[0]).updateStatusForPartInProcessing();
                    //如果此时工件正好加工完成
                    if (currentStatusOfPartInProcessing == 1) {
                        //并将buffer状态改为等待分配         ------------------------记录此时时间
                        //System.out.println("当前步骤加工完成，partID："+getProcessingID[0] +";Machine = " + sMachine.getID() +"; BUfferID = " + getProcessingID[1] + "；时间：" + countT);

                        //数据库记录完成时间，表格中位置根据机器号、工件号以及endTime是否为NULL判断
                        dao.insertEndTime3(sMachine.getID(), getProcessingID[0], countT);
                        //机器是否加工改为false
                        sMachine.setDoesItWork(false);

                        sMachine.changeStatusOfBuffer(Const.waitingForNextStep,getProcessingID[1]);
                        //将该工件ID丢入分配池
                        WipPart thisTASK = new WipPart(getProcessingID[0],sMachine.getID(),getProcessingID[1]);
                        taskContainer.taskContainerGetWipTask(thisTASK);
                    }
                }

                //再次确认是否还有正在加工工件
                getProcessingID = sMachine.idForPartProcessing();
                //确认是否有待加工工件,返回当前机器返回要选择的等待中任务的ID号
                List<int[]> idOfPartWaitingForProcess = sMachine.getIdOfPartWaitingForProcessing();
                //如果机器缓冲区存在待加工工件，且没有正在加工的工件，进行机器选择工件的游戏
                if(getProcessingID[0] == -1 && idOfPartWaitingForProcess.size()>0){
                        //机器选择工件的游戏,（剩余加工时间*一个系数），与交货期较近的会优先加工
                        Iterator<int[]> iterIdOfPartWaitingForProcess = idOfPartWaitingForProcess.iterator();
                        Vector<ElemWaitingPart> waitingParts = new Vector<ElemWaitingPart>();
                        while (iterIdOfPartWaitingForProcess.hasNext()){
                            int[] idAndBufferID =  iterIdOfPartWaitingForProcess.next();
                            int dt = partManager.partVector.get(idAndBufferID[0]).getDeliveryData();//交货期
                            int ot = partManager.partVector.get(idAndBufferID[0]).getOrderTime();//下单时间
                            int rt = partManager.partVector.get(idAndBufferID[0]).getRemainingProcessingTime();//剩余时间
                            int at = partManager.partVector.get(idAndBufferID[0]).getLastAcceptedTime();//该机器接收该工件的时间
                            int pt = partManager.partVector.get(idAndBufferID[0]).getLoad();//得到工件本次需要的加工时间
                            //todo:工件到达机器时间：
                            //需要从数据库中提取
                            waitingParts.add(new ElemWaitingPart(idAndBufferID[0],rt,ot,dt,countT, at, pt));
                        }
                        //
                        //todo:机器选工件规则变化比价
                        Comparator selectWaitingRule = new CompareWaitingPart1();
                        //
                        Collections.sort(waitingParts, selectWaitingRule);
                        //选择排名第一的  -------------------------------------------记录此时时间，工件号，机器，生成查询用的KeyID
                        int partID = waitingParts.get(0).getId();
                        int machineID = sMachine.getID();
                        int startTime =  countT;
                        keyID++;
                        //Todo:再加一个状态，机器选择后不应立刻进入加工，需要有准备时间
                        sMachine.changeStatusThID(partID,Const.waitingForFrocessingToComplete);//开始加工
                        //数据库插入开始时间
                        dao.insertTableForRecodring(keyID,machineID,partID,startTime,0, sMachine.getmachineName());
                        //机器加工状态改为true
                        sMachine.setDoesItWork(true);
                }

                //查看buffer区状态,得到空闲工位数量
                List<Integer> IdNothingInBuffer = sMachine.getIdOfNothingInBuffer();
                //更新未使用工位数量
                shopFloor.unusedBufferNumberRecord(sMachine.getmachineName(),IdNothingInBuffer.size());
            }


            //[2]查看时间，每隔x时间窗口后，遍历任务池，分配任务
            //     一般合同网机制每次都会遍历，有任务就进行任务分配。
            //     博弈论，一个时间窗口内
            //     模糊规则
            List<DataForOverdue> dataForOverdues = new ArrayList<>();//记录此轮超期信息
            if(countT % Const.timeWindow == 0){
                dataForOverdues = taskContainer.taskAssignment(countT);
            }

            //[3] 判断任务池是否能够接受订单，能够接受，转入[3]，不能接受转入[4]
            //输入：不同类型机器buffer数量，
            //todo: 返回能够下放的订单 而不是一刀切
            boolean taskOrder = shopFloor.WhetherToAllowTheTaskToBeReleased();
            //[4] 查看时间，prepareTimeForExports时间后，才能下numberOfWarehouseExports个任务
            //查看排产池，分配任务进入任务池
            if(taskOrder){
                //这次下单时间和前次下单时间必须超过 Const.prepareTimeForExports---仓库出货限制
                if(countT - lastOrderTime > Const.prepareTimeForExports){
                    //排产池针对任务排产并下放
                    pendingTask.decentralizedOrder(countT);
                    //记录下单时间
                    lastOrderTime = countT;
                }
            }

            //查询是否全部做完 false 跳出循环
            isAllFinished = partManager.isAllFinished();
            if(isAllFinished) {
                //[5] AMRM层
                // AMRM层系统分析 - Analysis domain
                //计算交货期临近指数
                double approachingIndexForDt = partManager.getApproachingIndexForDt(countT);
                ApproachingIndexForDtList.add(approachingIndexForDt); //记录交货期临近指数
                if(Const.whetherToUseAMRM) {
                    //调整“parts select machines”
                    if (approachingIndexForDt < 0.65) {
                        double ax = 0.6 - approachingIndexForDt;
                        double ex = ax - axx; //与上一次误差的差值，微分项
                        exx += ax; //误差的累计值，积分项
                        axx = ax; //记录这一次的误差值
                        //AMRM层系数调节 - Analysis domain
                        //double coefficient_energy = 1 - 0.7 * ax - 0.0015 * exx - 0.01 * ex; //可用的搭配
                        //double coefficient_energy = 1 - 0.8 * ax - 0.002 * exx; //可用的搭配
                        double coefficient_energy = 1 - 0.8 * ax - 0.002 * exx - 0.02 * ex;//可用的搭配
                        if (coefficient_energy < 0) coefficient_energy = 0;
                        if (coefficient_energy > 1) coefficient_energy = 0.8;
                        ApproachingIndexForDtListXX.add(coefficient_energy);
                        adjustmentCoefficients.setCoefficient_energy(coefficient_energy);
                        adjustmentCoefficients.setCoefficient_load((1 - coefficient_energy) * 0.5);
                        adjustmentCoefficients.setCoefficient_busyness((1 - coefficient_energy) * 0.5);
                    } else {
                        exx = 0; //消除误差的累计值，积分项
                        axx = 0;
                        adjustmentCoefficients.setCoefficient_energy(0.8);
                        ApproachingIndexForDtListXX.add(0.8);
                        adjustmentCoefficients.setCoefficient_load(0.1);
                        adjustmentCoefficients.setCoefficient_busyness(0.1);
                    }

                    //调整machines select parts
                    if (approachingIndexForDt <= 1 && approachingIndexForDt > 0.8) {
                        adjustmentCoefficients.setCoefficient_dt(0.1);
                        adjustmentCoefficients.setCoefficient_ot(0.4);
                        adjustmentCoefficients.setCoefficient_at(0.5);
                    } else if (approachingIndexForDt <= 0.8 && approachingIndexForDt > 0.6) {
                        adjustmentCoefficients.setCoefficient_dt(0.3);
                        adjustmentCoefficients.setCoefficient_ot(0.3);
                        adjustmentCoefficients.setCoefficient_at(0.4);
                    } else if (approachingIndexForDt <= 0.6 && approachingIndexForDt > 0.4) {
                        adjustmentCoefficients.setCoefficient_dt(0.5);
                        adjustmentCoefficients.setCoefficient_ot(0.2);
                        adjustmentCoefficients.setCoefficient_at(0.3);
                    } else {
                        adjustmentCoefficients.setCoefficient_dt(0.8);
                        adjustmentCoefficients.setCoefficient_ot(0.1);
                        adjustmentCoefficients.setCoefficient_at(0.1);
                    }
                }
                //计算瓶颈资源指数-----当前机器繁忙度
                shopFloor.refreshBusyness();
            }

            //[6] 实时数据统计
            //统计单位时间耗能信息
            Iterator<Machine> iterMachine_statistics = shopFloor.machineList.iterator();
            double unitEnergy = 0.0;
            while (iterMachine_statistics.hasNext()) {
                Machine yMachine = (Machine) iterMachine_statistics.next();
                if (yMachine.getDoesItWork() == true) {
                    unitEnergy += yMachine.getEnergyConsumption();
                }
            }
            unitEnergyList.add(unitEnergy);
            //总耗能
            //allEnergy += unitEnergy;

            //统计超期信息
            if(dataForOverdues.size()>0){
                Iterator<DataForOverdue> iterDataForOverdues = dataForOverdues.iterator();
                while (iterDataForOverdues.hasNext()){
                    dataForOverdueList.add(iterDataForOverdues.next());
                }
            }


        }

        System.out.println("总用时 = " + countT);
        for(int i=0;i<unitEnergyList.size();i++){
            allEnergy += unitEnergyList.get(i);
        }
        System.out.println("总耗能 = " + allEnergy);


        /**
         * 以下为数据绘图 The following is data plotting.
         */
        DataPlotting myDataPlot = new DataPlotting();
        //绘制实时耗能图
        myDataPlot.plotUnitEnergy(unitEnergyList,countT);
        //绘制交货期临近指数
        myDataPlot.plotApproachingIndexForDt(ApproachingIndexForDtList,countT);
        //
        myDataPlot.plotApproachingIndexForDtXX(ApproachingIndexForDtListXX,countT);
        //绘制交货期超期统计图
        myDataPlot.plotOverdueArtifacts(dataForOverdueList);
        //System.out.println(dataForOverdueList.size());
        //绘制每台机器的负载曲线
        myDataPlot.plotSingleMachineLoadChange(shopFloor.getDataForBusynessList(),countT);
        myDataPlot.plotSingleUseOfWorkstations(shopFloor.getDataForBusynessList(),countT);
        myDataPlot.plotSingleMachineBusynessChange(shopFloor.getDataForBusynessList(),countT);
        //绘制最大负载曲线
        myDataPlot.plotMaxMachineLoadChange(shopFloor.getDataForMaxBusynessList(),countT);


        //绘制甘特图
        new GanttPlotSvg(DataProcess.dataprocess(),countT);

        //关闭数据库
        dao.closeConn();
        //打开数据图
        DemoOpenFile openFile = new DemoOpenFile();
        openFile.openFile("output/out.md");
    }
}
