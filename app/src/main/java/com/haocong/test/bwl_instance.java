package com.haocong.test;

/**
 * Created by Dell-User on 2017/6/12.
 */

public class bwl_instance  {
    //有一个主键info_id是自动增长获取的
    private  int info_id;
    private  String info;
    private String rem_time;
    private String importance;
    private String time;
    private String type;


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getRem_time() {
        return rem_time;
    }

    public void setRem_time(String rem_time) {
        this.rem_time = rem_time;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "\n编号："+getInfo_id()+"\n备忘类型："+getType()+"\n备忘信息："+getInfo()+"\n备忘时间："+getTime()+"\n重要程度："+getImportance()+"\n提醒间隔时间："+getRem_time();
    }


    public int getInfo_id() {
        return info_id;
    }

    public void setInfo_id(int info_id) {
        this.info_id = info_id;
    }
}
