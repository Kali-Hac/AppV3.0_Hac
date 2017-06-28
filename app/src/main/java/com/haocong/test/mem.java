package com.haocong.test;

/**
 * Created by Dell-User on 2017/6/13.
 */

public class mem {
    private int mem_id;
    private String name;
    private String phone_num;
    private String phone_type;
    private String set_or_not;

    @Override
    public String toString() {
        return "\n编号："+getMem_id()+"\n姓名："+getName()+"\n号码："+getPhone_num()+"\n号码类型："+getPhone_type()+"\n是否为紧急联系人："+getSet_or_not();
    }

    public mem() {
    }

    public int getMem_id() {
        return mem_id;
    }

    public void setMem_id(int mem_id) {
        this.mem_id = mem_id;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getPhone_type() {
        return phone_type;
    }

    public void setPhone_type(String phone_type) {
        this.phone_type = phone_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSet_or_not() {
        return set_or_not;
    }

    public void setSet_or_not(String set_or_not) {
        this.set_or_not = set_or_not;
    }
}
