package com.haocong.test;
import java.util.List;
import java.util.ArrayList;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.Context;
import android.util.Log;
/**
 * Created by Dell-User on 2017/6/6.
 */

public class DBServer {
    private DBHelper dbhelper;
    public DBServer(Context context){
        this.dbhelper=new DBHelper(context);
    }
    public void addClass(Class x){
        SQLiteDatabase localSQLiteDatabase= this.dbhelper.getWritableDatabase();
        Object[]new_class=new Object[2];
        new_class[0]=x.getClassId();
        new_class[1]=x.getClassName();
        try {
            localSQLiteDatabase.execSQL("insert into classes(class_id,class_name) values(?,?)", new_class);
        }
        catch (Exception e){
            Log.e("error","添加班级失败");
        }
        localSQLiteDatabase.close();
    }
    public void addStudent(Student x){
        SQLiteDatabase localSQLiteDatabase=this.dbhelper.getWritableDatabase();
        Object[] new_student=new Object[4];
        new_student[0]=x.getStudentId();
        new_student[1]=x.getStudentName();
        new_student[2]=x.getScore();
        new_student[3]=x.getClassId();
        try {
            localSQLiteDatabase.execSQL("insert into students(student_id,student_name,score,class_id) values(?,?,?,?)", new_student);
        }
        catch (Exception e){
            Log.e("error","添加学生失败");
        }
        localSQLiteDatabase.close();
    }
    public void del_student(String student_id){
        SQLiteDatabase localSQLiteDatabase=this.dbhelper.getWritableDatabase();
        Object[] arr=new Object[1];
        arr[0]=student_id;
        try {
            localSQLiteDatabase.execSQL("delete from students where student_id =?", arr);
        }
        catch (Exception e){
            Log.e("error","删除学生失败");
        }
        localSQLiteDatabase.close();
        }
    public void del_class(String class_id){
        SQLiteDatabase localSQLiteDatabase=this.dbhelper.getWritableDatabase();
        localSQLiteDatabase.execSQL("PRAGMA foreign_keys=ON");
        Object[] arr=new Object[1];
        arr[0]=class_id;
        try {
            localSQLiteDatabase.execSQL("delete from classes where class_id =?", arr);
        }
        catch (Exception e){
            Log.e("error","删除班级失败");
        }
        localSQLiteDatabase.close();
    }
    public void modify_Student(Student x){
        SQLiteDatabase localSQLiteDatabase=this.dbhelper.getWritableDatabase();
        Object[] new_student=new Object[4];
        new_student[3]=x.getStudentId();
        new_student[0]=x.getStudentName();
        new_student[1]=x.getScore();
        new_student[2]=x.getClassId();
        try {
            localSQLiteDatabase.execSQL("update students set student_name=?,score=?,class_id=? where student_id=?", new_student);
        }
        catch (Exception e){
            Log.e("error","修改学生信息失败");
        }
        localSQLiteDatabase.close();
    }
    public void modify_Class(Class x){
        SQLiteDatabase localSQLiteDatabase= this.dbhelper.getWritableDatabase();
        Object[]new_class=new Object[2];
        new_class[1]=x.getClassId();
        new_class[0]=x.getClassName();
        try {
            localSQLiteDatabase.execSQL("update classes set class_name=? where class_id=?", new_class);
        }
        catch (Exception e){
            Log.e("error","修改班级信息失败");
        }
        localSQLiteDatabase.close();
    }
    public List<Student> get_all_Students(){
        SQLiteDatabase localDatabase=this.dbhelper.getReadableDatabase();
        List<Student> arr=new ArrayList<Student>();
        Cursor cursor=localDatabase.rawQuery("select * from students " + "where 1=1  order by score desc ", null);
        while(cursor.moveToNext()){
            Student temp=new Student();
            temp.setStudentId(cursor.getString(cursor.getColumnIndex("student_id")));
            temp.setStudentName(cursor.getString(cursor.getColumnIndex("student_name")));
            temp.setScore(cursor.getString(cursor.getColumnIndex("score")));
            temp.setClassId(cursor.getString(cursor.getColumnIndex("class_id")));
           arr.add(temp);
        }
        localDatabase.close();
        return arr;
    }
    public List<Class> get_all_Classes() {
        List<Class> localArrayList=new ArrayList<Class>();
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();
        Cursor localCursor = localSQLiteDatabase.rawQuery("select * from classes " +
                "where 1=1", null);
        while (localCursor.moveToNext())
        {
            Class temp=new Class();
            temp.setClassId(localCursor.getString(localCursor.getColumnIndex("class_id")));
            temp.setClassName(localCursor.getString(localCursor.getColumnIndex("class_name")));
            localArrayList.add(temp);
        }
        localSQLiteDatabase.close();
        return localArrayList;
    }
    public List<Student> findStudentByClass_name(String class_name){
        SQLiteDatabase localDatebase=this.dbhelper.getReadableDatabase();
        List<Student>arr=new ArrayList<Student>();
        Cursor cursor=localDatebase.rawQuery("select student_id,student_name,score,classes.class_id from " +
                "students,classes where students.classs_id=classes.class_id and class_name=?  order by score asc",new String[]{class_name});
        while(cursor.moveToNext()){
            Student temp=new Student();
            temp.setStudentId(cursor.getString(cursor.getColumnIndex("student_id")));
            temp.setStudentName(cursor.getString(cursor.getColumnIndex("student_name")));
            temp.setScore(cursor.getString(cursor.getColumnIndex("score")));
            temp.setClassId(cursor.getString(3));
            arr.add(temp);
        }
        localDatebase.close();
        return arr;
    }
    public List<Student> findStudentByClass_id(String class_id){
        SQLiteDatabase localDatebase=this.dbhelper.getReadableDatabase();
        List<Student>arr=new ArrayList<Student>();
        Cursor cursor=localDatebase.rawQuery("select student_id,student_name,score " +
                "students where students.classs_id=? order by score asc",new String[]{class_id});
        while(cursor.moveToNext()){
            Student temp=new Student();
            temp.setStudentId(cursor.getString(cursor.getColumnIndex("student_id")));
            temp.setStudentName(cursor.getString(cursor.getColumnIndex("student_name")));
            temp.setScore(cursor.getString(cursor.getColumnIndex("score")));
            temp.setClassId(class_id);
            arr.add(temp);
        }
        localDatebase.close();
        return arr;
    }
    public List<Class> findAllClasses() {
        List<Class> localArrayList=new ArrayList<Class>();
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();
        Cursor localCursor = localSQLiteDatabase.rawQuery("select * from classes " +
                "where 1=1", null);
        while (localCursor.moveToNext())
        {
            Class temp=new Class();
            temp.setClassId(localCursor.getString(localCursor.getColumnIndex("class_id")));
            temp.setClassName(localCursor.getString(localCursor.getColumnIndex("class_name")));
            localArrayList.add(temp);
        }
        localSQLiteDatabase.close();
        return localArrayList;
    }

