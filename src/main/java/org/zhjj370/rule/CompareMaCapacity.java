package org.zhjj370.rule;

import java.util.Comparator;

public class CompareMaCapacity  implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        int[] e1 = (int[]) o1;
        int[] e2 = (int[]) o2;

        int returnV = 0;
        //从小到大排列
        if(e1[1]>e2[1]){
            returnV = 1;
        }
        else if(e1[1]<e2[1]){
            returnV = -1;
        }
        else{
            //TODO：变为一个随机事件
            return 0;
        }
        return returnV;
    }
}
