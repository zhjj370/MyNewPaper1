package org.zhjj370.basic;

/**
 * 一个配置文件 A configuration file
 * @author zhjj370
 */
public class Const {
    /**
     * 各种配置文件的位置url
     */
    //加工设备配置文件位置 Location of a processing equipment configuration file
    public static final String machineTypeUrl = "machine-type.xml";
    //订单配置文件的位置 Location of a order configuration file
    public static final String orderUrl = "order-test1.xml";
    //画甘特图数据存放位置 Location of Gantt chart data storage
    public static final String dataForGanttUrl = "output/gantt.txt";

    /**
     * 实时决策的时间窗口间隔 Time window interval for real-time decision-making
     */
    public static final int timeWindow = 1;

    /**
     * 工件配送时间，这里认为是固定值，同时假设运力无限
     *  The delivery time of the workpiece is considered to be a fixed value here, and it is assumed that the capacity is unlimited
     */
    public static final int transportTime = 20;

    /**
     * 仓库的配置
     */
    //仓库的出口数量
    public static final int numberOfWarehouseExports = 2;
    //每个仓库出口准备下个货物的等待时间
    public static final int prepareTimeForExports = 9;
    //仓库的入口数量
    public static final int numberOfWarehouseEntrance = 3;
    //每个仓库入口准备下个货物的等待时间
    public static final int prepareTimeForEntranc = 10;

    /**
     * 机器中buffer区状态值
     */
    //无工件
    public static final int nothingInBuffer = 0;
    //待加工
    public static final int WaitingForProcessing = 1;
    //待接收
    public static final int waitingToRecive = 2;
    //正在加工
    public static final int waitingForFrocessingToComplete = 3;
    //加工完，等待下一步分配
    public static final int waitingForNextStep = 4;


    //车间任务限制数量
    public static final int maxPartAmount = 100;

    /**
     * 几率
     */
    //是否采用随机数
    public static final boolean whetherToUseRandom = false;
    //随机数random的种子
    public static final int seed = 0;
    //工艺对应时间改变几率
    public static final int _ptime = 80;
    //增长幅度
    public static final int _growth = 10;

    /**
     *
     * 各种系数
     */
    public static double x_EnlargeForRT = 1.1; //剩余加工时间放大系数

}