    /**
     * 成绩最好
     * @return
     */
    public Student findMaxScoreStudent() {
        Student temp =new Student();
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();
        Cursor localCursor = localSQLiteDatabase.rawQuery("select student_id,student_name,class_id,max(score)  from students  " +
                "where 1=1",null );
        localCursor.moveToFirst();
        temp.setStudentId(localCursor.getString(0));
        temp.setStudentName(localCursor.getString(1));
        temp.setClassId(localCursor.getString(2));
        temp.setScore(localCursor.getString(3));
        localSQLiteDatabase.close();
        return temp;
    }



    /**
     * 查找是否有该学生
     * @param studentId
     * @return
     */
    public boolean isStudentsExists(String studentId) {
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();
        Cursor localCursor = localSQLiteDatabase.rawQuery("select count(*)  from students  " +
                "where student_id=?", new String[]{studentId});
        localCursor.moveToFirst();
        localSQLiteDatabase.close();
        if(localCursor.getLong(0)>0)
            return true;
        else
            return false;
    }

    /**
     * 确认该班级是否存在
     * @param classId
     * @return
     */
    public boolean isClassExists(String s) {
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();
        Cursor localCursor = localSQLiteDatabase.rawQuery("select count(*)  from classes  " +
                "where class_id=? or class_name=?", new String[]{s,s});
        localCursor.moveToFirst();
        localSQLiteDatabase.close();
        if(localCursor.getLong(0)>0)
            return true;
        else
            return false;
    }

}

