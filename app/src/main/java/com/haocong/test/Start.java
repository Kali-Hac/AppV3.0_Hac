package com.haocong.test;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell-User on 2017/6/17.
 */

public class Start extends Activity {
    private SensorManager sensormanager;
    private MySensorEventListener sensorEventListener;
    private CircleMenu circleMenu;
    private Intent intent=null;
    private Spinner address_Spinner;
    private mem_DBServer db;
    private DB_all_Server db2;
    private TextView bwl_num,position,mem_num,sos_mem_num;//sos_mems
    private EditText address;
    private Button address_button;
    private Button help;
    private String address_new;
    private Switch yaoyiyao,yanshi;
    private ArrayAdapter<String> address_Adapter;
    private double a_yu=20.0;
    private float currentAcceleration = 0;
    private String mems;
    private boolean set=false,ys=true;
    private  List<mem>mem_arr=new ArrayList<mem>();
    private  List<bwl_instance>bwl_arr=new ArrayList<bwl_instance>();
    private List<String>address_items=new ArrayList<String>();//
    private Handler add_address_handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)
            {
                Toast.makeText(getApplicationContext(),"个人地址未添加，请点确认添加",Toast.LENGTH_SHORT).show();
            }
            else if(msg.what==1)
            {
                bwl_instance t=new bwl_instance();
                t.setInfo(address_new);
                t.setType("个人定位");
                db2.add_bwl(t);//
                address_items.add(address_new);
                Toast.makeText(getApplicationContext(),"成功添加个人定位",Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Handler set_handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)
            {
                set=false;
                yaoyiyao.setChecked(false);
                Toast.makeText(getApplicationContext(),"摇一摇发送紧急定位信息功能已取消",Toast.LENGTH_SHORT).show();
            }
            else if(msg.what==1)
            {
                set=true;
               windows(6);
            }
        }
    };
    private final class MySensorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {//可以得到传感器实时测量出来的变化值
            if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                double calibration = SensorManager.STANDARD_GRAVITY;
                float[] a_values = event.values;
                float x = a_values[0];
                float y = a_values[1];
                float z = a_values[2];
                DecimalFormat df = new DecimalFormat("#.####");
                StringBuilder sb = new StringBuilder();
                StringBuilder s2 = new StringBuilder();
                StringBuilder s3 = new StringBuilder();
//        sb.append("X方向上的加速度");
                sb.append(df.format(x));
//        s2.append("Y方向上的加速度");
                s2.append(df.format(y));
//        s3.append("z方向上的加速度");
                s3.append(df.format(z));
                double a = Math.round(Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z - calibration, 2)));
                //消去原有的重力引起的压力
                currentAcceleration = Math.abs((float) (a));
                if(currentAcceleration>=a_yu&&set){
                    Intent intent=new Intent();
                    intent.setClass(Start.this,LocationActivity.class);
                    intent.putExtra("personal_address",address.getText().toString());
                    intent.putExtra("first","no");
                    if(!ys)
                        intent.putExtra("yanshi","no");
                    Start.this.startActivity(intent);
                }

            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }
    }
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        sensormanager.unregisterListener(sensorEventListener);
    }
    @Override
    protected void onPause() {
        sensormanager.unregisterListener(sensorEventListener);
        super.onPause();
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        Sensor orientationSensor = sensormanager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensormanager.registerListener(sensorEventListener, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor accelerometerSensor = sensormanager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensormanager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
        //注册监听器，监听加速度传感器的变化
        //SensorManager.SENSOR_DELAY_UI表示灵敏度，这个是适合用户界面一般行为的频率
        //还有SENSOR_DELAY_GAME、 SENSOR_DELAY_FASTEST、SENSOR_DELAY_NORMAL   顾名思义，具体可以看api。
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_start);
        db=new mem_DBServer(this);
        db2=new DB_all_Server(this);
        sensorEventListener = new MySensorEventListener();
        sensormanager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        circleMenu = (CircleMenu) findViewById(R.id.circle_menu);
        bwl_num=(TextView)findViewById(R.id.bwl_num);
        position=(TextView)findViewById(R.id.position);
        mem_num=(TextView)findViewById(R.id.mem_num);
        sos_mem_num=(TextView)findViewById(R.id.sos_mem_num);
//        sos_mems=(TextView)findViewById(R.id.sos_mems);
        address_Spinner=(Spinner)findViewById(R.id.address_spinner);
        address_button=(Button)findViewById(R.id.address_button);
        address=(EditText)findViewById(R.id.address);
        yaoyiyao=(Switch)findViewById(R.id.yaoyiyao);
        yanshi=(Switch)findViewById(R.id.yanshi);
        help=(Button)findViewById(R.id.help);
        try {
           String info= getIntent().getExtras().getString("gps");
            position.setText("当前位置："+info);
            windows(5);
        }
        catch (Exception e){

        }
        try {
            mem_arr = db.get_all_mem();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"获取联系人信息失败！",Toast.LENGTH_SHORT).show();
        }
        try {
            bwl_arr = db2.get_bwl();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"获取备忘录信息失败！",Toast.LENGTH_SHORT).show();
        }
        address_items.add("[家中休憩]");
        address_items.add("[外出散步]");
        address_items.add("[晨练中]");
        address_items.add("[购物]");
        address_items.add("[出游]");
