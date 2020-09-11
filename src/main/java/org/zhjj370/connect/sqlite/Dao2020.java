package org.zhjj370.connect.sqlite;

import java.util.*;


/**
 * 仿真程序中使用的数据库操作类
 * Database operation classes used in the simulation program
 * @author Zequn Zhang
 */
public class Dao2020 extends BaseDaoSQLite {
    private static Dao2020 dao;

    static {
        dao = new Dao2020();
    }

    /**
     * 获取唯一可用对象
     * @return
     */
    public static Dao2020 getDao2020() {
        return dao;
    }

    /**
     * 向表格table_for_real_time_decision中插入一行数据
     *
     */
    public boolean insertTableForRecodring(int keyID,int machineID,int partID,int startTime,int endTime,String machineName){
        boolean test = longHaul("insert into table_for_real_time_decision (ID,machineID,partID,startTime,endTime,machineName)" +
                "values('" + keyID  +  "','"  + machineID + "','" + partID + "','" +
                startTime + "','" + endTime + "','" + machineName + "')");
        return test;
    }

    /**
     * 删除table_for_real_time_decision中所有数据
     */
    public boolean deleteTableForRecodring(){
        boolean test = longHaul("delete from table_for_real_time_decision");
        return test;
    }

    /**
     * 工件结束加工，将endtime插入对应位置
     */
    public boolean insertEndTime3(int machineID,int partID,int endTime){
        boolean test = longHaul("update table_for_real_time_decision set endTime='" + endTime +
                "' where machineID=" + machineID +
                " and partID=" + partID +
                " and endtime=" + 0);
        return test;
    }

    /**
     * 获取table_for_real_time_decision中的数据
     */
    public Vector getValueFromTb(){
        return selectSomeNote("select * from table_for_real_time_decision");
    }


    /**
     * 创建表格
     */
    public void createTable(){
        List<String> tableNames = getTbname();
        Iterator<String> iterTableNames = tableNames.iterator();
        boolean isTableExist = true;
        while (iterTableNames.hasNext()){
            if(iterTableNames.next().equals("table_for_real_time_decision")) {
                isTableExist = false;
                break;
            }
        }
        if(isTableExist){
            makenewtb("CREATE TABLE table_for_real_time_decision " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " machineID      INT     NOT NULL, " +
                    " partID         INT     NOT NULL, " +
                    " startTime      INT, " +
                    " endTime        INT, " +
                    " machineNAME    CHAR(50)) ");
        }
    }

    /**
     * 断开数据库连接
     */
    public boolean closeConn(){
        return closedJDBC();
    }

}
