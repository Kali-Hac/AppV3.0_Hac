package com.haocong.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.georgeyang.doublescroll.DoubleScrollView;
import cn.georgeyang.doublescroll.DoubleScrollViewPager;



public class Home extends AppCompatActivity {
    private SensorManager sensormanager;
    private MySensorEventListener sensorEventListener;
    private TabLayout tabLayout;
    private DoubleScrollView scrollView;
    private cn.georgeyang.doublescroll.DoubleScrollViewPager viewPager;
    private SAdapter adapter;
    private View layoutTabTitle;
    private TextView user_name,user_age,user_job,user_number,user_fujia,user_website,user_address;
    private ImageView hc,yx;
    private mem_DBServer db;
    private double a_yu=20.0;
    private boolean set=true,ys=true;
    private float currentAcceleration = 0;
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
                    intent.setClass(Home.this,LocationActivity.class);
                    String info = getIntent().getExtras().getString("personal_address");
                    intent.putExtra("personal_address",info);
                    try {
                        if (!ys)
                            intent.putExtra("yanshi", "no");
                    }
                    catch (Exception e){

                    }
                        Home.this.startActivity(intent);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.georgeyang.doublescroll.R.layout.activity_listview);
        db=new mem_DBServer(this);
        sensorEventListener = new MySensorEventListener();
        sensormanager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        scrollView = (DoubleScrollView) findViewById(cn.georgeyang.doublescroll.R.id.layoutContent);
        scrollView.setupTitleView(findViewById(cn.georgeyang.doublescroll.R.id.layoutTop));
        scrollView.setContentView(findViewById(cn.georgeyang.doublescroll.R.id.layoutContentBottom));
        tabLayout = (TabLayout) findViewById(cn.georgeyang.doublescroll.R.id.tabLayout);

        viewPager = (DoubleScrollViewPager) findViewById(cn.georgeyang.doublescroll.R.id.viewPager);
        layoutTabTitle = findViewById(cn.georgeyang.doublescroll.R.id.layoutTabTitle);
        user_name=(EditText)findViewById(cn.georgeyang.doublescroll.R.id.user_name);
        user_age=(EditText)findViewById(cn.georgeyang.doublescroll.R.id.user_age);
        user_address=(EditText)findViewById(cn.georgeyang.doublescroll.R.id.user_address);
        user_number=(EditText)findViewById(cn.georgeyang.doublescroll.R.id.user_number);
        user_job=(EditText)findViewById(cn.georgeyang.doublescroll.R.id.user_job);
        user_fujia=(EditText)findViewById(cn.georgeyang.doublescroll.R.id.fujia);
        user_website=(EditText)findViewById(cn.georgeyang.doublescroll.R.id.user_website);
        hc=(ImageView) findViewById(cn.georgeyang.doublescroll.R.id.imgAvatar);
        yx=(ImageView) findViewById(cn.georgeyang.doublescroll.R.id.imgQrCode);
        try{
            String []arr=new String[7];
            arr=db.get_user_info();
            user_name.setText(arr[0]);
            user_number.setText(arr[1]);
            user_address.setText(arr[2]);
            user_job.setText(arr[3]);
            user_fujia.setText(arr[4]);
            user_website.setText(arr[5]);
            user_age.setText(arr[6]);
        }
        catch (Exception e){

        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                scrollView.setContentInnerScrollableView(adapter.getSlidableView(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter = new SAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.setContentInnerScrollableView(adapter.getSlidableView(0));
                viewPager.setTagHeight(scrollView.getMeasuredHeight() - layoutTabTitle.getMeasuredHeight());
            }
        },100);
        List<mem> arr=new ArrayList<mem>();
        try {
            arr = db.FindMemByName("加速度阈值");
            String a = arr.get(0).getPhone_num();
            double aa = Double.valueOf(a);
            a_yu=aa;
            Toast.makeText(this,"已读取到存入的加速度阈值,为"+" a", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this,"还未存入加速度阈值,默认为20.0", Toast.LENGTH_SHORT).show();
        }
        try {
            String ys_info = getIntent().getExtras().getString("yanshi");
            String set_info = getIntent().getExtras().getString("set");
            if (ys_info.equals("no"))
                ys = false;
            if (set_info.equals("no"))
                set = false;
        }
        catch (Exception e){

        }
        hc.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.del_user_info();
                mem temp=new mem();
                temp.setName(user_name.getText().toString());
                temp.setPhone_type("用户信息1");
                temp.setPhone_num(user_number.getText().toString());
                temp.setSet_or_not("否");
                db.add_mem(temp);
                temp.setName(user_address.getText().toString());
                temp.setPhone_type("用户信息2");
                temp.setPhone_num(user_job.getText().toString());
                temp.setSet_or_not("否");
                db.add_mem(temp);
                temp.setName(user_fujia.getText().toString());
                temp.setPhone_type("用户信息3");
                temp.setPhone_num(user_website.getText().toString());
                temp.setSet_or_not("否");
                db.add_mem(temp);
                temp.setPhone_type("用户信息4");
                temp.setPhone_num(user_age.getText().toString());
                temp.setSet_or_not("否");
                db.add_mem(temp);
                Toast.makeText(getApplicationContext(),"修改用户信息成功！",Toast.LENGTH_SHORT).show();
            }
        });
        yx.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                windows(0);
            }
        });

    }
    public void windows(int x){
        if(x==0){
            new  AlertDialog.Builder(this)
                    .setTitle("用户帮助" )
                    .setMessage("①修改完个人信息后点击左边头像即可保存！\n②下面分别显示：联系人信息(详细)、紧急" +
                            "联系人信息(简略)、运动/健康备忘录、设置信息(包括定位设置、短信通知、加速度(灵敏度设置)、最近的几次定位)")
                    .setPositiveButton("确定" ,  null )
                    .show();
        }
    }
}
