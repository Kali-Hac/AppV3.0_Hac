package com.haocong.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

public class BWL extends Activity {
    private CircleMenu circleMenu;
    private TextView tv = null,bwl_db=null;
    private Button setTime,cancelTime,display_bwl,add_button,del_item,get_item,help;
    private EditText info,info_id,time;
    private Spinner importance_Spinner,type_Spinner,rem_time_Spinner;
    private  ArrayAdapter<String> importance_Adapter = null,type_Adapter=null,rem_time_Adapter=null;
    private Calendar c = null;
    private int importance_Pos,type_Pos,rem_time_Pos;
    private DB_all_Server db;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private List<bwl_instance>bwl_arr=new ArrayList<bwl_instance>();
    private String[] importance_items = new String[] {"普通","重要","非常重要"};//
    private String[] type_items = new String[] {"其它","运动","健康"};//
    private String[] rem_time_items = new String[] {"30s","1min","5min","30min","无"};//
    private Handler cancel_handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)
            {

            }
            else if(msg.what==1)
            {
                Intent intent = new Intent(BWL.this, AlarmReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(BWL.this, 0,
                        intent, 0);
                AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
                //取消警报
                am.cancel(pi);
                Toast.makeText(getApplicationContext(),"备忘录提醒闹钟已被取消", Toast.LENGTH_LONG).show();
//                tv.setText("备忘录提醒闹钟已被取消");
            }
        }
    };
    private Handler del_handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)
            {

            }
            else if(msg.what==1)
            {
                int i = Integer.parseInt(info_id.getText().toString());
                db.delete_bwl_by_id(i);
                bwl_db.setText("");
                Toast.makeText(getApplicationContext(),  "成功删除编号为 "+i+" 的备忘记录~", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Handler clear_handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)
            {

            }
            else if(msg.what==1)
            {
                info.setText("");
                time.setText("");
                importance_Spinner.setSelection(0,true);
                type_Spinner.setSelection(0,true);
                rem_time_Spinner.setSelection(0,true);
                Toast.makeText(getApplicationContext(), "成功清空内容重置信息~", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private boolean checkInfo(String s)
    {
        if(s.equals("")||s==null)
            return false;
        else
            return true;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bwl);
        share=getSharedPreferences("DatabaseDamo", 0);
        editor=share.edit();
        db=new DB_all_Server(this);
        bwl_db = (TextView)findViewById(R.id.bwl_db);

        tv = (TextView)findViewById(R.id.tv);
//        setTime = (Button)findViewById(R.id.set_alarm);
        display_bwl=(Button)findViewById(R.id.display_bwl);
//        add_item=(Button)findViewById(R.id.add_item);
        del_item=(Button)findViewById(R.id.del_item);
        get_item=(Button)findViewById(R.id.get_item);
        info=(EditText)findViewById(R.id.info);
        add_button=(Button)findViewById(R.id.add_button);
        time=(EditText)findViewById(R.id.time);
        help=(Button)findViewById(R.id.help);
        info_id=(EditText)findViewById(R.id.info_id);
//        cancelTime = (Button)findViewById(R.id.cancel_alarm);
       importance_Spinner = (Spinner)findViewById(R.id.importance);
        rem_time_Spinner = (Spinner)findViewById(R.id.rem_time);
        type_Spinner = (Spinner)findViewById(R.id.type);
        //绑定适配器和值
        importance_Adapter = new ArrayAdapter<String>(BWL.this,
                android.R.layout.simple_spinner_item,importance_items);
        type_Adapter = new ArrayAdapter<String>(BWL.this,
                android.R.layout.simple_spinner_item,type_items);
        rem_time_Adapter = new ArrayAdapter<String>(BWL.this,
                android.R.layout.simple_spinner_item,rem_time_items);
        importance_Spinner.setAdapter(importance_Adapter);
        type_Spinner.setAdapter(type_Adapter);
        rem_time_Spinner.setAdapter(rem_time_Adapter);
        importance_Spinner.setSelection(0,true);
        type_Spinner.setSelection(0,true);
        rem_time_Spinner.setSelection(0,true); //设置默认选中项，此处为默认选中第1个值
        importance_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {

                importance_Pos= position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                importance_Pos=0;
            }

        });
        type_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {

               type_Pos= position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                type_Pos=0;
            }

        });
        rem_time_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {


                rem_time_Pos= position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                rem_time_Pos=0;
            }

        });
        if(share.getInt("times", 0)==0)
        {
            editor.putInt("times", 1);
            editor.commit();
        }

        circleMenu = (CircleMenu) findViewById(R.id.circle_menu);

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.mipmap.cong, R.mipmap.icon_cancel)
                .addSubMenu(Color.parseColor("#258CFF"), R.mipmap.clear)
                .addSubMenu(Color.parseColor("#30A400"), R.mipmap.icon_search)
                .addSubMenu(Color.parseColor("#FF4B32"), R.mipmap.icon_notify)
                .addSubMenu(Color.parseColor("#8A39FF"), R.mipmap.icon_notify_no)
                .addSubMenu(Color.parseColor("#FF6A00"), R.mipmap.s)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {

                    @Override
                    public void onMenuSelected(int index) {
                        switch (index){
                            case 0:
                               windows(3);
                                break;
                            case 1:
                                bwl_instance t=new bwl_instance();
                                int i = Integer.parseInt(info_id.getText().toString());
                                try {
                                    t = db.find_bwl_by_id(i);
                                    bwl_db.setText("");
                                    bwl_db.setText(t.toString());
                                    Toast.makeText(getApplicationContext(), "成功获取编号为 " + i + " 的备忘记录~", Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e){
                                    Toast.makeText(getApplicationContext(), "不存在编号为 " + i + " 的备忘记录~请重新输入查询", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 2:
                                c = Calendar.getInstance();
                                c.setTimeInMillis(System.currentTimeMillis());
                                int hour = c.get(Calendar.HOUR_OF_DAY);
                                int minute = c.get(Calendar.MINUTE);
                                new TimePickerDialog(BWL.this, minute, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        //是设置日历的时间，主要是让日历的年月日和当前同步
                                        c.setTimeInMillis(System.currentTimeMillis());
                                        //设置小时分钟，秒和毫秒都设置为0
                                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        c.set(Calendar.MINUTE, minute);
                                        c.set(Calendar.SECOND, 0);
                                        c.set(Calendar.MILLISECOND, 0);
                                        Intent intent = new Intent(BWL.this, AlarmReceiver.class);
                                        String temp_info=info.getText().toString();
                                        intent.putExtra("tx_info","["+type_items[type_Pos]+":"+importance_items[importance_Pos]+"]"+temp_info);
                                        PendingIntent pi = PendingIntent.getBroadcast(BWL.this, 0, intent, 0);
                                        //得到AlarmManager实例
                                        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
                                        //根据当前时间预设一个警报
                                        if(checkInfo(temp_info)) {
                                            am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
                                            /**
                                             * 第一个参数是警报类型；第二个参数是第一次执行的延迟时间，可以延迟，也可以马上执行；第三个参数是重复周期为一天
                                             * 这句话的意思是设置闹铃重复周期，也就是执行警报的间隔时间
                                             */
//                      am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+(60*1000),
//                              (24*60*60*1000), pi);
                                            if(rem_time_items[rem_time_Pos].equals("30s")) {
                                                am.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),
                                                        30000, pi);
                                            }
                                            if(rem_time_items[rem_time_Pos].equals("1min")) {
                                                am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() ,
                                                        60000, pi);
                                            }
                                            if(rem_time_items[rem_time_Pos].equals("5min")) {
                                                am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() ,
                                                        300000, pi);
                                            }
                                            if(rem_time_items[rem_time_Pos].equals("30min")) {
                                                am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() ,
                                                        1800000, pi);
                                            }
                                            if(rem_time_items[rem_time_Pos].equals("无")) {
                                                am.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis() ,
                                                        1800000000, pi);
                                            }
                                            String msg = "成功设置备忘录提醒时间为" + format(hourOfDay) + ":" + format(minute);
                                            String rem_time= format(hourOfDay) + ":" + format(minute);
                                            time.setText(rem_time);
                                            bwl_instance bwl=new bwl_instance();
                                            bwl.setTime(rem_time);
                                            bwl.setType(type_items[type_Pos]);
                                            bwl.setImportance(importance_items[importance_Pos]);
                                            bwl.setInfo(temp_info);
                                            bwl.setRem_time(rem_time_items[rem_time_Pos]);
                                            db.add_bwl(bwl);
                                            Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
//                                            tv.setText(msg);
                                        }
                                        else{
                                            String msg = "信息填写不完整，无法生成备忘录提醒";
                                            Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
//                                            tv.setText(msg);
                                        }
                                    }
                                }, hour, minute, true).show();
                                //上面的TimePickerDialog中的5个参数参考：http://blog.csdn.net/yang_hui1986527/article/details/6839342
                                break;
                            case 3:
                               windows(1);
                                break;
                            case 4:
                              windows(2);
                                break;
                        }
                    }

                }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

            @Override
            public void onMenuOpened() {

            }

            @Override
            public void onMenuClosed() {
//                Toast.makeText(getApplicationContext(), "Please click the button and get more choice！", Toast.LENGTH_SHORT).show();
            }

        });
        circleMenu.openMenu();
        //得到日历实例，主要是为了下面的获取时间
        c = Calendar.getInstance();
