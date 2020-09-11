package org.zhjj370.container.element;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录设备的繁忙指数
 */
public class DataForBusyness {
    private int machineID;
    private String machineName;
    private List<Double> dataList = new ArrayList<>(); //记录负载
    private List<Integer> stationUsage = new ArrayList<>(); //记录工位使用情况
    private List<Double> busynessList = new ArrayList<>(); //记录繁忙度

    public DataForBusyness(int machineID, String machineName){
        this.machineID = machineID;
        this.machineName = machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    public String getMachineName() {
        return machineName;
    }

    public int getMachineID() {
        return machineID;
    }

    public List<Double> getDataList() {
        return dataList;
    }

    public List<Integer> getStationUsage() {
        return stationUsage;
    }

    public List<Double> getBusynessList() {
        return busynessList;
    }
}
