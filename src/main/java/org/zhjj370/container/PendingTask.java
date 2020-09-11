package org.zhjj370.container;

import org.zhjj370.basic.Const;
import org.zhjj370.basic.Machine;
import org.zhjj370.basic.WipPart;
import org.zhjj370.rule.ComparePendingTask;
import org.zhjj370.rule.elemp.ElemPendingTask;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 */
public class PendingTask {

    private static PendingTask pendingTask = new PendingTask();
    private Vector<Integer> waitingTask = new Vector<Integer>();

    public PendingTask(){
        //[1]获取任务数量
        PartManager object = PartManager.getPartManager();
        //[2]建立waitingTask
        waitingTask = new Vector(object.partVector.size());
        for(int i=0; i< object.partVector.size();i++){
            waitingTask.add(i);
        }

        System.out.println("排产池长度：" + waitingTask.size());
    }

    /**
     * 获取唯一可用对象
     * @return
     */
    public static PendingTask getPendingTask() {
        return pendingTask;
    }

    /**
     * 排列
     * 查看还能下放多少订单
     */
    public void makeSecquence(){
        //[1] 获取车间层，遍历车间层，得到加工工件剩余时间，缓存区工件剩余时间，缓存区数量
        ShopFloor object = ShopFloor.getShopFloor();
        Iterator<Machine> iter = object.machineList.iterator();

    }

    /**
     * 下放订单
     */
    public void decentralizedOrder(int countT){
        TaskContainer taskContainer = TaskContainer.getTaskContainer();
        //ShopFloor shopFloor = ShopFloor.getShopFloor();
        PartManager partManager = PartManager.getPartManager();
        //参与排序的任务号集合
        Vector<ElemPendingTask> schedulingPart = new Vector<ElemPendingTask>();
        //遍历排产池中的任务，确定当前能下的订单
        for(int i = 0; i < waitingTask.size(); i++){
            //索引waitingTask[i]号任务工件下单的时间
            //System.out.println(waitingTask.get(i));
            int orderTime = partManager.partVector.get(waitingTask.get(i)).getOrderTime();
            //判断是否在规定时间内
            if(countT >= orderTime){
                int id = waitingTask.get(i);
                int dt = partManager.partVector.get(id).getDeliveryData();
                schedulingPart.add(new ElemPendingTask(id,dt,orderTime,countT));
            }
        }

        /**排序
         * 考虑因素
         * 1、交货期
         * 2、订单时间
         * 以上两个好办，交货期优先，0.9:0.1的关系
         *
         * 3、考虑负载------即当前加工车间是否允许下单，在本例中暂时不考虑，快有瓶颈资源产生时不再下单
         *
         */
        //重新排序当前时间前面的工件任务
        Comparator pendingRule = new ComparePendingTask();
        Collections.sort(schedulingPart, pendingRule);
        //将排列好的部分嫁接到waitingTask上
        for(int i = 0 ;i < schedulingPart.size(); i++){
            waitingTask.set(i, schedulingPart.get(i).getIdVal());
        }

        if(schedulingPart.size() >= Const.numberOfWarehouseExports){
            //下放订单
            for(int i=0;i < Const.numberOfWarehouseExports; i++){
                //将排在排产池队列中的第一个元素给任务分配池
                WipPart thisTASK = new WipPart((Integer) waitingTask.get(0),-1,-1);
                //TODO：打印出来，之后删除
                System.out.println("下放工件，id="+thisTASK.getPartID()+";时间="+countT);
                taskContainer.taskContainerGetWipTask(thisTASK);
                //同时移除该任务
                waitingTask.remove(0);
            }
        }
        else{
            //下放订单
            for(int i=0;i < schedulingPart.size(); i++){
                //将排在排产池队列中的第一个元素给任务分配池
                WipPart thisTASK = new WipPart((Integer) waitingTask.get(0),-1,-1);
                //TODO：打印出来，之后删除
                System.out.println("下放工件，id="+thisTASK.getPartID()+";时间="+countT);
                taskContainer.taskContainerGetWipTask(thisTASK);
                //同时移除该任务
                waitingTask.remove(0);
                if(waitingTask.size() == 0){
                    System.out.println("任务下达完成");
                }
            }
        }



    }
}
