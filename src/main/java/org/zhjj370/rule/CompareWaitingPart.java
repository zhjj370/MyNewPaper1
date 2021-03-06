package org.zhjj370.rule;

import org.zhjj370.basic.Const;
import org.zhjj370.rule.elemp.ElemWaitingPart;

import java.util.Comparator;

public class CompareWaitingPart implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        ElemWaitingPart e1 = (ElemWaitingPart) o1;
        ElemWaitingPart e2 = (ElemWaitingPart) o2;
        int returnV = 0;
        float aN = (float) Const.x_EnlargeForRT; //剩余加工时间放大系数

        int ot1 = e1.getcTime() - e1.getoTime();
        float dt1 = (float) e1.getdTime()-(e1.getcTime()+aN*e1.getrTime());
        int ot2 = e2.getcTime() - e2.getoTime();
        float dt2 = (float) e2.getdTime()-(e2.getcTime()+aN*e2.getrTime());

        if (dt1 <= 0 && dt2 > 0) {
            returnV = -1;
        } else if (dt2 <= 0 && dt1 > 0) {
            returnV = 1;
        } else if (dt1 > 0 && dt2 > 0) {
            float ot1To1 = (float) ot1 / Math.max(ot1, ot2);
            float ot2To1 = (float) ot2 / Math.max(ot1, ot2);
            float dt1To1 = (float) Math.max(dt1, dt2) / dt1;
            float dt2To1 = (float) Math.max(dt1, dt2) / dt2;

            float x1 = (float) (0.8 * dt1To1 + 0.2 * ot1To1);
            float x2 = (float) (0.8 * dt2To1 + 0.2 * ot2To1);

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
