package org.zhjj370.basic;


import org.zhjj370.container.PartManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class Machine {
    //私有域
    private String machineTypeID = null;
    private String machineName = null;
    private int bufferAmount = 0;
    private Buffer[] mybuffer;
    private String[] myAbility;
    private int[] arrivalTimeCounter;
    private int ID;
    private double energyConsumption = 0;
    private int life = 0;
    private boolean doesItWork = false;

    public String getMachineTypeID(){
        return machineTypeID;
    }

    public String getmachineName(){
        return machineName;
    }

    public int getBufferAmount(){
        return bufferAmount;
    }

    public String[] getMyAbility(){
        return myAbility;
    }

    public int getID() {
        return ID;
    }

    public double getEnergyConsumption() {
        return energyConsumption;
    }

    public int getLife() {
        return life;
    }

    public boolean getDoesItWork(){
        return doesItWork;
    }

    public void setDoesItWork(boolean doesItWork) {
        this.doesItWork = doesItWork;
    }

    public Buffer[] getMybuffer() {
        return mybuffer;
    }

    public int[] getArrivalTimeCounter() {
        return arrivalTimeCounter;
    }


    /**
     * 构造函数
     *
     */
    public Machine(String machineTypeID, String machineName,
                   int bufferAmount, Vector<String> ability,int ID,
                   double energyConsumption, int life){
        this.bufferAmount = bufferAmount;
        this.machineName = machineName;
        this.machineTypeID = machineTypeID;
        //构造缓冲区
        this.mybuffer = new Buffer[bufferAmount];
        for(int i=0;i<mybuffer.length;i++){
            mybuffer[i] = new Buffer();
        }

        //时间计数器，在计数时间达到Const.transportTime时，工件到达缓冲区
        this.arrivalTimeCounter = new int[bufferAmount];
        for(int i=0;i<arrivalTimeCounter.length;i++){
            arrivalTimeCounter[i] = 0;
        }
        this.myAbility = new String[ability.size()];
        for(int i=0;i<ability.size();i++){
            myAbility[i] = ability.get(i);
        }
        this.ID = ID;

        this.energyConsumption = energyConsumption;
        this.life = life;
    }

    /**
     * 查看buffer区状态，确定正在加工工件序列号,以及对应的buffer区序号
     * @return [加工工件的id号，buffer区序号]
     *
     */
    public int[] idForPartProcessing(){
        int partId = -1;
        int bufferId = -1;
        int i = 0;
        for(Buffer currentBuffer: mybuffer){
            if(currentBuffer.getState() == Const.waitingForFrocessingToComplete){
                partId = currentBuffer.getPartID();
                bufferId = i;
                break;
            }
            i=i+1;
        }
        int[] x = {partId,bufferId};
        return x;
    }

    /**
     * 返回等待加工工件ID以及bufferID
     * @return
     */
    public List<int[]>  getIdOfPartWaitingForProcessing(){
        //遍历buffer，得到待加工工件序号
        List<int[]> idPartForMachining = new ArrayList<int[]>();
        for(int i = 0;i < mybuffer.length;i++){
            if(mybuffer[i].getState() == Const.WaitingForProcessing){
                int partId = mybuffer[i].getPartID();
                int bufferId = i;
                int[] x = {partId,bufferId};
                idPartForMachining.add(x);
            }
        }
        return idPartForMachining;
    }

    /**
     * 返回无工件bufferID
     * @return
     */
    public List<Integer>  getIdOfNothingInBuffer(){
        //遍历buffer，得到待加工工件序号
        List<Integer> idNothingInBuffer = new ArrayList<Integer>();
        for(int i = 0;i < mybuffer.length;i++){
            if(mybuffer[i].getState() == Const.nothingInBuffer){
                idNothingInBuffer.add(i);
            }
        }
        return idNothingInBuffer;
    }

    /**
     * 返回机器上所有的工件号，包括待接受的工件
     * @return
     */
    public List<Integer> getIdInBuffer() {
        List<Integer> idArray = new ArrayList<>();
        for(int i = 0;i < mybuffer.length;i++){
            int state = mybuffer[i].getState();
            if( state != Const.nothingInBuffer && state != Const.waitingForNextStep){
                idArray.add(mybuffer[i].getPartID());
            }
        }
        return idArray;
    }

    public List<Integer> getPartIdInBuffer() {
        List<Integer> idArray = new ArrayList<>();
        for(int i = 0;i < mybuffer.length;i++){
            int state = mybuffer[i].getState();
            if( state != Const.nothingInBuffer ){
                idArray.add(mybuffer[i].getPartID());
                System.out.println("i =" + i + "        " + mybuffer[i].getState());
            }
        }
        return idArray;
    }

    /**
     * 返回waitingForNextStep()的工位
     */
    public List<Integer> getPartIdOfWaitingForNextStep() {
        List<Integer> idArray = new ArrayList<>();
        for(int i = 0;i < mybuffer.length;i++){
            int state = mybuffer[i].getState();
            if( state == Const.waitingForNextStep ){
                idArray.add(mybuffer[i].getPartID());
            }
        }
        return idArray;
    }

    /**
     * 返回空闲率
     */
    public double getIdleRateOfMachine(){
        double idleRate = 0.0;
        int sizeOfNothing = getIdOfNothingInBuffer().size();
        idleRate =  ((double) sizeOfNothing) / ((double) mybuffer.length);
        return idleRate;
    }


    /**
     * 更改buffer区状态
     * @param status
     * @param bufferID
     */
    public void changeStatusOfBuffer(int status,int bufferID){
        mybuffer[bufferID].setState(status);
        //如果staus = nothingInBuffer , buffer 对应的工件号变为-1；
        if(status == Const.nothingInBuffer){
            mybuffer[bufferID].setPartID(-1);
        }
    }

    /**
     * 更改buffer区状态,通过索引ID号
     * @param partID
     * @param status
     */
    public void changeStatusThID(int partID, int status){
        for(int i=0;i<mybuffer.length;i++){
            if(mybuffer[i].getPartID() == partID){
                mybuffer[i].setState(status);
                break;
            }
        }
    }

    /**
     * 接收工件
     * @param ID=要接受工件的ID号
     */
    public void receivePart(int ID){
        for(int i = 0;i < mybuffer.length;i++){
            if(mybuffer[i].getState() == Const.nothingInBuffer){
                mybuffer[i].setPartID(ID);
                mybuffer[i].setState(Const.waitingToRecive);
                //Todo:测试用，找出负载没有正确计算原因
                //System.out.println("机器号："+ getID() + "，分配工位号：" + i);
                break;
            }
        }
    }


    /**
     * 更新接收时间,+1,并判断是否到达，如果到达改变状态
     */
    public void updateTimeOfPartInTran(){
        //遍历缓冲区
        for(int i=0;i<mybuffer.length;i++){

            if(mybuffer[i].getState() == Const.waitingToRecive){
                arrivalTimeCounter[i] += 1;
                //如果计数时间满，代表工件已经到达，缓冲区状态更改为Const.WaitingForProcessing
                //ToDo：这里Const.transportTime是一个固定值，后续将变为一个随机值
                if(arrivalTimeCounter[i] == Const.transportTime){
                    mybuffer[i].setState(Const.WaitingForProcessing);
                    //等待时间计数清零
                    arrivalTimeCounter[i] = 0;
                    //刷新buffer中工件最新的接收时间

                }
            }
        }
    }


    /**
     * 得到机器当前负载
     * @return
     */
    public int getLoad(){
        int load = 0 ;
        List<Integer> getIdInBuffer = getIdInBuffer();
        PartManager partManager = PartManager.getPartManager();
        Iterator<Integer> iterForLoad = getIdInBuffer.iterator();
        //计算负载·
        while(iterForLoad.hasNext()){
            load += partManager.partVector.get(iterForLoad.next()).getLoad();
        }
        return load;
    }

    /**
     * 得到机器当前繁忙度
     * @return
     */
    public double getBusyness(){
        double busyness = 0 ;
        double load = getLoad();  //负载
        double u = 1-getIdleRateOfMachine(); //得到数量
        /*if(nPart<=1){
            busyness = 0 ;
        }
        else {
            busyness = load/(nPart-1);
        }*/
        busyness = load*u;
        return busyness;
    }
}
