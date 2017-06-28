package com.haocong.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dell-User on 2017/6/13.
 */

public class mem_DBHelper extends SQLiteOpenHelper {

    public mem_DBHelper(Context context){
        super(context, "mem.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String memSQL="CREATE TABLE mem_table (mem_id integer primary key autoincrement, name varchar(10) , phone_num varchar(20),"
                + "phone_type varchar(6), set_or_not varchar(6))";
        db.execSQL(memSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
