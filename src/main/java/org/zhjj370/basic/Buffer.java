package org.zhjj370.basic;

/**
 * A buffer station
 * @author Zequn ZHANG
 */
public class Buffer{
    //Used to record the part number，-1 means no part in this station
    private int partID = -1;
    //用于记录当前状态 Used to record the current state
    private int state = Const.nothingInBuffer;

    public void setPartID(int partID){
        this.partID = partID;
    }

    public void setState(int state){
        this.state = state;
    }

    public int getPartID(){
        return partID;
    }

    public int getState(){
        return state;
    }

}