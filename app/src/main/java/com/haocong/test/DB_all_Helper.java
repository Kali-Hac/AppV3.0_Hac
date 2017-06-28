package com.haocong.test;

import android.app.Activity;
import android.content.Context;
import  android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Dell-User on 2017/6/11.
 */

public class DB_all_Helper extends SQLiteOpenHelper  {
    public DB_all_Helper(Context context) {
        super(context, "bwl_info.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String bwlSQL =  "CREATE TABLE Bw_info_table  (info_id INTEGER PRIMARY KEY AUTOINCREMENT , info varchar(10)  , " +
                "rem_time varchar(20) ,importance varchar(10) ,time varchar(10), " +
                "type varchar(10))";
        db.execSQL(bwlSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //更新数据库操作(新的版本特性)
    }
}
