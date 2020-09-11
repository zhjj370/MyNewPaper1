package org.zhjj370.basic;

/**
 * 记录待分配任务序号以及所处机器
 */
public class WipPart {
    private int partID;
    private int machineID;//-1代表从仓库出来
    private int bufferID;//

    public WipPart(int partID,int machineID,int bufferID){
        this.bufferID = bufferID;
        this.machineID = machineID;
        this.partID = partID;
    }

    public void setBufferID(int bufferID) {
        this.bufferID = bufferID;
    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    public void setPartID(int partID) {
        this.partID = partID;
    }

    public int getBufferID() {
        return bufferID;
    }

    public int getMachineID() {
        return machineID;
    }

    public int getPartID() {
        return partID;
    }
}
