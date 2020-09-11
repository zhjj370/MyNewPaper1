package org.zhjj370.functions;

import org.zhjj370.basic.Machine;
import org.zhjj370.container.PartManager;
import org.zhjj370.container.ShopFloor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GetMachineForStepProcess {

    /**
     * 工件选机床，返回判断条件
     * @param stepName
     * @return [0]机床ID [1]机床负载
     */
    public static List<int[]> getMachinesForStepProcess0(String stepName){
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
                    //返回非空闲位置的工件ID
                    List<Integer> getIdInBuffer = nMachine.getIdInBuffer();
                    //代表还有空位
                    if(amountNone > 0 ){
                        Iterator<Integer> iterForLoad = getIdInBuffer.iterator();
                        //计算负载
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
    }

    /**
     * 工件选机床，返回判断条件
     * @param stepName
     * @return list<数组> [0]机床ID [1]机床负载 [2]剩余工位数量
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
