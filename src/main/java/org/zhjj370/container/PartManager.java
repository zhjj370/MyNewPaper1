package org.zhjj370.container;


import org.zhjj370.basic.Const;
import org.zhjj370.basic.Part;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 */
public class PartManager {
    /**
     * 创建一个PartManager对象
     */
    private static PartManager partManager;
    static {
        try {
            partManager = new PartManager();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 工件任务列表
     */
    public Vector<Part> partVector = new Vector<Part>();


    /**
     * 实例化
     */
    private PartManager() throws DocumentException {
        SAXReader reader = new SAXReader();
        //[2] 读取xml文件
        InputStream INPUTSTREAM = this.getClass().getResourceAsStream(Const.orderUrl);
        Document docMachine = reader.read(INPUTSTREAM);
        //[3] 获取根元素
        Element root = docMachine.getRootElement();
        //[4] 获取根元素下的子元素
        Iterator<Element> it = root.elementIterator();
        //记录工件号
        int taskID = 0;
        while (it.hasNext()) {
            //取出元素
            Element e = (Element) it.next();
            //获取id属性
            Attribute e_id = e.attribute("id");
            //获取子元素
            Element e_orderTime = e.element("orderTime");
            Element e_deliveryData = e.element("deliveryData");
            //获取需要的值
            int orderTime = Integer.parseInt(e_orderTime.getStringValue());
            int delieryData = Integer.parseInt(e_deliveryData.getStringValue());
            Element e_content = e.element("directory");
            Iterator<Element> it_content = e_content.elementIterator();
            while (it_content.hasNext()) {
                Element ee = (Element) it_content.next();
                Element ee_partTypeID = ee.element("partTypeID");
                Element ee_partType = ee.element("partType");
                Element ee_processing = ee.element("processing");
                Element ee_ptime = ee.element("time");
                Element ee_amount = ee.element("amount");
                String partTypeID = ee_partTypeID.getStringValue();
                String partType = ee_partType.getStringValue();
                String processing = ee_processing.getStringValue();
                String time = ee_ptime.getStringValue();
                int amount = Integer.parseInt(ee_amount.getStringValue());
                for (int i = 0; i < amount; i = i + 1) {
                    partVector.add(new Part(partTypeID, partType, processing, time, orderTime, delieryData, taskID));
                    taskID += 1 ;
                }
            }
        }
        System.out.println("任务数量："+ partVector.size());
        //打印工件列表
        //printlnPart();

    }

    /**
     * 获取唯一可用对象
     * @return
     */
    public static PartManager getPartManager() {
        return partManager;
    }

    /**
     * 查询所有工件是否都加工完
     */
    public boolean isAllFinished(){
        int i = 0;
        boolean isAllFinished = false;
        Iterator<Part> iterSearchPart = partVector.iterator();
        while (iterSearchPart.hasNext()) {
            if (iterSearchPart.next().getIsFinished() == false) {
                isAllFinished = true;
                break;
            }
        }
        return isAllFinished;
    }

    /**
     * 检查还有哪个没有加工完
     */
    public void checkALL(){
        int i = 0;
        Iterator<Part> iterSearchPart = partVector.iterator();
        while (iterSearchPart.hasNext()) {
            Part s = iterSearchPart.next();
            if (s.getIsFinished() == false) {
                System.out.println(s.getTaskID());
            }
        }
    }

    /**
     * 打印Part
     */
    public void printlnPart(){
        Iterator<Part> iter = partVector.iterator();
        while (iter.hasNext()) {
            Part s = (Part) iter.next();
            String[] processing = s.getProcessing();
            int[] time = s.getTime();

            for(String step: processing){
                System.out.print("   " + step);
            }
            for(int stepTime:time){
                System.out.print("   " + stepTime);
            }
            System.out.println("   "  + s.getPartTypeName());
            System.out.println("   "  + s.getTaskID());
        }
    }

    /**
     * 计算交货期临近指数
     */
    public double getApproachingIndexForDt(int currentT){
        double indexA = 0.0;
        double minDT = 0.0; //记录最近的交货期剩余时间
        //double testTime = 0.0;//测试用，剩余交货期时间最小值记录
        //minDT = partVector.get(0).getRemainingProcessingTime();
        int i = 0;
        Iterator<Part> iter = partVector.iterator();
        while (iter.hasNext()) {
            Part s = (Part) iter.next();
            //省点时间
            if(s.getOrderTime()>currentT) break;
            if(s.getIsFinished() == false) {
                int rt = s.getRemainingProcessingTime();
                int dt = s.getDeliveryData();
                //double surplusTime = (dt - (currentT + rt * Const.x_EnlargeForRT))/10;   //预估的富余时间
                double surplusTime = (dt - (currentT + rt * Const.x_EnlargeForRT));
                //
                //实验
                //
                if(rt>0) surplusTime = surplusTime/rt;
                //surplusTime = Math.pow(surplusTime,1d/3);
                if (i == 0) {
                    minDT = surplusTime;
                }
                i++;
                if (minDT > surplusTime) {
                    minDT = surplusTime;
                }
            }
        }
        if (minDT<=0){
            indexA =0;
        }
        else{
            //indexA =  Math.pow(minDT,1d/3);
            indexA = minDT;
        }
        indexA = indexA/(1+indexA);
        return indexA;
        //return minDT;
    }


}
