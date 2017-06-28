package com.haocong.test;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.*;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

/**
 * Created by Dell-User on 2017/6/7.
 */

public class LocationActivity extends Activity  implements LocationSource, AMapLocationListener {
    private static final int WRITE_COARSE_LOCATION_REQUEST_CODE =1 ;
    private boolean qx=false;
    private AlertDialog alertDialog = null;//私有的对话框
    private DelayCloseController delayCloseController = new DelayCloseController();
    private String type;
    private class DelayCloseController extends TimerTask {
        private Timer timer = new Timer();
        private int actionFlags = 1;//标志位参数
        public void setCloseFlags(int flag)
        {
            actionFlags = flag;
        }
        @Override
        public void run() {
            if(qx)
                actionFlags=0;
            Message messageFinish = new Message();
            messageFinish.what = actionFlags ;
            handle_send.sendMessage(messageFinish);
        }
    }
    private mem_DBServer db;
    private List<mem>mem_arr=new ArrayList<mem>();
    private List<String>set_num_arr=new ArrayList<String>();
    private DB_all_Server db2;
    AMap aMap = null;
    MapView mapView = null;
    boolean isFirstLoc = true;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private String current_address,current_time;
    //初始化AMapLocationClientOption对象
    private Handler handle_send=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)
            {   String info="",info2="";
                try {
               info = getIntent().getExtras().getString("type");
            }
            catch (Exception e){

            }
//            try {
//                info2 = getIntent().getExtras().getString("first");
//            }
//            catch (Exception e){
//
//            }
                if(info.equals("定位")){
                    Intent n_intent=new Intent();
                    n_intent.putExtra("gps",current_address);
                    n_intent.setClass(LocationActivity.this,Start.class);
                    LocationActivity.this.startActivity(n_intent);
                    System.exit(0);
                    onDestroy();
                }  //关闭对话框
            }
            else if(msg.what==1)
            {
                for(int i=0;i<set_num_arr.size();i++) {
                    sendSMSMessage(set_num_arr.get(i));
                }
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        db2=new DB_all_Server(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        }
        db=new mem_DBServer(this);
        mem_arr=db.get_all_mem();
        for(int i=0;i<mem_arr.size();i++){
            if(mem_arr.get(i).getSet_or_not().equals("是")) {
                set_num_arr.add(mem_arr.get(i).getPhone_num());
            }
        }
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
            UiSettings settings = aMap.getUiSettings();
            aMap.setLocationSource(this);//设置了定位的监听,这里要实现LocationSource接口
            // 是否显示定位按钮
            settings.setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
        }
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
//设置定位回调监听
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                String address=date.toString()+'\n'+aMapLocation.getAddress();
                current_address=aMapLocation.getAddress();
                current_time=date.toString();
                if (isFirstLoc) {
                    Toast.makeText(getApplicationContext(),  address, Toast.LENGTH_LONG).show();
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(25));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    isFirstLoc = false;
                        windows(0);
                    mLocationListener.onLocationChanged(aMapLocation);
                    //获取定位信息
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败,请先设置权限", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        //mLocationClient.stopLocation();//停止定位
        mLocationClient.onDestroy();//销毁定位客户端。
        //销毁定位客户端之后，若要重新开启定位请重新New一个AMapLocationClient对象。
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
//        isFirstLoc = true;
    }

    @Override
    public void deactivate() {

    }
    protected void sendSMSMessage(String phoneNo) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        }
        String info = getIntent().getExtras().getString("personal_address");
        String message ="(紧急提醒！)定位时间："+current_time+"\n定位所在地点："+current_address+"\n[附加信息]"+info;
        String save="Position："+current_address;
        bwl_instance t=new bwl_instance();
        t.setInfo(save);
        t.setType("定位记录");
        t.setTime(current_time);
        t.setImportance("重要");
        t.setRem_time("×");
        db2.add_bwl(t);
        List<mem>temp=new ArrayList<mem>();
        temp=db.FindMemByPhone_num(phoneNo);
        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> texts = smsManager.divideMessage(message);
            for(String text : texts){
                smsManager.sendTextMessage(phoneNo, null, text, null, null);//分别发送每一条短信
                for(int i=0;i<temp.size();i++) {
                    Toast.makeText(getApplicationContext(), "紧急定位信息已经发送给" + temp.get(i).getName()+"("+phoneNo+")",
                            Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "短信发送失败.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void windows(int x) {
        try {
            String ys_info = getIntent().getExtras().getString("yanshi");
            if (ys_info.equals("no")) {
                Message msg = new Message();
                msg.what = 1;
                handle_send.sendMessage(msg);
                return;
            }
        }
        catch (Exception e){

        }
        if (x == 0) {
            alertDialog= new AlertDialog.Builder(this)
                    .setTitle("系统提示：")
                    .setMessage("确定发送紧急定位短信吗吗？（若10s后无确认将自动发送）")
                    .setPositiveButton("确认发送", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 1;
                            handle_send.sendMessage(msg);
                        }
                    })
                    .setNegativeButton("仅定位", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 0;
                            qx=true;
                            handle_send.sendMessage(msg);
                        }
                    })
                    .show();
            delayCloseController.setCloseFlags(1);             //设置信息标志位
            delayCloseController.timer.schedule(delayCloseController, 10000);   //启动定时器
        }
    }

}