package org.zhjj370.rule;

import org.zhjj370.basic.AdjustmentCoefficients;
import org.zhjj370.rule.elemp.ElemMaCapacity;

import java.util.Comparator;

/**
 * 工件选设备规则-1，对比规则0，加入空闲工位
 */
public class CompareMaCapacity1 implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        ElemMaCapacity e1 = (ElemMaCapacity) o1;
        ElemMaCapacity e2 = (ElemMaCapacity) o2;
        //得到各项系数
        AdjustmentCoefficients adjustmentCoefficients = AdjustmentCoefficients.getAdjustmentCoefficients();
        //-----------获取比较数据-------------//
        double load_e1 = (double) e1.getLoad();
        double buysness_e1 = e1.getBusyness();
        double energy_e1 = e1.getEnergyConsumption();

        double load_e2 = (double) e2.getLoad();
        double buysness_e2 = e2.getBusyness();
        double energy_e2 = e2.getEnergyConsumption();

        //----------归一化处理---------------//
        double load_e1_Normalized=0;
        if(load_e1 == 0 && load_e2 == 0){
            load_e1_Normalized=0;
        }
        else {
            load_e1_Normalized = load_e1 / Math.max(load_e1, load_e2);
        }
        double busyness_e1_Normalized = 0;
        if(buysness_e1 == 0 && buysness_e2 == 0){
            busyness_e1_Normalized=0;
        }
        else {
            busyness_e1_Normalized = buysness_e1 / Math.max(buysness_e1, buysness_e2);
        }
        double energy_e1_Normalized = energy_e1/ Math.max(energy_e1, energy_e2);

        double load_e2_Normalized=0;
        if(load_e1 == 0 && load_e2 == 0){
            load_e2_Normalized=0;
        }
        else {
            load_e2_Normalized = load_e2 / Math.max(load_e1, load_e2);
        }
        double busyness_e2_Normalized = 0;
        if(buysness_e1 == 0 && buysness_e2 == 0){
            busyness_e2_Normalized=0;
        }
        else {
            busyness_e2_Normalized = buysness_e2 / Math.max(buysness_e1, buysness_e2);
        }
        double energy_e2_Normalized = energy_e2/ Math.max(energy_e1, energy_e2);


        int returnV = 0;
        //----------判断条件------------//
        double x1 =  adjustmentCoefficients.getCoefficient_load()*load_e1_Normalized+
                adjustmentCoefficients.getCoefficient_busyness()*busyness_e1_Normalized+
                adjustmentCoefficients.getCoefficient_energy()*energy_e1_Normalized;
        double x2 =  adjustmentCoefficients.getCoefficient_load()*load_e2_Normalized+
                adjustmentCoefficients.getCoefficient_busyness()*busyness_e2_Normalized+
                adjustmentCoefficients.getCoefficient_energy()*energy_e2_Normalized;

        if(x1 > x2){
            returnV = 1;
        }
        else if(x1 < x2){
            returnV = -1;
        }
        else{
            return 0;
        }
        return returnV;
    }
}
