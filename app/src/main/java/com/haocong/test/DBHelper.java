package com.haocong.test;
import android.content.Context;
import  android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Dell-User on 2017/6/6.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "info.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String classesSQL = "CREATE TABLE classes(class_id varchar(10) primary key , " +
                "class_name varchar(20))";

        String studentsSQL = "CREATE TABLE students(student_id varchar(10) primary key , " +
                "student_name varchar(20) ,score varchar(4) ,class_id varchar(10), " +
                "foreign key (class_id) references classes(class_id) " +
                "on delete cascade on update cascade )";
        db.execSQL(classesSQL);
        Log.d("my", "create table classes:"+classesSQL);
        db.execSQL(studentsSQL);
        Log.d("my", "create table students:"+studentsSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //更新数据库操作(新的版本特性)
    }
}
