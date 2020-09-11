package org.zhjj370.basic;

/**
 * 内部类，构造一个Buffer
 *
 */
public class Buffer{
    //用于记录工件号
    private int partID = -1;
    //用于记录当前状态
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