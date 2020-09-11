package org.zhjj370.functions.element;

/**
 * 自定义的数据类型，用来完成
 */
public class DataForGantt {
    private String machineName;
    private String partName;
    private int startTme;
    private int endTime;

    public String getMachineName() {
        return machineName;
    }

    public String getPartName() {
        return partName;
    }

    public int getStartTme() {
        return startTme;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public void setStartTme(int startTme) {
        this.startTme = startTme;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

}
