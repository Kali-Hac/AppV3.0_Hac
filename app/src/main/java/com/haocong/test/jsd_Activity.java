package com.haocong.test;

import android.app.Activity;
import java.text.DecimalFormat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

/**
 * Created by Dell-User on 2017/6/7.
 */

public class jsd_Activity extends Activity{
    private SensorManager sensormanager;
    private TextView x_a,y_a,z_a,accelerationTextView,maxAccelerationTextView;
    private TextView x_p,y_p,z_p,p_TextView,max_pTextView;
    private MySensorEventListener sensorEventListener;
    private float currentAcceleration = 0;
    private float maxAcceleration = 0;
    private float now_p=0;
    private float max_p=0;
    private mem_DBServer db;
    private  Button x,xx,xxx,help,ziji;
    private boolean set=false;
    private boolean set_flag=false;
    private double a_yu=20.0;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)
            {

            }
            else if(msg.what==1)
            {
                mem temp=new mem();
                temp.setName("加速度阈值");
                temp.setSet_or_not("否");
                temp.setPhone_num(""+maxAcceleration);
                a_yu=maxAcceleration;
                db.add_mem(temp);
                Intent intent=new Intent();
                intent.setClass(jsd_Activity.this,Start.class);
                jsd_Activity.this.startActivity(intent);
            }
        }
    };
    public double get_a(){
        List<mem>arr=new ArrayList<mem>();
        double aa=20.0;
        try {
            arr = db.FindMemByName("加速度阈值");
            String a = arr.get(0).getPhone_num();
            aa = Double.valueOf(a);
            Toast.makeText(this,"已读取到存入的加速度阈值,为"+aa+"\n现在重新设置", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this,"还未存入加速度阈值,默认为20.0", Toast.LENGTH_SHORT).show();
        }
        return aa;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jsd);
        db=new mem_DBServer(this);
        double aa=get_a();
        if(aa!=20.0){
            a_yu=aa;
        }
        db.del_a();
        x_a=(TextView) findViewById(R.id.x_a);
        y_a=(TextView) findViewById(R.id.y_a);
        z_a=(TextView) findViewById(R.id.z_a);
        x=(Button)findViewById(R.id.x);
        xx=(Button)findViewById(R.id.xx);
        xxx=(Button)findViewById(R.id.xxx);
        ziji=(Button)findViewById(R.id.ziji);
        help=(Button)findViewById(R.id.help);
        accelerationTextView=(TextView) findViewById(R.id.a);
        maxAccelerationTextView=(TextView) findViewById(R.id.max_a);
        x_p=(TextView) findViewById(R.id.x_p);
        y_p=(TextView) findViewById(R.id.y_p);
        z_p=(TextView) findViewById(R.id.z_p);
        p_TextView=(TextView) findViewById(R.id.p);
        max_pTextView=(TextView) findViewById(R.id.max_p);
        sensorEventListener = new MySensorEventListener();
        sensormanager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Timer updateTimer = new Timer("gForceUpdate");
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                updateGUI();
            }
        }, 0, 100);//0s以后每隔100ms执行一次任务
        //schedule和scheduleAtFixedRate的区别在于，如果指定开始执行的时间在当前系统运行时间之前，scheduleAtFixedRate会把已经过去的时间也作为周期执行，而schedule不会把过去的时间算上。
    }
    private void updateGUI() {
   /*
    * 推荐的一个刷新UI的方法
    * Activity.runOnUiThread（Runnable）
    * 在新的线程中更新UI
    * Runnable是一个接口，需要你实现run方法，上面的TimerTask就是实现了这个接口同样需要实现run方法
    * */
        runOnUiThread(new Runnable() {
            public void run() {
                DecimalFormat df = new DecimalFormat("#.####");
                String currentG =  df.format(currentAcceleration/SensorManager.STANDARD_GRAVITY)
                        + "g/s";
                String currentP=now_p+"";
                accelerationTextView.setText(currentG);
                accelerationTextView.invalidate();
                p_TextView.setText(currentP);
                p_TextView.invalidate();
                String maxG =df.format(maxAcceleration/SensorManager.STANDARD_GRAVITY)+ "g/s";
                String maxP=max_p+"";
                maxAccelerationTextView.setText(maxG);
                maxAccelerationTextView.invalidate();
                max_pTextView.setText(maxP);
                max_pTextView.invalidate();
            }
        });
        x.setOnClickListener(new Button.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                windows(0);
            }

        });
        xx.setOnClickListener(new Button.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                windows(1);
            }

        });
        xxx.setOnClickListener(new Button.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                windows(2);
            }

        });
       ziji.setOnClickListener(new Button.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                windows(3);
            }

        });
        help.setOnClickListener(new Button.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                windows(4);
            }

        });
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
    public void save_a(){
        new AlertDialog.Builder(this)
                .setTitle("提示：")
                .setMessage("将 "+maxAcceleration+" 设置为加速度阈值，确定？")
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Message msg =new Message();
                        set_flag=true;
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }


    private final class MySensorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {//可以得到传感器实时测量出来的变化值
            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                float[] p_values = event.values;
                float x = p_values[0];
                float y = p_values[1];
                float z = p_values[2];
                StringBuilder sb = new StringBuilder();
                StringBuilder s2 = new StringBuilder();
                StringBuilder s3 = new StringBuilder();
                DecimalFormat df = new DecimalFormat("#.####");
//        sb.append("X方向上的加速度");
                sb.append(df.format(x));
//        s2.append("Y方向上的加速度");
                s2.append(df.format(y));
//        s3.append("z方向上的加速度");
                s3.append(df.format(z));
                x_p.setText(sb.toString());
                y_p.setText(s2.toString());
                z_p.setText(s3.toString());
                double p = Math.round(Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)));
                //消去原有的重力引起的压力
                now_p = Math.abs((float) (p));
                if (now_p > max_p)
                    max_p = now_p;

            } else if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
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
                x_a.setText(sb.toString());
                y_a.setText(s2.toString());
                z_a.setText(s3.toString());
                double a = Math.round(Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z - calibration, 2)));
                //消去原有的重力引起的压力
                currentAcceleration = Math.abs((float) (a));
                if(set_flag&&currentAcceleration>=a_yu){
                    Intent intent=new Intent();
                    intent.setClass(jsd_Activity.this,LocationActivity.class);
                    jsd_Activity.this.startActivity(intent);
                }
                if (currentAcceleration > maxAcceleration) {
                    maxAcceleration = currentAcceleration;
                    if(!set_flag&&set){
                        save_a();
                    }
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
    public void windows(int x){
        if(x==0){
            mem temp=new mem();
            temp.setName("加速度阈值");
            temp.setSet_or_not("否");
            temp.setPhone_num("25.0");
            db.add_mem(temp);
            new  AlertDialog.Builder(this)
                    .setTitle("摇一摇灵敏度设置成功！" )
                    .setMessage("当前设置等级：灵敏")
                    .setPositiveButton("确定" ,  null )
                    .show();
            Intent intent=new Intent();
            intent.setClass(jsd_Activity.this,Start.class);
            jsd_Activity.this.startActivity(intent);
        }
        else if(x==1){
            mem temp=new mem();
            temp.setName("加速度阈值");
            temp.setSet_or_not("否");
            temp.setPhone_num("37.0");
            db.add_mem(temp);
            new  AlertDialog.Builder(this)
                    .setTitle("摇一摇灵敏度设置成功！" )
                    .setMessage("当前设置等级：中等灵敏")
                    .setPositiveButton("确定" ,  null )
                    .show();
            Intent intent=new Intent();
            intent.setClass(jsd_Activity.this,Start.class);
            jsd_Activity.this.startActivity(intent);

        }
        else if(x==2){
            mem temp=new mem();
            temp.setName("加速度阈值");
            temp.setSet_or_not("否");
            temp.setPhone_num("49.0");
            db.add_mem(temp);
            new  AlertDialog.Builder(this)
                    .setTitle("摇一摇灵敏度设置成功！" )
                    .setMessage("当前设置等级：低灵敏度")
                    .setPositiveButton("确定" ,  null )
                    .show();
            Intent intent=new Intent();
            intent.setClass(jsd_Activity.this,Start.class);
            jsd_Activity.this.startActivity(intent);
        }
        else if(x==3){
            set=true;
            new  AlertDialog.Builder(this)
                    .setTitle("自定义设置灵敏度" )
                    .setMessage("将根据当前已探测的最大合加速度进行设置")
                    .setPositiveButton("确定" ,  null )
                    .show();
        }
        else if(x==4){
            new  AlertDialog.Builder(this)
                    .setTitle("用户帮助" )
                    .setMessage("①可以通过前三个按钮直接设置'摇一摇'灵敏度，系统默认灵敏度为中等\n" +
                            "②可以自定义设置灵敏度，此时线性加速度传感器会不断更新最大值并询问是否要设置" +
                            "\n③设置之后可以重新修改")
                    .setPositiveButton("确定" ,  null )
                    .show();
        }
    }

}