//        db2.delete_personalAddr();
        List<String>save_address=new ArrayList<String>();
        try {
            save_address = db2.get_personal_address();
        }
        catch (Exception e){

        }
        if(save_address.size()>0){
            for(int i=0;i<save_address.size();i++){
                address_items.add(save_address.get(i));
            }
        }
        address_Adapter = new ArrayAdapter<String>(Start.this,
                android.R.layout.simple_spinner_item,address_items);
        address_Spinner.setAdapter(address_Adapter);//
        address_Spinner.setSelection(address_items.size()-1,true);
        address.setText(address_items.get(address_items.size()-1));
        address_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {

                address.setText(address_items.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }

        });
       address_button.setOnClickListener(new Button.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                String personal_address=address.getText().toString();
                if(personal_address.equals(""))
                    Toast.makeText(getApplicationContext(),"地址不能为空！",Toast.LENGTH_SHORT).show();
                else {
                    boolean flag = false;
                    for (int i = 0; i < address_items.size(); i++) {
                        if(personal_address.equals(address_items.get(i))) {
                            flag = true;
                            break;
                        }
                    }
                    if(flag)
                        Toast.makeText(getApplicationContext(),"该地址已存在，请重新输入！",Toast.LENGTH_SHORT).show();
                    else{
                        address_new=personal_address;
                        windows(0);
                    }
                }
            }

        });
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.mipmap.cong, R.mipmap.icon_cancel)
                .addSubMenu(Color.parseColor("#258CFF"), R.mipmap.icon_home)
                .addSubMenu(Color.parseColor("#30A400"), R.mipmap.icon_search)
                .addSubMenu(Color.parseColor("#FF4B32"), R.mipmap.icon_notify)
                .addSubMenu(Color.parseColor("#258CFF"), R.mipmap.icon_home_left)
                .addSubMenu(Color.parseColor("#8A39FF"), R.mipmap.icon_setting)
                .addSubMenu(Color.parseColor("#FF6A00"), R.mipmap.icon_gps)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {

                    @Override
                    public void onMenuSelected(int index) {
                        switch (index){
                            case  0:
                                intent=new Intent();
                                intent.setClass(Start.this,Home.class);
                                intent.putExtra("personal_address",address.getText().toString());
                                try {
                                    if (!ys)
                                        intent.putExtra("yanshi", "no");
                                    if (!set)
                                        intent.putExtra("set", "no");
                                }
                                catch(Exception e){

                                }
                                    Start.this.startActivity(intent);
                                break;
                            case  1:
                                intent=new Intent();
                                intent.setClass(Start.this,SendActivity.class);
                                Start.this.startActivity(intent);
                                break;
                            case  2:
                                intent=new Intent();
                                intent.setClass(Start.this,BWL.class);
                                Start.this.startActivity(intent);
                                break;
                            case  3:
                               System.exit(0);
                                break;
                            case  4:
                                intent=new Intent();
                                intent.setClass(Start.this,jsd_Activity.class);
                                Start.this.startActivity(intent);
                                break;
                            case  5:
                                Intent intent=new Intent();
                                intent.setClass(Start.this,LocationActivity.class);
                                intent.putExtra("type","定位");
                                Start.this.startActivity(intent);
                                System.exit(0);
                                break;
                        }
                    }

                }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

            @Override
            public void onMenuOpened() {
            }

            @Override
            public void onMenuClosed() {

            }

        });
        circleMenu.openMenu();
        if(!address.getText().toString().equals("")) {
            set = true;
            yaoyiyao.setChecked(true);
        }
        yanshi.setChecked(true);
        String bwl_number=db2.count_bwl()+"";
        bwl_num.setText("      备忘录条数："+bwl_number);
        String mem_number=db.count_mem()+"";
        mem_num.setText("      联系人总数："+mem_number);
        String sos_mem_number=db.count_sos_mem()+"";
        sos_mem_num.setText("       紧急联系人数："+sos_mem_number);

        List<String>sos_mem_arr=new ArrayList<String>();
        sos_mem_arr=db.get_sos_mems();
            mems="";
        if(sos_mem_arr.size()>0)
            mems+=sos_mem_arr.get(0);
        for(int i=1;i<sos_mem_arr.size();i++){
            mems+="、"+sos_mem_arr.get(i);
        }

