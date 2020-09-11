package org.zhjj370.rule;

import org.zhjj370.basic.AdjustmentCoefficients;
import org.zhjj370.basic.Const;
import org.zhjj370.rule.elemp.ElemWaitingPart;

import java.util.Comparator;

/**
 * 比较规则，加了一个当前机器接收时间
 */

public class CompareWaitingPart1 implements Comparator {
    @Override
    public int compare(Object o1, Object o2 ) {
        //得到各项系数
        AdjustmentCoefficients adjustmentCoefficients = AdjustmentCoefficients.getAdjustmentCoefficients();
        ElemWaitingPart e1 = (ElemWaitingPart) o1;
        ElemWaitingPart e2 = (ElemWaitingPart) o2;
        int returnV = 0;
        double aN =  Const.x_EnlargeForRT; //剩余加工时间放大系数

        int ot1 = e1.getcTime() - e1.getoTime(); //订单下单时间到现在时间的距离
        double dt1 =  e1.getdTime()-(e1.getcTime()+aN*e1.getrTime());
        int at1 = e1.getcTime() - e1.getaTime();//工件到达机器时间到现在时间距离
        int pt1 = e1.getpTime();//工件当前步骤加工时间

        int ot2 = e2.getcTime() - e2.getoTime();
        double dt2 =  e2.getdTime()-(e2.getcTime()+aN*e2.getrTime());
        int at2 = e2.getcTime() - e2.getaTime();//工件到达机器时间到现在时间距离
        int pt2 = e2.getpTime();//工件当前步骤加工时间


        if (dt1 <= 0 && dt2 > 0) {
            returnV = -1;
        } else if (dt2 <= 0 && dt1 > 0) {
            returnV = 1;
        } else if (dt1 > 0 && dt2 > 0) {
            //因素1：工件下单时间
            double ot1To1 = (double) ot1 / Math.max(ot1, ot2);
            double ot2To1 =  (double) ot2 / Math.max(ot1, ot2);
            //因素2：工件交货期
            double dt1To1 =  (double) Math.max(dt1, dt2) / dt1;
            double dt2To1 = (double) Math.max(dt1, dt2) / dt2;
            //因素3：工件到达机器的时间
            double at1To1 =  (double) at1 / Math.max(at1, at2);
            double at2To1 = (double) at2 / Math.max(at1, at2);
            //因素4：工件本道工序加工时间
            double pt1To1 =  (double) Math.max(pt1, pt2) / pt1;
            double pt2To1 = (double) Math.max(pt1, pt2) / pt2;
            //---------------用于比较排列的因数---------------//

            double x1 =  adjustmentCoefficients.getCoefficient_dt() * dt1To1 +
                    adjustmentCoefficients.getCoefficient_pt() * pt1To1 +
                    adjustmentCoefficients.getCoefficient_ot() * ot1To1 +
                    adjustmentCoefficients.getCoefficient_at() * at1To1;
            double x2 =  adjustmentCoefficients.getCoefficient_dt() * dt2To1 +
                    adjustmentCoefficients.getCoefficient_pt() * pt2To1 +
                    adjustmentCoefficients.getCoefficient_ot() * ot2To1 +
                    adjustmentCoefficients.getCoefficient_at() * at2To1;
            if (x1 > x2) {
                returnV = -1;
            } else if (x1 < x2) {
                returnV = 1;
            } else {
                returnV = 0;
            }
        }
        return returnV;
    }

}
