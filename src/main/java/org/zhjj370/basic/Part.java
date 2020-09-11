package org.zhjj370.basic;

import java.util.Random;

public class Part {
    private String partTypeID; //类型编号，没什么用
    private String partTypeName;// 工件类型名字
    private String[] processing;// 工艺步骤
    private int[] time;// 每步耗时
    private int orderTime;// 下单时间
    private int deliveryData;//交货期
    private int taskID;

    private int lastAcceptedTime = 0; //最近一次的接收时间

    //Random random = new Random(Const.seed);//随机数
    Random random = new Random();

    private int[] progerssRate; //记录时间进度

    private boolean isFinished = false; //记录工件是否完成

    public int getDeliveryData() {
        return deliveryData;
    }

    public int getOrderTime() {
        return orderTime;
    }

    public String getPartTypeID() {
        return partTypeID;
    }

    public String getPartTypeName() {
        return partTypeName;
    }

    public int[] getTime() {
        return time;
    }

    public String[] getProcessing(){
        return processing;
    }

    public int getTaskID(){
        return taskID;
    }

    public void setIsFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    public void setLastAcceptedTime(int lastAcceptedTime) {
        this.lastAcceptedTime = lastAcceptedTime;
    }

    public int getLastAcceptedTime() {
        return lastAcceptedTime;
    }

    public Part(String partTypeID, String partTypeName, String processing, String atime, int orderTime, int deliveryData, int taskID){

        this.deliveryData = deliveryData;
        this.orderTime = orderTime;
        this.partTypeID = partTypeID;
        this.partTypeName = partTypeName;

        //time和processing需要拆分
        String[] sTime = atime.split("-");
        this.time = new int[sTime.length];
        for(int i=0;i<sTime.length;i++){
            this.time[i] = Integer.parseInt(sTime[i]);
            //todo: 增加随机性，以下加工时间为实际完成时间
            //有70%可能性实际时间改变
            if(Const.whetherToUseRandom) {
                int probabilityTime = random.nextInt(100);
                if (probabilityTime < Const._ptime) {
                    //实际加工时间改变增幅为20%以内
                    int increase = random.nextInt(Const._growth);
                    this.time[i] = this.time[i] * (100 + increase) / 100;
                }
            }
        }
        this.processing = processing.split("-");
        this.taskID = taskID;

        this.progerssRate = new int[sTime.length];
        for(int i=0;i<progerssRate.length;i++){
            progerssRate[i] = 0;
        }
    }


    /**
     * 更新加工中工件的状态
     * @return 0代表还在加工，1代表已经加工完毕
     */
    public int updateStatusForPartInProcessing(){
        int isCompleted = 0;
        //循环遍历progerssRate,判断所处工序
        for(int i=0;i<progerssRate.length;i++){
            //找到正在加工的工序
            if(progerssRate[i] < time[i]){
                //时间+1
                progerssRate[i] +=1;
                if(progerssRate[i]==time[i]){isCompleted = 1;}
                else{isCompleted = 0;}
                break;
            }
        }
        return isCompleted;
    }


    /**
     * 获取剩余加工时间
     * @return
     */
    public int getRemainingProcessingTime(){
        int remainingProcessingTime = 0;
        int currentStep = 0;
        boolean x_WhetherToCalculation = true;
        for(int i=0;i<progerssRate.length;i++){
            if(progerssRate[i] < time[i]){
                currentStep = i;
                break;
            }
            if(i == (progerssRate.length-1)){
                x_WhetherToCalculation = false; //说明此时工件已经加工完毕。
            }
        }
        if(x_WhetherToCalculation) {
            for (int a = currentStep; a < time.length; a++) {
                if (a == currentStep) {
                    remainingProcessingTime = time[a] - progerssRate[a];
                } else {
                    remainingProcessingTime += time[a];
                }
            }
        }
        return remainingProcessingTime;
    }

    /**
     * 获取马上要做的下一步加工时间
     */
    public int getLoad(){

        int load = 0;
        int currentStep = 0;
        if(isFinished == false) {
            for (int i = 0; i < progerssRate.length; i++) {
                if (progerssRate[i] < time[i]) {
                    currentStep = i;
                    break;
                }
            }
            load = time[currentStep] - progerssRate[currentStep];
        }
        return load;
    }

    /**
     * 获取下一步要做什么
     *
     */
    public String getNextStep(){
        int currentStep = -1;
        String nextStep = "";
        for(int i=0;i<progerssRate.length;i++){
            if(progerssRate[i] == 0){
                currentStep = i;
                break;
            }
        }
        if(currentStep != -1){
            nextStep = processing[currentStep];
        }
        else{nextStep = "Finished";}
        return nextStep;
    }


}