//        sos_mems.setText("紧急联系人："+mems);
        List<mem> arr=new ArrayList<mem>();
        try {
            arr = db.FindMemByName("加速度阈值");
            String a = arr.get(0).getPhone_num();
            double aa = Double.valueOf(a);
            a_yu=aa;
            Toast.makeText(this,"已读取到存入的加速度阈值,为 "+aa, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this,"还未存入加速度阈值,默认为20.0", Toast.LENGTH_SHORT).show();
        }
//        windows(4);
        sos_mem_num.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
               windows(1);
            }
        });
        mem_num.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
               windows(2);
            }
        });
        bwl_num.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                windows(3);
            }
            });
        yaoyiyao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if(!set)
                         windows(4);
                } else {
                    set=false;
                }

            }
        });
        yanshi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ys=true;
                } else {
                    if(ys)
                        ys=false;
                    windows(7);
                }

            }
        });
       help.setOnClickListener(new Button.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                windows(8);
            }

        });
    }

    private void windows(int x){
        if(x==0) {
            new AlertDialog.Builder(this)
                    .setTitle("系统提示：")
                    .setMessage("确定要添加该个人地址吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 1;
                            add_address_handler.sendMessage(msg);
                        }
                    })
                    .setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 0;
                            add_address_handler.sendMessage(msg);
                        }
                    })
                    .show();
        }
        else if(x==1){
            new  AlertDialog.Builder(this)
                    .setTitle("紧急联系人列表" )
                    .setMessage("已设置："+mems)
                    .setPositiveButton("确定" ,  null )
                    .show();
        }
        else if(x==2){
            String data="";
            for(int i=0;i<mem_arr.size();i++){
                data+=mem_arr.get(i).toString()+"\n";
            }
            new  AlertDialog.Builder(this)
                    .setTitle("所有联系人列表" )
                    .setMessage(data)
                    .setPositiveButton("确定" ,  null )
                    .show();
        }
        else if(x==3){
            String data="";
            for(int i=0;i<bwl_arr.size();i++){
                data+=bwl_arr.get(i).toString()+"\n";
            }
            new  AlertDialog.Builder(this)
                    .setTitle("所有备忘录列表" )
                    .setMessage(data)
                    .setPositiveButton("确定" ,  null )
                    .show();
        }
        else if(x==4){
            new AlertDialog.Builder(this)
                    .setTitle("系统功能提示：")
                    .setMessage("是否开启“摇一摇”发送紧急定位信息功能？")
                    .setPositiveButton("确认开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 1;
                            set_handler.sendMessage(msg);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 0;
                            set_handler.sendMessage(msg);
                        }
                    })
                    .show();
            new  AlertDialog.Builder(this)
                    .setTitle("定位提示" )
                    .setMessage("请首先点击gps按钮定位~")
                    .setPositiveButton("确定" ,  null )
                    .show();
        }
        else if(x==5){
            new  AlertDialog.Builder(this)
                    .setTitle("更精确定位提示" )
                    .setMessage("请添加更具体的个人定位信息(在横线上)并点击“添加地址”按钮~")
                    .setPositiveButton("确定" ,  null )
                    .show();
        }
        else if(x==6){
            new  AlertDialog.Builder(this)
                    .setTitle("功能设置成功！" )
                    .setMessage("摇晃你的手机将直接发送紧急定位信息(加速度可以自己设置)~")
                    .setPositiveButton("确定" ,  null )
                    .show();
        }
        else if(x==7){
            new  AlertDialog.Builder(this)
                    .setTitle("功能设置成功！" )
                    .setMessage("定位信息将直接不发送而不延时10s~")
                    .setPositiveButton("确定" ,  null )
                    .show();
        }
        else if(x==8){
            new  AlertDialog.Builder(this)
                    .setTitle("用户帮助" )
                    .setMessage(("①填写附加地址用于发送更精确的定位短信给紧急联系人\n②首次按定位即可在最顶端显示gps定位地址" +
                            "\n③点击联系人数、备忘录数即可获取详细信息\n④开启'摇一摇'定位后，摇动手机即可发送详细定位信息给紧急联系人" +
                            "\n⑤选择延时发送后，会在定位10s后发送信息(可自行取消)\n⑥菜单顺时针依次是:(蓝)详细主页、(绿)联系人、(红)备忘录、(蓝)退出、(紫)设置摇一摇的加速度、(橙)定位"))
                    .setPositiveButton("确定" ,  null )
                    .show();
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        circleMenu.openMenu();
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onBackPressed() {
        circleMenu.closeMenu();
    }
    }
