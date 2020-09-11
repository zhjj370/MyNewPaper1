package org.zhjj370.functions.gantt;

import org.zhjj370.basic.Const;
import org.zhjj370.connect.sqlite.Dao2020;
import org.zhjj370.functions.element.DataForGantt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class DataProcess {

    public static List<DataForGantt>  dataprocess(){
        Dao2020 dao = Dao2020.getDao2020();
        Vector vectorResult = dao.getValueFromTb();
        List<DataForGantt> dataList = new ArrayList<>();


        Iterator iterator = vectorResult.iterator();
        int iY = 0;
        while (iterator.hasNext()) {
            Vector x = (Vector) iterator.next();
            DataForGantt data1 = new DataForGantt();
            data1.setMachineName(((Integer) x.get(2)) + "-" + x.get(6));//机器名称
            data1.setPartName(x.get(3)+"");
            data1.setStartTme((Integer) x.get(4));
            data1.setEndTime((Integer) x.get(5));
            dataList.add(data1);
        }



        File f = new File(Const.dataForGanttUrl);
        FileWriter fw = null;
        BufferedWriter w = null;
        try {
            fw = new FileWriter(f);
            w = new BufferedWriter(fw);
            Iterator iteratorData =  dataList.iterator();
            while (iteratorData.hasNext()){
                DataForGantt data2 = (DataForGantt) iteratorData.next();
                w.write(data2.getMachineName()+" " +data2.getPartName()+ " " +
                        data2.getStartTme() + " " + data2.getEndTime() + "\n");
            }
            w.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            fw = null;
            w = null;
        }

        //dao.closeConn();
        return dataList;
    }

}
