package org.zhjj370.rule;

import org.zhjj370.rule.elemp.ElemPendingTask;

import java.util.Comparator;

public class ComparePendingTask implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        ElemPendingTask e1 = (ElemPendingTask) o1;
        ElemPendingTask e2 = (ElemPendingTask) o2;

        int ot1 = e1.getCurrentT() - e1.getOTime();
        int dt1 = e1.getDTime() - e1.getCurrentT();
        int ot2 = e2.getCurrentT() - e2.getOTime();
        int dt2 = e2.getDTime() - e2.getCurrentT();

        int returnV = 0;
        if (dt1 <= 0 && dt2 > 0) {
            returnV = -1;
        } else if (dt2 <= 0 && dt1 > 0) {
            returnV = 1;
        } else if (dt1 == 0 && dt2 == 0) {
            returnV = 0;
        } else if (dt1 > 0 && dt2 > 0) {
            float ot1To1 = (float) ot1 / Math.max(ot1, ot2);
            float ot2To1 = (float) ot2 / Math.max(ot1, ot2);
            float dt1To1 = (float) Math.max(dt1, dt2) / dt1;
            float dt2To1 = (float) Math.max(dt1, dt2) / dt2;

            float x1 = (float) (0.9 * dt1To1 + 0.1 * ot1To1);
            float x2 = (float) (0.9 * dt2To1 + 0.1 * ot2To1);

            if (x1 > x2) {
                returnV = -1;
            } else if (x1 < x2) {
                returnV = 1;
            } else {
                returnV = 0;
            }
        } else {
            returnV = 0;
        }
        return returnV;
    }
}


