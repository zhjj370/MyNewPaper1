package org.zhjj370.rule.elemp;

public class ElemPendingTask{
    private int idVal;
    private int dTime;
    private int oTime;
    private int currentT;

    public ElemPendingTask(int id, int dTime, int oTime, int currentT) {
        this.idVal = id;
        this.dTime = dTime;
        this.oTime = oTime;
        this.currentT = currentT;
    }

    public int getIdVal() {
        return this.idVal;
    }

    public int getDTime() {
        return dTime;
    }

    public int getOTime() {
        return oTime;
    }

    public int getCurrentT() {
        return currentT;
    }

    //判断函数
    public int valueRule1(){
        int ruleV = 0;
        //归一化-任务下达时间对现在时间

        //归一化-任务交货时间对现在时间

        return ruleV;
    }
}