//        setTime.setOnClickListener(new Button.OnClickListener()
//        {
//
//            @Override
//            public void onClick(View arg0) {
//                c.setTimeInMillis(System.currentTimeMillis());
//                int hour = c.get(Calendar.HOUR_OF_DAY);
//                int minute = c.get(Calendar.MINUTE);
//                new TimePickerDialog(BWL.this, minute, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        //是设置日历的时间，主要是让日历的年月日和当前同步
//                        c.setTimeInMillis(System.currentTimeMillis());
//                        //设置小时分钟，秒和毫秒都设置为0
//                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                        c.set(Calendar.MINUTE, minute);
//                        c.set(Calendar.SECOND, 0);
//                        c.set(Calendar.MILLISECOND, 0);
//                        Intent intent = new Intent(BWL.this, AlarmReceiver.class);
//                        String temp_info=info.getText().toString();
//                        intent.putExtra("tx_info","["+type_items[type_Pos]+":"+importance_items[importance_Pos]+"]"+temp_info);
//                        PendingIntent pi = PendingIntent.getBroadcast(BWL.this, 0, intent, 0);
//                        //得到AlarmManager实例
//                        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
//                        //根据当前时间预设一个警报
//                        if(checkInfo(temp_info)) {
//                            am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
//                            /**
//                             * 第一个参数是警报类型；第二个参数是第一次执行的延迟时间，可以延迟，也可以马上执行；第三个参数是重复周期为一天
//                             * 这句话的意思是设置闹铃重复周期，也就是执行警报的间隔时间
//                             */
////                      am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+(60*1000),
////                              (24*60*60*1000), pi);
//                            if(rem_time_items[rem_time_Pos].equals("30s")) {
//                                am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() ,
//                                        30000, pi);
//                            }
//                            if(rem_time_items[rem_time_Pos].equals("1min")) {
//                                am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() ,
//                                        60000, pi);
//                            }
//                            if(rem_time_items[rem_time_Pos].equals("5min")) {
//                                am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() ,
//                                        300000, pi);
//                            }
//                            if(rem_time_items[rem_time_Pos].equals("30min")) {
//                                am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() ,
//                                        1800000, pi);
//                            }
//                            if(rem_time_items[rem_time_Pos].equals("无")) {
//                                am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() ,
//                                        1800000000, pi);
//                            }
//                            String msg = "成功设置备忘录提醒时间为" + format(hourOfDay) + ":" + format(minute);
//                            String rem_time= format(hourOfDay) + ":" + format(minute);
//                            time.setText(rem_time);
//                            bwl_instance bwl=new bwl_instance();
//                            bwl.setTime(rem_time);
//                            bwl.setType(type_items[type_Pos]);
//                            bwl.setImportance(importance_items[importance_Pos]);
//                            bwl.setInfo(temp_info);
//                            bwl.setRem_time(rem_time_items[rem_time_Pos]);
//                            db.add_bwl(bwl);
//                            tv.setText(msg);
//                        }
//                        else{
//                            String msg = "信息填写不完整，无法生成备忘录提醒";
//                            tv.setText(msg);
//                        }
//                    }
//                }, hour, minute, true).show();
//                //上面的TimePickerDialog中的5个参数参考：http://blog.csdn.net/yang_hui1986527/article/details/6839342
//            }
//
//        });

