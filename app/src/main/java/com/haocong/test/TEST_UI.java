package com.haocong.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.ljs.lovelyprogressbar.LovelyProgressBar;

/**
 * Created by Dell-User on 2017/6/17.
 */

public class TEST_UI extends Activity {
    LovelyProgressBar mloadbar;
    int progress = 0;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)
            {
                Intent intent=new Intent();
                intent.putExtra("type","定位");
                intent.setClass(TEST_UI.this,LocationActivity.class);
                TEST_UI.this.startActivity(intent);
            }
            else if(msg.what==1)
            {
                Intent intent=new Intent();
                intent.setClass(TEST_UI.this,LocationActivity.class);
                TEST_UI.this.startActivity(intent);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_ui);
        mloadbar = (LovelyProgressBar) findViewById(R.id.loadbar);
        progress=0;
        mloadbar.startload();
        new CountDownTimer(11000, 30) {

            @Override
            public void onTick(long millisUntilFinished) {
                mloadbar.setProgress(progress++);
            }

            @Override
            public void onFinish() {
                dingwei();
            }
        }.start();
        mloadbar.setOnLoadListener(new LovelyProgressBar.OnLoadListener() {
            @Override
            public void onAnimSuccess() {
                Toast.makeText(TEST_UI.this,"Success",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimError() {


            }
        });

    }
    public void dingwei(){
        new AlertDialog.Builder(this)
                .setTitle("提示：")
                .setMessage("进行定位将发送给已设置联系人紧急定位信息，确定？")
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Message msg =new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                })
                .setNegativeButton("仅定位",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Message msg =new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    }
                })
                .show();
    }

        }


