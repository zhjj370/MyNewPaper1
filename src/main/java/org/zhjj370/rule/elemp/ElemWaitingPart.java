package org.zhjj370.rule.elemp;

public class ElemWaitingPart {
    private int id;//工件id
    private int rTime;//剩余加工时间
    private int oTime;//订单时间
    private int dTime;//交货时间
    private int cTime;//现在时间
    private int aTime;//当前机器接收该工件时间
    private int pTime;//该工件本次加工时间

    public ElemWaitingPart(int id, int rTime, int oTime, int dTime, int cTime, int aTime, int pTime){
        this.id = id;
        this.rTime = rTime;
        this.cTime = cTime;
        this.oTime = oTime;
        this.dTime = dTime;
        this.aTime = aTime;
        this.pTime = pTime;
    }


    public int getId(){
        return id;
    }

    public int getdTime() {
        return dTime;
    }

    public int getcTime() {
        return cTime;
    }

    public int getoTime() {
        return oTime;
    }

    public int getrTime() {
        return rTime;
    }

    public int getaTime() {
        return aTime;
    }

    public int getpTime() {
        return pTime;
    }
}
