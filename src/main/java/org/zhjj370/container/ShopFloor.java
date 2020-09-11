package org.zhjj370.container;


import org.zhjj370.basic.Const;
import org.zhjj370.basic.Machine;
import org.zhjj370.container.element.DataForBusyness;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * 根据读取到的MXL信息
 */
public class ShopFloor {

    /**
     * 创建一个ShopFloor对象
     */
    private static ShopFloor shopFloor;

    static {
        try {
            shopFloor = new ShopFloor();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 管理车间层设备的列表
     */
    public List<Machine> machineList = new ArrayList<Machine>();

    /**
     * 记录车间层设备实时负载（即繁忙度）
     */
    private List<DataForBusyness> dataForBusynessList = new ArrayList<>();
    /**
     * 记录车间当前最大负载
     */
    private List<Double> dataForMaxBusynessList = new ArrayList<>();

    /**
     * 属性值
     */
    //哈希表，有多少类型的机床及其所属有多少buffer,
    // int[0]=所属机床类型buffer数量，int[1]=所属机床类型还有多少buffer没用
    HashMap<String, int[]> bufferUsageSituationMap = new HashMap<String, int[]>();
    //空位数量表
    //HashMap<String, Integer> machineAndBufferUsageSituationMap = new HashMap<String, Integer>();
    //

    /**
     * 实例化
     */
    private ShopFloor() throws DocumentException {//[1] 创建SAXReader，用于读取xml对象
        SAXReader reader = new SAXReader();
        //[2] 读取xml文件
        InputStream INPUTSTREAM = this.getClass().getResourceAsStream(Const.machineTypeUrl);
        Document docMachine = reader.read(INPUTSTREAM);
        //[3] 获取根元素
        Element root = docMachine.getRootElement();
        //[4] 获取根元素下的子元素
        Iterator<Element> it = root.elementIterator();
        int IDforM = 0;
        while (it.hasNext()) {
            //取出元素
            Element e = (Element) it.next();
            //获取id属性
            Attribute e_id = e.attribute("id");
            //获取子元素
            Element e_machineName = e.element("machineName");
            Element e_bufferAmount = e.element("bufferAmount");
            Element e_amount = e.element("amount");
            Element e_energyConsumption = e.element("energyConsumption"); //耗能
            Element e_life = e.element("life"); //当前寿命
            //获取需要的值
            String machineTypeID = e_id.getValue();
            String machineName = e_machineName.getStringValue();
            int bufferAmount = Integer.parseInt(e_bufferAmount.getStringValue());
            int amount = Integer.parseInt(e_amount.getStringValue());

            Vector<String> ability = new Vector<>();
            List<Element> listE = e.elements();
            for (Element c : listE) {
                if (c.getName() == "ability") {
                    ability.add(c.getStringValue());
                }
            }

            //机器及其对应buffer数量的哈希表
            int[] bufferRecording = {bufferAmount * amount, 0};
            bufferUsageSituationMap.put(machineName, bufferRecording);

            double energyConsumption = Double.parseDouble(e_energyConsumption.getStringValue());
            int life = Integer.parseInt(e_life.getStringValue());
            //machineAndBufferUsageSituationMap.put(machineName,0);
            //创建该机器
            for (int i = 0; i < amount; i = i + 1) {
                machineList.add(new Machine(machineTypeID, machineName, bufferAmount, ability, IDforM,energyConsumption,life));//机器列表创建
                dataForBusynessList.add(new DataForBusyness(IDforM,machineName));//机器繁忙度记录列表创建
                IDforM++;
            }
            //

        }
    }

    /**
     * 获取唯一可用对象
     */
    public static ShopFloor getShopFloor() {
        return shopFloor;
    }

    public List<DataForBusyness> getDataForBusynessList(){
        return dataForBusynessList;
    }

    public List<Double> getDataForMaxBusynessList() {
        return dataForMaxBusynessList;
    }

    /**
     * buffer使用情况重置
     */
    public void resetBufferUsageSituationMap() {
        //遍历machineAndBufferAmountMap的key值
        Iterator iter = bufferUsageSituationMap.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            int[] a = {bufferUsageSituationMap.get(key)[0], 0};
            bufferUsageSituationMap.put(key, a);
        }
    }

    /**
     * 更新buffer使用情况
     *
     * @param keyMachineName
     * @param valueAmount
     */
    public void unusedBufferNumberRecord(String keyMachineName, int valueAmount) {
        int[] a = {bufferUsageSituationMap.get(keyMachineName)[0], bufferUsageSituationMap.get(keyMachineName)[0] + valueAmount};
        bufferUsageSituationMap.put(keyMachineName, a);
    }

    /**
     * 暂用函数，用于判断车间层是否还能下单
     * 判断标准：每种类型机床的使用数量
     */
    public boolean WhetherToAllowTheTaskToBeReleased() {
        boolean allow = true;

        Iterator iter = bufferUsageSituationMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int[] value = (int[]) entry.getValue();
            int judgmentValue = value[1] / value[0];
            if (judgmentValue < 0.2) {
                allow = false;
                break;
            }
        }
        return allow;
    }



    /**
     * 打印在buffer中的工件号
     */
    public void printlnPartInBuffer() {
        Iterator<Machine> iterMachine = machineList.iterator();
        while (iterMachine.hasNext()) {
            Machine imachine = iterMachine.next();
            List<Integer> getPartIDInMachine = imachine.getPartIdInBuffer();
            if (getPartIDInMachine.size() > 0) {
                Iterator<Integer> iPart = getPartIDInMachine.iterator();
                while (iPart.hasNext()) {
                    System.out.println("机器号：" + imachine.getID() + ";     在buffer中的工件号：" + iPart.next());
                }
            }
        }
    }

    /**
     * 刷新当前机器负载，繁忙度，工位使用情况
     */
    public void refreshBusyness(){
        PartManager partManager = PartManager.getPartManager();
        Iterator<DataForBusyness> iterDataForBusynessList = dataForBusynessList.listIterator();
        int iMachineID = 0;
        int maxBusiness = 0;
        while (iterDataForBusynessList.hasNext()){
            int load = 0;
            double busyness = 0.0;
            //获取机器中的工件的id号
            List<Integer> idList =  machineList.get(iMachineID).getIdInBuffer();
            //获取每个工件当前负载
            Iterator<Integer> iterIdList = idList.iterator();
            while (iterIdList.hasNext()){
                load += partManager.partVector.get(iterIdList.next()).getLoad();
            }
            //TODO：错误的方法
            /*if(idList.size()<=1){
                busyness = 0;
            } else{
                busyness = load/(idList.size()-1);
            }*/
            double utilizationOfBuffer = 1-machineList.get(iMachineID).getIdleRateOfMachine();
            if(load>maxBusiness) maxBusiness = load; //记录最大负载（繁忙度）
            if(load>10) busyness = (1-utilizationOfBuffer)/(0.01*load);
            else busyness = (1-utilizationOfBuffer)/0.1;

            //更新数据
            DataForBusyness dataForBusyness0 = iterDataForBusynessList.next();
            dataForBusyness0.getDataList().add((double) load);//添加负载
            dataForBusyness0.getStationUsage().add(idList.size());//添加目前使用的工位数量
            dataForBusyness0.getBusynessList().add(busyness/(1+busyness));//添加繁忙度(这里用工位利用率替代)

            iMachineID++;
        }
        dataForMaxBusynessList.add((double) maxBusiness);
    }

}
