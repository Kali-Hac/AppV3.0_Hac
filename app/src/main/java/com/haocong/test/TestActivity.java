package com.haocong.test;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.text.AlteredCharSequence;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * Created by Dell-User on 2017/6/6.
 */

public class TestActivity extends Activity implements OnClickListener {
    private  Button b,b1,b2,b3,b4,b5,b6;
    private EditText sId,sName,score,cId,cName;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private List<Student>students=new ArrayList<Student>();
    private List<Class>classes=new ArrayList<Class>();
    private DBServer db;
    private String info="";
    private TextView textView;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)
            {
                sId.setText("");
                sName.setText("");
                score.setText("");
                cName.setText("");
                cId.setText("");
            }
            else if(msg.what==1)
            {
                db.del_class((String)msg.obj);
                info += "删除一个班级及班级里面的学生：班级Id:"+(String)msg.obj;
                textView.setText(info);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_page);
        initView();
        share=getSharedPreferences("DatabaseDamo", 0);
        editor=share.edit();
        db=new DBServer(this);
        Class x=new Class();
        x.setClassId("04");
        x.setClassName("iSoftware");
        db.addClass(x);
        x.setClassId("07");
        x.setClassName("iSoftware卓越");
        db.addClass(x);
        Student s=new Student();
        s.setStudentId("01");
        s.setScore("91");
        s.setClassId("04");
        s.setStudentName("iSoftware");
        db.addStudent(s);
        s.setStudentId("02");
        s.setScore("95");
        s.setClassId("07");
        s.setStudentName("iSoftware");
        db.addStudent(s);
        s.setStudentId("03");
        s.setScore("99");
        s.setClassId("07");
        s.setStudentName("iSoftware卓越");
        db.addStudent(s);
        if(share.getInt("times", 0)==0)
        {
            editor.putInt("times", 1);
            editor.commit();
        }
    }
    private void  initView() {
        textView = (TextView) findViewById(R.id.TextView);
        sId = (EditText) findViewById(R.id.editText2);
        sName = (EditText) findViewById(R.id.editText3);
        score = (EditText) findViewById(R.id.editText4);
        cId = (EditText) findViewById(R.id.editText);
        cName = (EditText) findViewById(R.id.editText1);
        b =(Button) findViewById(R.id.button1);
        b1 =(Button) findViewById(R.id.button2);
        b2 =(Button) findViewById(R.id.button3);
        b3 =(Button) findViewById(R.id.button4);
        b4 =(Button) findViewById(R.id.button5);
        b5 =(Button) findViewById(R.id.button6);
        b6 =(Button) findViewById(R.id.button7);
        b.setOnClickListener(this);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
    }
    private void addAStudent()
    {
        info ="";
        textView.setText("");
        String tempSID = sId.getText().toString();
        String tempSName = sName.getText().toString();
        String tempScore = score.getText().toString();
        String tempCID = cId.getText().toString();
        if(checkInfo(tempSID)&&checkInfo(tempSName)&&checkInfo(tempScore)&&checkInfo(tempCID))
        {
            Student temp =new Student();
            temp.setStudentId(tempSID);
            temp.setStudentName(tempSName);
            temp.setScore(tempScore);
            temp.setClassId(tempCID);
            db.addStudent(temp);
            info+=  "add to database students:"+'\n'+temp.toString();
        }
        else
        {
            info += "添加一个学生失败：缺少必要信息，请确认studentId,studentName,score,classId的信息是否完整！";
        }
        textView.setText(info);
    }


    private void deleteAClass()
    {
        info ="";
        textView.setText("");
        final String tempCID = cId.getText().toString();
        if(checkInfo(tempCID))
        {
            if(db.isClassExists(tempCID))
            {
                new AlertDialog.Builder(this)
                        .setTitle("提示：")
                        .setMessage("删除一个班级将会删除该班的所有学生信息，确定？")
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                Message msg =new Message();
                                msg.what = 1;
                                msg.obj = tempCID;
                                handler.sendMessage(msg);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
            else
                info += "删除一个班级失败：查无此对应班级，请确认classId的信息是否正确！";
        }
        else
        {
            info += "删除一个班级失败：缺少必要信息，请确认classId的信息是否完整！";
        }
        textView.setText(info);
    }

    private void deleteAStudent()
    {
        info ="";
        textView.setText("");
        String tempSID = sId.getText().toString();
        if(checkInfo(tempSID))
        {
            if(db.isStudentsExists(tempSID))
            {
                db.del_student(tempSID);
                info += "删除一个学生：学生Id:"+tempSID;
            }
            else
                info += "删除一个学生失败：查无此对应学生，请确认studentId的信息是否正确！";
        }
        else
        {
            info += "删除一个学生失败：缺少必要信息，请确认studentId的信息是否完整！";
        }
        textView.setText(info);
    }


    private void updateStudent()
    {
        info ="";
        textView.setText("");
        String tempSID = sId.getText().toString();
        String tempSName = sName.getText().toString();
        String tempScore = score.getText().toString();
        String tempCID = cId.getText().toString();
        if(checkInfo(tempSID)&&checkInfo(tempSName)&&checkInfo(tempScore)&&checkInfo(tempCID))
        {
            if(db.isStudentsExists(tempSID))
            {
                Student temp =new Student();
                temp.setStudentId(tempSID);
                temp.setStudentName(tempSName);
                temp.setScore(tempScore);
                temp.setClassId(tempCID);
                db.modify_Student(temp);
                info+=  "update database students:"+'\n'+temp.toString();
            }
            else
            {
                Student temp =new Student();
                temp.setStudentId(tempSID);
                temp.setStudentName(tempSName);
                temp.setScore(tempScore);
                temp.setClassId(tempCID);
                db.addStudent(temp);
                info+= "没有找到对应ID的学生，将此学生添加到数据库！"+'\n'+
                        "add to database students:"+'\n'+temp.toString();
            }
        }
        else
        {
            info += "更新学生失败：缺少必要信息，请确认studentId,studentName,score,classId的信息是否完整！";
        }
        textView.setText(info);
    }

    /**
     * 打印某班的学生
     */
    private boolean checkInfo(String s)
    {
        if(s.equals("")||s==null)
            return false;
        else
            return true;
    }
    private void printStudentsOfClass()
    {
        info ="";
        textView.setText("");
        String tempCID = cId.getText().toString();
        String tempCName = cName.getText().toString();
        if(checkInfo(tempCID))
        {
            if(db.isClassExists(tempCID))
            {
                info += "使用ID查询";
                students.clear();
                students= db.findStudentByClass_id(tempCID);
            }
            else
            {
                info += "该ID对应的班级不存在";
            }
        }
        else if(checkInfo(tempCName))
        {
            if(db.isClassExists(tempCName))
            {
                info += "使用Name查询";
                students.clear();
                students = db.findStudentByClass_name(tempCName);
            }
            else
            {
                info += "该Name对应的班级不存在";
            }
        }
        else
        {
            students.clear();
            info += "查找学生失败：缺少必要信息，请确认classId或className的信息是否完整！";
        }
        for(int i=0;i<students.size();i++)
        {
            info+= '\n'+students.get(i).toString();
        }
        textView.setText(info);;
    }

    private void printMaxScoreStudent()
    {
        info ="";
        textView.setText("");
        Student temp =db.findMaxScoreStudent();
        info+= '\n'+temp.toString();
        textView.setText(info);;
    }


    private void getAllStudent()
    {
        students.clear();
        students = db.get_all_Students();
        for(int i=0;i<students.size();i++)
        {
            info+= '\n'+students.get(i).toString();
        }
    }

    private void getAllClass()
    {
        classes.clear();
        classes = db.findAllClasses();
        for(int i=0;i<classes.size();i++)
        {
            info+= '\n'+classes.get(i).toString();
        }
    }

    private void printAllInfo()
    {
        info ="";
        textView.setText("");
        getAllStudent();
        getAllClass();
        textView.setText(info);

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id)
        {
            case R.id.button7:
                printAllInfo();
                break;
            case R.id.button1:
                addAStudent();
                break;
            case R.id.button2:
                deleteAStudent();
                break;
            case R.id.button3:
                deleteAClass();
                break;
            case R.id.button4:
                updateStudent();
                break;
            case R.id.button5:
                printStudentsOfClass();
                break;
            case R.id.button6:
                printMaxScoreStudent();
                break;
        }
        handler.sendEmptyMessageDelayed(0, 5000);
    }
}
