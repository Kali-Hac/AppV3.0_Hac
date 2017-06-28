package com.haocong.test;
import android.util.Log;
/**
 * Created by Dell-User on 2017/6/6.
 */

public class Class
{
    private String classId;
    private String className;

    public String getClassId() {
        return classId;
    }
    public void setClassId(String classId) {
        this.classId = classId;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String toString() {
        return "Class--->"+"classId:"+classId+"  className:"+className;
    }
}