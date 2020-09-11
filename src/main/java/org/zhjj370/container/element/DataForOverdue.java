package org.zhjj370.container.element;

/**
 * 记录超期数据
 */
public class DataForOverdue {
    private int partID;
    private int finishTime;
    private int overTime;

    public DataForOverdue(int partID,int finishTime,int overTime){
        this.partID = partID;
        this.finishTime = finishTime;
        this.overTime = overTime;
    }

    public int getPartID() {
        return partID;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public int getOverTime() {
        return overTime;
    }

    public void setPartID(int partID) {
        this.partID = partID;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public void setOverTime(int overTime) {
        this.overTime = overTime;
    }
}
