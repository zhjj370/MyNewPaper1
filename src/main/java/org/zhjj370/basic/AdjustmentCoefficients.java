package org.zhjj370.basic;


/**
 * 存放调整系数
 *  Storage adjustment factor
 * @author Zequn ZHANG
 */

public class AdjustmentCoefficients {
    private static AdjustmentCoefficients adjustmentCoefficients = new AdjustmentCoefficients();

    //-------“机器选工件”中各项调整系数-----------//
    //Various adjustment coefficients in "machines select parts"//
    private double coefficient_ot = 0.2;
    private double coefficient_dt = 0.5;
    private double coefficient_at = 0.2;
    private double coefficient_pt = 0.0;

    //-----“工件选机器”中各项调整系数------- //
    //Various adjustment coefficients in "parts select machines"//
    private double coefficient_load = 0.2;
    private double coefficient_busyness = 0.2;
    private double coefficient_energy = 0.6;



    /**
     * 获取唯一可用对象
     */
    public static AdjustmentCoefficients getAdjustmentCoefficients() {
        return adjustmentCoefficients;
    }

    //-------机器选工件调整系数操作方法-----------//
    public double getCoefficient_at() {
        return coefficient_at;
    }

    public double getCoefficient_dt() {
        return coefficient_dt;
    }

    public double getCoefficient_ot() {
        return coefficient_ot;
    }

    public double getCoefficient_pt() {
        return coefficient_pt;
    }

    public void setCoefficient_at(double coefficient_at) {
        this.coefficient_at = coefficient_at;
    }

    public void setCoefficient_dt(double coefficient_dt) {
        this.coefficient_dt = coefficient_dt;
    }

    public void setCoefficient_ot(double coefficient_ot) {
        this.coefficient_ot = coefficient_ot;
    }

    public void setCoefficient_pt(double coefficient_pt) {
        this.coefficient_pt = coefficient_pt;
    }

    //-------工件选机器调整系数操作方法-----------//
    public double getCoefficient_busyness() {
        return coefficient_busyness;
    }

    public double getCoefficient_energy() {
        return coefficient_energy;
    }

    public double getCoefficient_load() {
        return coefficient_load;
    }

    public void setCoefficient_busyness(double coefficient_busyness) {
        this.coefficient_busyness = coefficient_busyness;
    }

    public void setCoefficient_energy(double coefficient_energy) {
        this.coefficient_energy = coefficient_energy;
    }

    public void setCoefficient_load(double coefficient_load) {
        this.coefficient_load = coefficient_load;
    }
}
