package com.haocong.test;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell-User on 2017/6/13.
 */

public class mem_DBServer{
    private mem_DBHelper mem_dbHelper;
    public mem_DBServer(Context context){
        this.mem_dbHelper=new mem_DBHelper(context);
    }
    public  void add_mem(mem x){
//        private int mem_id;
//        private String name;
//        private String phone_num;
//        private String phone_type;
//        private String set_or_not;
        Object[] arr=new Object[4];
        SQLiteDatabase localDatabase=this.mem_dbHelper.getWritableDatabase();
        arr[0]=x.getName();
        arr[1]=x.getPhone_num();
        arr[2]=x.getPhone_type();
        arr[3]=x.getSet_or_not();
        localDatabase.execSQL("insert into mem_table (name,phone_num, phone_type, set_or_not) values(?,?,?,?)",arr);
        localDatabase.close();
    }
    public void del_mem(int i){
        SQLiteDatabase localDatabase=this.mem_dbHelper.getWritableDatabase();
        localDatabase.execSQL("delete from mem_table where mem_id=?",new String[]{i+""});
        localDatabase.close();
    }
    public List<mem>get_all_mem(){
        //        private int mem_id;
//        private String name;
//        private String phone_num;
//        private String phone_type;
//        private String set_or_not;
        List<mem>arr=new ArrayList<mem>();
        SQLiteDatabase localDatabase=this.mem_dbHelper.getReadableDatabase();
        Cursor cursor=localDatabase.rawQuery("select * from mem_table where 1=1",null);
        while(cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex("name")).equals("加速度阈值")||cursor.getString(cursor.getColumnIndex("phone_type")).equals("用户信息1")||cursor.getString(cursor.getColumnIndex("phone_type")).equals("用户信息2")||cursor.getString(cursor.getColumnIndex("phone_type")).equals("用户信息3")||cursor.getString(cursor.getColumnIndex("phone_type")).equals("用户信息4")){
                continue;
            }
            mem temp=new mem();
            temp.setMem_id(cursor.getInt(0));
            temp.setName(cursor.getString(cursor.getColumnIndex("name")));
            temp.setPhone_num(cursor.getString(cursor.getColumnIndex("phone_num")));
            temp.setPhone_type(cursor.getString(cursor.getColumnIndex("phone_type")));
            temp.setSet_or_not(cursor.getString(cursor.getColumnIndex("set_or_not")));
            arr.add(temp);
        }
        localDatabase.close();
        return arr;
    }
    public mem FindMemById(int i){
        SQLiteDatabase localDatabase=this.mem_dbHelper.getReadableDatabase();
        Cursor cursor=localDatabase.rawQuery("select * from mem_table where mem_id=?",new String[]{i+""});
        mem temp=new mem();
        cursor.moveToFirst();
        temp.setMem_id(cursor.getInt(0));
        temp.setName(cursor.getString(1));
        temp.setPhone_num(cursor.getString(2));
        temp.setPhone_type(cursor.getString(3));
        temp.setSet_or_not(cursor.getString(4));
        localDatabase.close();
        return temp;
    }
    public List<mem> FindMemByName(String name){
        SQLiteDatabase localDatabase=this.mem_dbHelper.getReadableDatabase();
        Cursor cursor=localDatabase.rawQuery("select * from mem_table where name=?",new String[]{name});
        List<mem>arr=new ArrayList<mem>();
        while(cursor.moveToNext()) {
            mem temp=new mem();
            temp.setMem_id(cursor.getInt(0));
            temp.setName(cursor.getString(1));
            temp.setPhone_num(cursor.getString(2));
            temp.setPhone_type(cursor.getString(3));
            temp.setSet_or_not(cursor.getString(4));
            arr.add(temp);
        }
        localDatabase.close();
        return arr;
    }
    public List<mem> FindMemByPhone_num(String phone_num){
        SQLiteDatabase localDatabase=this.mem_dbHelper.getReadableDatabase();
        Cursor cursor=localDatabase.rawQuery("select * from mem_table where phone_num=?",new String[]{phone_num});
        List<mem>arr=new ArrayList<mem>();
        while(cursor.moveToNext()) {
            mem temp=new mem();
            temp.setMem_id(cursor.getInt(0));
            temp.setName(cursor.getString(1));
            temp.setPhone_num(cursor.getString(2));
            temp.setPhone_type(cursor.getString(3));
            temp.setSet_or_not(cursor.getString(4));
            arr.add(temp);
        }
        localDatabase.close();
        return arr;
    }
    public List<mem> FindMemByPhone_type(String phone_type){
        SQLiteDatabase localDatabase=this.mem_dbHelper.getReadableDatabase();
        Cursor cursor=localDatabase.rawQuery("select * from mem_table where phone_type=?",new String[]{phone_type});
        List<mem>arr=new ArrayList<mem>();
        while(cursor.moveToNext()) {
            mem temp=new mem();
            temp.setMem_id(cursor.getInt(0));
            temp.setName(cursor.getString(1));
            temp.setPhone_num(cursor.getString(2));
            temp.setPhone_type(cursor.getString(3));
            temp.setSet_or_not(cursor.getString(4));
            arr.add(temp);
        }
        localDatabase.close();
        return arr;
    }
    public List<mem> FindMemBySet_or_not(String set_or_not){
        SQLiteDatabase localDatabase=this.mem_dbHelper.getReadableDatabase();
        Cursor cursor=localDatabase.rawQuery("select * from mem_table where set_or_not=?",new String[]{set_or_not});
        List<mem>arr=new ArrayList<mem>();
        while(cursor.moveToNext()) {
            mem temp=new mem();
            temp.setMem_id(cursor.getInt(0));
            temp.setName(cursor.getString(1));
            temp.setPhone_num(cursor.getString(2));
            temp.setPhone_type(cursor.getString(3));
            temp.setSet_or_not(cursor.getString(4));
            arr.add(temp);
        }
        localDatabase.close();
        return arr;
    }
    public void modify_mem(mem x){
        Object[] arr=new Object[5];
        SQLiteDatabase localDatabase=this.mem_dbHelper.getWritableDatabase();
        arr[0]=x.getName();
        arr[1]=x.getPhone_num();
        arr[2]=x.getPhone_type();
        arr[3]=x.getSet_or_not();
        arr[4]=x.getMem_id()+"";
        try {
            localDatabase.execSQL("update mem_table set name=?,phone_num=?, phone_type=?, set_or_not=? where mem_id=?", arr);
        }
        catch (Exception e){
            add_mem(x);
        }
        localDatabase.close();
    }
    public int count_sos_mem(){
        SQLiteDatabase LocalDatabase=mem_dbHelper.getReadableDatabase();
        int Count = 0;
        Cursor cursor = LocalDatabase.rawQuery("select count(*) from mem_table where set_or_not ='是' " , null);
        cursor.moveToFirst();
        Count=cursor.getInt(0);
        LocalDatabase.close();
        return Count;
    }
    public int count_mem(){
        SQLiteDatabase LocalDatabase=mem_dbHelper.getReadableDatabase();
        int Count = 0;
        Cursor cursor = LocalDatabase.rawQuery("select count(*) from mem_table where name is not '加速度阈值' and  phone_type is not '用户信息1' " +
                " and  phone_type is not '用户信息2'  and  phone_type is not '用户信息3' and  phone_type is not '用户信息4'" , null);
        cursor.moveToFirst();
        Count=cursor.getInt(0);
        LocalDatabase.close();
        return Count;
    }
    public List<String> get_sos_mems(){
        SQLiteDatabase LocalDatabase=mem_dbHelper.getReadableDatabase();
        List<String>arr=new ArrayList<String>();
        Cursor cursor = LocalDatabase.rawQuery("select name from mem_table where set_or_not is  '是' " , null);
        while(cursor.moveToNext()) {
            arr.add(cursor.getString(0));
        }
        LocalDatabase.close();
        return arr;
    }
    public void del_a(){
        SQLiteDatabase localDatabase=this.mem_dbHelper.getWritableDatabase();
        localDatabase.execSQL("delete from mem_table where name is '加速度阈值'");
        localDatabase.close();
    }
    public void del_user_info(){
        SQLiteDatabase localDatabase=this.mem_dbHelper.getWritableDatabase();
        try {
            localDatabase.execSQL("delete from mem_table where phone_type is '用户信息1'");
            localDatabase.execSQL("delete from mem_table where phone_type is '用户信息2'");
            localDatabase.execSQL("delete from mem_table where phone_type is '用户信息3'");
            localDatabase.execSQL("delete from mem_table where phone_type is '用户信息4'");
        }
        catch (Exception e){

        }
        localDatabase.close();
    }
    public String[] get_user_info(){
        String []arr=new String[7];
        List<mem>mem_arr=new ArrayList<mem>();
        mem_arr=FindMemByPhone_type("用户信息1");
        arr[0]=mem_arr.get(0).getName();
        arr[1]=mem_arr.get(0).getPhone_num();
        mem_arr=FindMemByPhone_type("用户信息2");
        arr[2]=mem_arr.get(0).getName();
        arr[3]=mem_arr.get(0).getPhone_num();
        mem_arr=FindMemByPhone_type("用户信息3");
        arr[4]=mem_arr.get(0).getName();
        arr[5]=mem_arr.get(0).getPhone_num();
        mem_arr=FindMemByPhone_type("用户信息4");
        arr[6]=mem_arr.get(0).getPhone_num();
        return arr;
    }
}
