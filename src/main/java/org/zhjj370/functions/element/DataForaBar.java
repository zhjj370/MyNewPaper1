package org.zhjj370.functions.element;

public class DataForaBar {
    private double value;
    private String rowKey;
    private String coloumKey;

    public DataForaBar(double value,String rowKey,String coloumKey){
        this.value = value;
        this.rowKey = rowKey;
        this.coloumKey = coloumKey;
    }

    public double getValue() {
        return value;
    }

    public String getColoumKey() {
        return coloumKey;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setColoumKey(String coloumKey) {
        this.coloumKey = coloumKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
