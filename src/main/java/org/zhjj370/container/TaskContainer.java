package org.zhjj370.container;

import org.zhjj370.basic.Const;
import org.zhjj370.basic.Machine;
import org.zhjj370.basic.WipPart;
import org.zhjj370.container.element.DataForOverdue;
import org.zhjj370.rule.CompareMaCapacity1;
import org.zhjj370.rule.elemp.ElemMaCapacity;

import java.util.*;

/**
 *
 */
public class TaskContainer {
    private static TaskContainer taskContainer = new TaskContainer();
    //待分配在制品列表
    public List<WipPart> wipList = new ArrayList<WipPart>();



    public TaskContainer(){

    }
    /**
     * 获取唯一可用对象
     * @return
     */
    public static TaskContainer getTaskContainer() {
        return taskContainer;
    }


    /**
     * 任务分配池获得任务ID
     *
     */
    public void taskContainerGetWipTask(WipPart task){
        wipList.add(task);
    }

    /**
     * 打印wip中的任务
     */
    public void printlnWIP(){
        Iterator<WipPart> iter = wipList.iterator();
        while (iter.hasNext()) {
            WipPart s = (WipPart) iter.next();
            System.out.println("任务：  " + s.getPartID());
        }
    }

    /**
     *
     * @param countT
     */
    public List<DataForOverdue> taskAssignment(int countT){
        //超期工件信息统计
        List<DataForOverdue> dataForOverdueList = new ArrayList<>();
        //TaskContainer taskContainer = TaskContainer.getTaskContainer();
        ShopFloor shopFloor = ShopFloor.getShopFloor();
        PartManager partManager = PartManager.getPartManager();
        //排序（沈略）
        //遍历工件，查询当前步骤
        Iterator<WipPart> iterWIP = taskContainer.wipList.iterator();
        List<WipPart>  listHaveDone = new ArrayList<>();
        while (iterWIP.hasNext()){
            //得到要安排生产的工件,以及它的来源
            WipPart wipPart=  iterWIP.next();
            int idWIP = wipPart.getPartID();
            int fromWhatMa = wipPart.getMachineID();
            int fromWhatBu = wipPart.getBufferID();

            //获取当前工件要安排的步骤
            String stepName = partManager.partVector.get(idWIP).getNextStep();
            //todo:测试用
            //System.out.println("安排生产：" + idWIP + ",来源：" + fromWhatMa + "，步骤：" + stepName + "时间：" + countT);

            if(stepName.equals("Finished")){
                //送回仓库
                partManager.partVector.get(idWIP).setIsFinished(true);//完成
                System.out.println( "工件" + idWIP +"is Finished");
                //判断是否超期，并将超期信息统计到列表
                if(countT>partManager.partVector.get(idWIP).getDeliveryData()){
                    dataForOverdueList.add(new DataForOverdue(idWIP,countT,
                            countT-partManager.partVector.get(idWIP).getDeliveryData()));
                }

                //
                if(fromWhatMa != -1){
                    //分配出去的，改变原buffer位，变为nothing：只要分配出去了，会被立刻拿走
                    //Todo:加一个状态，不会直接拿走，等一个随机时间拿走
                    shopFloor.machineList.get(fromWhatMa).changeStatusOfBuffer(Const.nothingInBuffer,fromWhatBu);
                }
                //移除
                //taskContainer.wipList.remove(wipPart);
                listHaveDone.add(wipPart);
            }else{
                //listForResourceDeterc 用来存储判断规则中要用到的信息
                /*List<int[]> listForResourceDeter = new ArrayList<int[]>();
                listForResourceDeter = getMachinesForStepProcess0(stepName);*/

                List<ElemMaCapacity> listForResourceDeter = new ArrayList<>(); //用于对比的列表
                listForResourceDeter = getMachinesForStepProcess0(stepName);

                //rule:工件选机床
                if(listForResourceDeter.size()>0){
                    Comparator maRule = new CompareMaCapacity1();
                    Collections.sort(listForResourceDeter, maRule);
                    //排第一的接收工件，并将一个空闲位变为待接收位
                    shopFloor.machineList.get(listForResourceDeter.get(0).getMachineID()).receivePart(idWIP);
                    if(fromWhatMa != -1){
                        //分配出去的，改变原buffer位，变为noting，这里有个假设：只要分配出去了，会被立刻拿走
                        //Todo:加一个状态，不会直接拿走，等一个随机时间拿走
                        shopFloor.machineList.get(fromWhatMa).changeStatusOfBuffer(Const.nothingInBuffer,fromWhatBu);
                    }

                    //移除
                    //taskContainer.wipList.remove(wipPart);
                    listHaveDone.add(wipPart);
                    //System.out.println(taskContainer.wipList.size());
                }
            }
        }
        //移除已经分配的任务
        Iterator<WipPart> iterHaveDone = listHaveDone.iterator();
        while(iterHaveDone.hasNext()){
            taskContainer.wipList.remove(iterHaveDone.next());
        }

        return dataForOverdueList;
    }

