package com.haocong.test;
import java.util.List;
import java.util.ArrayList;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.Context;
import android.util.Log;
/**
 * Created by Dell-User on 2017/6/11.
 */

public class DB_all_Server {
    private DB_all_Helper db_all_helper;

    public DB_all_Server(Context context) {
        this.db_all_helper = new DB_all_Helper(context);
    }

    public void add_bwl(bwl_instance bwl) {
//        private String info_id;
//        private  String info;
//        private String rem_time;
//        private String importance;
//        private String time;
//        private String type;
        Object[] arr = new Object[5];
        arr[0] = bwl.getInfo();
        arr[1] = bwl.getRem_time();
        arr[2] = bwl.getImportance();
        arr[3] = bwl.getTime();
        arr[4] = bwl.getType();
        SQLiteDatabase localDatebase = this.db_all_helper.getWritableDatabase();
        localDatebase.execSQL("insert into Bw_info_table(info_id, info,rem_time,importance,time,type) values(null,?,?,?,?,?)", arr);
        localDatebase.close();
    }

    public void delete_bwl_by_id(int id) {
//        private String info_id;
//        private  String info;
//        private String rem_time;
//        private String importance;
//        private String time;
//        private String type;
        SQLiteDatabase localDatebase = this.db_all_helper.getWritableDatabase();
        localDatebase.execSQL("delete from Bw_info_table where info_id =?", new String[]{"" + id});
        localDatebase.close();
    }

    public bwl_instance find_bwl_by_id(int id) {
//        private String info_id;
//        private  String info;
//        private String rem_time;
//        private String importance;
//        private String time;
//        private String type;
        SQLiteDatabase localDatabase = this.db_all_helper.getReadableDatabase();
        Cursor cursor = localDatabase.rawQuery("select * from Bw_info_table where info_id=?", new String[]{"" + id});
        cursor.moveToFirst();
        bwl_instance temp = new bwl_instance();
        temp.setInfo_id(cursor.getInt(0));
        temp.setInfo(cursor.getString(cursor.getColumnIndex("info")));
        temp.setRem_time(cursor.getString(cursor.getColumnIndex("rem_time")));
        temp.setImportance(cursor.getString(cursor.getColumnIndex("importance")));
        temp.setTime(cursor.getString(cursor.getColumnIndex("time")));
        temp.setType(cursor.getString(cursor.getColumnIndex("type")));
        localDatabase.close();
        return temp;
    }

    public List<bwl_instance> get_bwl() {
        List<bwl_instance> arr = new ArrayList<bwl_instance>();
        SQLiteDatabase locatDatabase = this.db_all_helper.getReadableDatabase();
        Cursor cursor = locatDatabase.rawQuery("select * from Bw_info_table where 1=1  ", null);
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("type")).equals("定位记录") || cursor.getString(cursor.getColumnIndex("type")).equals("个人定位")) {
                continue;
            }
            bwl_instance temp = new bwl_instance();
            temp.setInfo_id(cursor.getInt(cursor.getColumnIndex("info_id")));
            temp.setInfo(cursor.getString(cursor.getColumnIndex("info")));
            temp.setRem_time(cursor.getString(cursor.getColumnIndex("rem_time")));
            temp.setImportance(cursor.getString(cursor.getColumnIndex("importance")));
            temp.setTime(cursor.getString(cursor.getColumnIndex("time")));
            temp.setType(cursor.getString(cursor.getColumnIndex("type")));
            arr.add(temp);
        }
        locatDatabase.close();
        return arr;
    }

    public List<bwl_instance> find_bwl_by_type(String type) {
//        private String info_id;
//        private  String info;
//        private String rem_time;
//        private String importance;
//        private String time;
//        private String type;
        List<bwl_instance> arr = new ArrayList<bwl_instance>();
        SQLiteDatabase localDatabase = this.db_all_helper.getReadableDatabase();
        Cursor cursor = localDatabase.rawQuery("select * from Bw_info_table where type=?", new String[]{type});
        while (cursor.moveToNext()) {
            bwl_instance temp = new bwl_instance();
            temp.setInfo_id(cursor.getInt(cursor.getColumnIndex("info_id")));
            temp.setInfo(cursor.getString(cursor.getColumnIndex("info")));
            temp.setRem_time(cursor.getString(cursor.getColumnIndex("rem_time")));
            temp.setImportance(cursor.getString(cursor.getColumnIndex("importance")));
            temp.setTime(cursor.getString(cursor.getColumnIndex("time")));
            temp.setType(cursor.getString(cursor.getColumnIndex("type")));
            arr.add(temp);
        }
        localDatabase.close();
        return arr;
    }

    public int count_bwl() {
        SQLiteDatabase LocalDatabase = db_all_helper.getReadableDatabase();
        int Count = 0;
        Cursor cursor = LocalDatabase.rawQuery("select count(*) from Bw_info_table where type is not '定位记录' and  type is not '个人定位' ", null);
        cursor.moveToFirst();
        Count = cursor.getInt(0);
        LocalDatabase.close();
        return Count;
    }

    public List<String> get_personal_address() {
        List<String> arr = new ArrayList<String>();
        SQLiteDatabase localDatabase = this.db_all_helper.getReadableDatabase();
        Cursor cursor = localDatabase.rawQuery("select info from Bw_info_table where type is '个人定位' ", null);
        while (cursor.moveToNext()) {
            arr.add(cursor.getString(0));
        }
        return arr;
    }

    public void delete_personalAddr() {
//        private String info_id;
//        private  String info;
//        private String rem_time;
//        private String importance;
//        private String time;
//        private String type;
        SQLiteDatabase localDatebase = this.db_all_helper.getWritableDatabase();
        localDatebase.execSQL("delete from Bw_info_table where type is '个人定位'");
        localDatebase.execSQL("delete from Bw_info_table where type is '定位记录'");
        localDatebase.close();
    }
}
