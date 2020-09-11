package org.zhjj370.rule.elemp;

public class ElemMaCapacity {
    private int load; //负载
    private double busyness; //繁忙度
    private double energyConsumption; //耗能
    private int machineID; // 机器编号
    private String machineName;// 机器名称


    public ElemMaCapacity(int load, double busyness, double energyConsumption,int machineID, String machineName){
        this.load = load;
        this.busyness = busyness;
        this.energyConsumption = energyConsumption;
        this.machineID = machineID;
        this.machineName = machineName;
    }

    public double getBusyness() {
        return busyness;
    }

    public double getEnergyConsumption() {
        return energyConsumption;
    }

    public int getLoad() {
        return load;
    }

    public int getMachineID() {
        return machineID;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setBusyness(double busyness) {
        this.busyness = busyness;
    }

    public void setEnergyConsumption(double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public void setLoad(int load) {
        this.load = load;
    }
}