    /**
     * 工件选机床，返回判断条件
     * @param stepName
     * @return [0]机床ID [1]机床负载
     */
    /*public static List<int[]> getMachinesForStepProcess0(String stepName){
        //获取容器
        ShopFloor shopFloor = ShopFloor.getShopFloor();
        PartManager partManager = PartManager.getPartManager();
        //listForResourceDeterc 用来存储判断规则中要用到的信息
        List<int[]> listForResourceDeter = new ArrayList<int[]>();
        Iterator<Machine> iterMachine1 = shopFloor.machineList.iterator();
        while(iterMachine1.hasNext()){
            Machine nMachine = iterMachine1.next();
            String[] ability = nMachine.getMyAbility();
            //判断是否能做
            for(String abilityElement:ability){
                if(abilityElement.equals(stepName)){
                    int idOfMaCanDo = nMachine.getID();
                    int currentLoad = 0;
                    //空闲工位数量
                    int amountNone = nMachine.getIdOfNothingInBuffer().size();
                    if(amountNone==0){System.out.println("设备"+ nMachine.getID()+" 空闲工位为0");}
                    //返回非空闲位置的工件ID
                    List<Integer> getIdInBuffer = nMachine.getIdInBuffer();
                    //代表还有空位
                    if(amountNone > 0 ){
                        Iterator<Integer> iterForLoad = getIdInBuffer.iterator();
                        //计算负载·
                        while(iterForLoad.hasNext()){
                            currentLoad += partManager.partVector.get(iterForLoad.next()).getLoad();
                        }
                        //[机床号 负载信息]
                        int[] idAndLoad = {idOfMaCanDo,currentLoad};
                        //todo:输出机床负载，后续删除
                        System.out.println("机床号="+ idAndLoad[0] +";  负载=" + idAndLoad[1]);
                        //将机床号和负载放入数组LIST
                        listForResourceDeter.add(idAndLoad);
                        break;
                    }
                }
            }
        }
        return listForResourceDeter;
    }*/

    public static List<ElemMaCapacity> getMachinesForStepProcess0(String stepName){
        //获取容器
        ShopFloor shopFloor = ShopFloor.getShopFloor();
        PartManager partManager = PartManager.getPartManager();
        //listForResourceDeterc 用来存储判断规则中要用到的信息
        List<ElemMaCapacity> listForResourceDeter = new ArrayList<>();
        Iterator<Machine> iterMachine1 = shopFloor.machineList.iterator();
        while(iterMachine1.hasNext()){
            Machine nMachine = iterMachine1.next();
            String[] ability = nMachine.getMyAbility();
            //判断是否能做
            for(String abilityElement:ability){
                if(abilityElement.equals(stepName)){
                    //int idOfMaCanDo = nMachine.getID();
                    int currentLoad = 0;
                    //空闲工位数量
                    int amountNone = nMachine.getIdOfNothingInBuffer().size();
                    //if(amountNone==0){System.out.println("设备"+ nMachine.getID()+" 空闲工位为0");}
                    //代表还有空位
                    if(amountNone > 0 ){
                        int load = nMachine.getLoad();
                        double busyness = 1-nMachine.getIdleRateOfMachine();
                        double energy = nMachine.getEnergyConsumption();
                        int idMachine = nMachine.getID();
                        String name = nMachine.getmachineName();
                        ElemMaCapacity myAbility = new ElemMaCapacity(load,busyness,energy,idMachine,name);
                        //todo:输出机床负载，后续删除
                        //System.out.println("机床号="+ idAndLoad[0] +";  负载=" + idAndLoad[1]);
                        //将机床号和负载放入数组LIST
                        listForResourceDeter.add(myAbility);
                        break;
                    }
                }
            }
        }
        return listForResourceDeter;
    }


    /**
     * 工件选机床，返回判断条件
     * @param stepName
     * @return list<数组> [0]机床ID [1]机床负载 [2]剩余工位数量 [3]机床耗能 [4]机床寿命
     */
    public static List<int[]> getMachinesForStepProcess1(String stepName){
        //获取容器
        ShopFloor shopFloor = ShopFloor.getShopFloor();
        PartManager partManager = PartManager.getPartManager();
        //listForResourceDeterc 用来存储判断规则中要用到的信息
        List<int[]> listForResourceDeter = new ArrayList<int[]>();

        //遍历shopfloor获取所要信息
        Iterator<Machine> iterMachine1 = shopFloor.machineList.iterator();
        while(iterMachine1.hasNext()){
            Machine nMachine = iterMachine1.next();
            String[] ability = nMachine.getMyAbility();
            //判断是否能做
            for(String abilityElement:ability){
                if(abilityElement.equals(stepName)){
                    int idOfMaCanDo = nMachine.getID();
                    int currentLoad = 0;
                    //空闲工位数量
                    int amountNone = nMachine.getIdOfNothingInBuffer().size();
                    //返回非空闲位置的工件ID
                    List<Integer> getIdInBuffer = nMachine.getIdInBuffer();
                    //返回waiting for next step ID
                    List<Integer> getIdOfWaitingForNextStep = nMachine.getPartIdOfWaitingForNextStep();
                    //返回空闲工位与总工位比值
                    double idleRateOfMachine = nMachine.getIdleRateOfMachine();
                    //返回耗能


                    //代表还有空位
                    if(amountNone > 0 ){
                        Iterator<Integer> iterForLoad = getIdInBuffer.iterator();
                        //计算负载
                        while(iterForLoad.hasNext()){
                            currentLoad += partManager.partVector.get(iterForLoad.next()).getLoad();
                        }
                        //[机床号 负载信息]
                        int[] idAndLoad = {idOfMaCanDo,currentLoad};
                        //将机床号和负载放入数组LIST
                        listForResourceDeter.add(idAndLoad);
                        break;
                    }
                }
            }
        }
        return listForResourceDeter;
    }

}