//        cancelTime.setOnClickListener(new Button.OnClickListener()
//        {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(BWL.this, AlarmReceiver.class);
//                PendingIntent pi = PendingIntent.getBroadcast(BWL.this, 0,
//                        intent, 0);
//                AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
//                //取消警报
//                am.cancel(pi);
//                tv.setText("闹钟取消");
//            }
//
//        });
//        add_item.setOnClickListener(new Button.OnClickListener()
//        {
//  //单纯的清空&提示作用
//            @Override
//            public void onClick(View v) {
//                info.setText("");
//                time.setText("");
//                Toast.makeText(getApplicationContext(), "新的备忘录加入数据库成功", Toast.LENGTH_SHORT).show();
//            }
//
//        });
    display_bwl.setOnClickListener(new Button.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            bwl_arr=db.get_bwl();
            Toast.makeText(getApplicationContext(), "成功获取所有备忘事件~", Toast.LENGTH_SHORT).show();
            String data="";
            for(int i=0;i<bwl_arr.size();i++){
                data+="\n编号："+bwl_arr.get(i).getInfo_id()+"\n备忘类型："+bwl_arr.get(i).getType()+"\n备忘信息："+bwl_arr.get(i).getInfo()+"\n备忘时间："+bwl_arr.get(i).getTime()+"\n重要程度："+bwl_arr.get(i).getImportance()+"\n提醒间隔时间："+bwl_arr.get(i).getRem_time();
            }
          bwl_db.setText(data);
        }

    });
        del_item.setOnClickListener(new Button.OnClickListener()
        {
            //单纯的清空&提示作用
            @Override
            public void onClick(View v) {
               windows(2);
            }

        });
        get_item.setOnClickListener(new Button.OnClickListener()
        {
            //单纯的清空&提示作用
            @Override
            public void onClick(View v) {
                bwl_instance t=new bwl_instance();
                int i = Integer.parseInt(info_id.getText().toString());
                try {
                    t = db.find_bwl_by_id(i);
                    bwl_db.setText("");
                    bwl_db.setText(t.toString());
                    Toast.makeText(getApplicationContext(), "成功获取编号为 " + i + " 的备忘记录~", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "不存在编号为 " + i + " 的备忘记录~请重新输入查询", Toast.LENGTH_SHORT).show();
                }
            }

        });
        add_button.setOnClickListener(new Button.OnClickListener()
        {
            //单纯的清空&提示作用
            @Override
            public void onClick(View v) {
                bwl_instance t=new bwl_instance();
                String temp_info=info.getText().toString();
                String temp_time=time.getText().toString();
                if(checkInfo(temp_info)&&!temp_time.equals("请通过上面按钮设置")){
                            bwl_instance bwl=new bwl_instance();
                            bwl.setTime(temp_time);
                            bwl.setType(type_items[type_Pos]);
                            bwl.setImportance(importance_items[importance_Pos]);
                            bwl.setInfo(temp_info);
                            bwl.setRem_time(rem_time_items[rem_time_Pos]);
                            db.add_bwl(bwl);
                    windows(4);
                }
                else{
                    String msg = "信息填写不完整，无法生成备忘录提醒";
                    Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
//                            tv.setText(msg);
                }

            }

        });
        help.setOnClickListener(new Button.OnClickListener()
        {
            //单纯的清空&提示作用
            @Override
            public void onClick(View v) {
               windows(5);

            }

        });
    }

    private String format(int x)
    {
        String s = ""+x;
        if(s.length() == 1)
            s = "0"+s;
        return s;
    }
    private void  windows(int x){
        if(x==1){
            new AlertDialog.Builder(this)
                    .setTitle("系统提示：")
                    .setMessage("确定取消该备忘录定时提醒吗？(一旦取消需要重新设置)")
                    .setPositiveButton("确认取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 1;
                            cancel_handler.sendMessage(msg);
                        }
                    })
                    .setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 0;
                            cancel_handler.sendMessage(msg);
                        }
                    })
                    .show();
        }
        else if(x==2){
            new AlertDialog.Builder(this)
                    .setTitle("系统提示：")
                    .setMessage("确定删除该备忘录信息吗？(一旦删除不可恢复)")
                    .setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 1;
                            del_handler.sendMessage(msg);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 0;
                            del_handler.sendMessage(msg);
                        }
                    })
                    .show();
        }
        else if(x==3){
            new AlertDialog.Builder(this)
                    .setTitle("系统提示：")
                    .setMessage("确定重置已填写的信息吗？(一旦清空重置不可恢复)")
                    .setPositiveButton("确认重置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 1;
                            clear_handler.sendMessage(msg);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 0;
                            clear_handler.sendMessage(msg);
                        }
                    })
                    .show();
        }
        else if(x==4){
            new  AlertDialog.Builder(this)
                    .setTitle("成功添加" )
                    .setMessage("新的备忘录记录已成功加入！")
                    .setPositiveButton("确定" ,  null )
                    .show();
        }
        else if(x==5){
            new  AlertDialog.Builder(this)
                    .setTitle("用户帮助" )
                    .setMessage("①添加备忘录需要填写完整的备忘录信息，事项类型包括'健康'、'运动'、'其它'" +
                            "\n②可以设置具体时间(填写完整信息后点击红色闹铃按钮)，并自动生成一个定时的提醒" +
                            "\n③可以设置自定义事件(填写完整信息后点击下面的灰色最长按钮，此时不会生成自动提醒)" +
                            "\n④点击紫色闹钟按钮可以取消最近的备忘录定时提醒" +
                            "\n⑤点击蓝色的'清'按钮可以清除已填写的信息(不会影响已保存信息)" +
                            "\n⑥先点击'显示所有备忘事件'可以获取每个备忘录的ID，然后可以进行删除操作等")
                    .setPositiveButton("确定" ,  null )
                    .show();
        }
    }

}