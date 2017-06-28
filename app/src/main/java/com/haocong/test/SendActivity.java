package com.haocong.test;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import android.view.View.OnClickListener;
/**
 * Created by Dell-User on 2017/6/8.
 */

public class SendActivity extends Activity {
//    private int mem_id;
//    private String name;
//    private String phone_num;
//    private String phone_type;
//    private String set_or_not;
    private int phone_type_Pos,set_or_not_Pos,search_type_Pos;
    private String set_num;
    private EditText  mem_id, name, phone_num,search_msg,send_info;
    private mem_DBServer db;
    private TextView display_mem;
    private Button btn_send, get_mem, del_mem, add_mem, mod_mem, get_all_mem,send_button,help;
    private Spinner phone_type_Spin, set_or_not_Spin,search_type_Spin;
    private  ArrayAdapter<String> set_or_not_Adapter = null,phone_type_Adapter=null,search_type_Adapter=null;
    private String[]set_or_not_items=new String[]{"是","否"};
    private String[]phone_type_items=new String[]{"手机","电话"};
    private String[]search_type_items=new String[]{"编号","是否设置","姓名","号码","方式"};
    private Handler add_handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)
            {

            }
            else if(msg.what==1)
            { mem t=new mem();
                String temp_name=name.getText().toString();
                String temp_phone_num=phone_num.getText().toString();
                t.setPhone_type(phone_type_items[phone_type_Pos]);
                t.setSet_or_not(set_or_not_items[set_or_not_Pos]);
                t.setName(temp_name);
                t.setPhone_num(temp_phone_num);
                db.add_mem(t);
                init();
                Toast.makeText(SendActivity.this, "成功添加联系人!", Toast.LENGTH_LONG).show();
                if(set_or_not_Pos==0){
                    set_num=temp_phone_num;
                    sendSMSMessage("","");
                }
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
                int i=Integer.parseInt(mem_id.getText().toString());
                try {
                    db.del_mem(i);
                    Toast.makeText(SendActivity.this, "成功删除联系人!", Toast.LENGTH_LONG).show();
                    init();
                }
                catch (Exception e){
                    Toast.makeText(SendActivity.this, "编号数据不存在，删除联系人失败!", Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    private Handler mod_handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)
            {

            }
            else if(msg.what==1)
            {
                String temp_mem_id=mem_id.getText().toString();
                mem t = new mem();
                t.setPhone_type(phone_type_items[phone_type_Pos]);
                t.setSet_or_not(set_or_not_items[set_or_not_Pos]);
                t.setName(name.getText().toString());
                t.setPhone_num(phone_num.getText().toString());
                int i=Integer.parseInt(temp_mem_id);
                t.setMem_id(i);
                db.modify_mem(t);
                Toast.makeText(SendActivity.this, "成功修改联系人!", Toast.LENGTH_LONG).show();
                init();
            }
        }
    };
    private Handler send_handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)
            {

            }
//            "编号","是否设置","姓名","号码","方式"
            else if(msg.what==1)
            {
                String search_info=search_msg.getText().toString();
                String infos=send_info.getText().toString();
                mem t=new mem();
                List<mem>arr=new ArrayList<mem>();
                try{
                    display_mem.setText("");
                    if(search_type_Pos==0) {
                        int i = Integer.parseInt(search_info);
                        t = db.FindMemById(i);
                        sendSMSMessage(t.getPhone_num(),infos);
                        Toast.makeText(SendActivity.this, "成功发送短信给->"+t.getName(), Toast.LENGTH_LONG).show();
                    }
                    else if(search_type_Pos==1){
                        String data="";
                        arr=db.FindMemBySet_or_not(search_info);
                        for(int i=0;i<arr.size();i++){
                            sendSMSMessage(arr.get(i).getPhone_num(),infos);
                            Toast.makeText(SendActivity.this, "成功发送短信给->"+arr.get(i).getName(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else if(search_type_Pos==2){
                        String data="";
                        arr=db.FindMemByName(search_info);
                        for(int i=0;i<arr.size();i++){
                            sendSMSMessage(arr.get(i).getPhone_num(),infos);
                            Toast.makeText(SendActivity.this, "成功发送短信给->"+arr.get(i).getName(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else if(search_type_Pos==3){
                        String data="";
                        arr=db.FindMemByPhone_num(search_info);
                        for(int i=0;i<arr.size();i++){
                            sendSMSMessage(arr.get(i).getPhone_num(),infos);
                            Toast.makeText(SendActivity.this, "成功发送短信给->"+arr.get(i).getName(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else if(search_type_Pos==4){
                        String data="";
                        arr=db.FindMemByPhone_type(search_info);
                        for(int i=0;i<arr.size();i++){
                            sendSMSMessage(arr.get(i).getPhone_num(),infos);
                            Toast.makeText(SendActivity.this, "成功发送短信给->"+arr.get(i).getName(), Toast.LENGTH_LONG).show();
                        }
                    }
                    Toast.makeText(SendActivity.this, "所有短信发送成功~", Toast.LENGTH_LONG).show();
                }
                catch (Exception e) {
                    Toast.makeText(SendActivity.this, "没有找到符合条件的联系人!", Toast.LENGTH_LONG).show();
                }
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_msg);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }
        db=new mem_DBServer(this);
        mem_id = (EditText) this.findViewById(R.id.mem_id);
        name = (EditText) this.findViewById(R.id.name);
        search_msg = (EditText) this.findViewById(R.id.search_msg);
        phone_num = (EditText) this.findViewById(R.id.phone_num);
        send_info=(EditText)this.findViewById(R.id.send_info);
        send_button=(Button) this.findViewById(R.id.send_button);
//        txt_content = (EditText) this.findViewById(R.id.txt_content);
//        btn_send = (Button) this.findViewById(R.id.btn_send);
        get_mem = (Button) this.findViewById(R.id.get_mem);
        display_mem = (TextView) this.findViewById(R.id.display_mem);
        get_all_mem = (Button) this.findViewById(R.id.get_all_mem);
        add_mem=(Button) this.findViewById(R.id.add_mem);
        help=(Button)this.findViewById(R.id.help);
        del_mem = (Button) this.findViewById(R.id.del_mem);
        mod_mem = (Button) this.findViewById(R.id.mod_mem);
        phone_type_Spin=(Spinner)this.findViewById(R.id.phone_type);
        set_or_not_Spin=(Spinner)this.findViewById(R.id.set_or_not);
        search_type_Spin=(Spinner)this.findViewById(R.id.search_type);
        phone_type_Adapter = new ArrayAdapter<String>(SendActivity.this,
                android.R.layout.simple_spinner_item,phone_type_items);
        set_or_not_Adapter = new ArrayAdapter<String>(SendActivity.this,
                android.R.layout.simple_spinner_item,set_or_not_items);
       search_type_Adapter = new ArrayAdapter<String>(SendActivity.this,
                android.R.layout.simple_spinner_item,search_type_items);
        set_or_not_Spin.setAdapter(set_or_not_Adapter);
        phone_type_Spin.setAdapter(phone_type_Adapter);
        search_type_Spin.setAdapter(search_type_Adapter);
        search_type_Spin.setSelection(0,true);
        phone_type_Spin.setSelection(0,true);
        set_or_not_Spin.setSelection(0,true);
        set_or_not_Spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {

               set_or_not_Pos= position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                set_or_not_Pos=0;
            }

        });
        phone_type_Spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {

                phone_type_Pos= position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                phone_type_Pos=0;
            }

        });
        search_type_Spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
//                private String[]search_type_items=new String[]{"编号","是否设置","姓名","号码","方式"};
                search_type_Pos= position;
                if(position==0) search_msg.setText("1");
                else if(position==1) search_msg.setText("是");
                else if(position==2) search_msg.setText("");
                else if(position==3) search_msg.setText("");
                else if(position==4) search_msg.setText("手机");
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                search_type_Pos=0;
            }

        });
        add_mem.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mem t=new mem();
                String temp_name=name.getText().toString();
                String temp_phone_num=phone_num.getText().toString();
                if(checkInfo(temp_name)&&checkInfo(temp_phone_num)) {
                   windows(1);
                }
                else{
                    Toast.makeText(SendActivity.this, "联系人信息填写不完整，无法添加", Toast.LENGTH_LONG).show();
                }
                }
        });
        del_mem.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
             windows(2);
            }
        });
        get_mem.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//                private String[]search_type_items=new String[]{"编号","是否设置","姓名","号码","方式"};
               String search_info=search_msg.getText().toString();
                mem t=new mem();
                List<mem>arr=new ArrayList<mem>();
                    try{
                        display_mem.setText("");
                        if(search_type_Pos==0) {
                            int i = Integer.parseInt(search_info);
                            t = db.FindMemById(i);
                            display_mem.setText(t.toString());
                        }
                        else if(search_type_Pos==1){
                            String data="";
                            arr=db.FindMemBySet_or_not(search_info);
                            for(int i=0;i<arr.size();i++){
                                data+="\n"+arr.get(i).toString();
                            }
                            display_mem.setText(data);
                        }
                        else if(search_type_Pos==2){
                            String data="";
                            arr=db.FindMemByName(search_info);
                            for(int i=0;i<arr.size();i++){
                                data+="\n"+arr.get(i).toString();
                            }
                            display_mem.setText(data);
                        }
                        else if(search_type_Pos==3){
                            String data="";
                            arr=db.FindMemByPhone_num(search_info);
                            for(int i=0;i<arr.size();i++){
                                data+="\n"+arr.get(i).toString();
                            }
                            display_mem.setText(data);
                        }
                        else if(search_type_Pos==4){
                            String data="";
                            arr=db.FindMemByPhone_type(search_info);
                            for(int i=0;i<arr.size();i++){
                                data+="\n"+arr.get(i).toString();
                            }
                            display_mem.setText(data);
                        }
                    }
                    catch (Exception e) {
                        Toast.makeText(SendActivity.this, "没有找到符合条件的联系人!", Toast.LENGTH_LONG).show();
                    }
                Toast.makeText(SendActivity.this, "成功查找联系人!", Toast.LENGTH_LONG).show();
            }
        });
        mod_mem.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String temp_name=name.getText().toString();
                String temp_phone_num=phone_num.getText().toString();
                String temp_mem_id=mem_id.getText().toString();
                if(checkInfo(temp_mem_id)&&checkInfo(temp_name)&&checkInfo(temp_phone_num)){
                    windows(3);
                }
                else{
                    Toast.makeText(SendActivity.this, "联系人信息填写不完整，无法修改", Toast.LENGTH_LONG).show();//提示成功
                }
            }
        });
        send_button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String infos=send_info.getText().toString();
                String search_info=search_msg.getText().toString();
                if(checkInfo(infos)&&checkInfo(search_info)){
                    windows(4);
                }
                else{
                    Toast.makeText(SendActivity.this, "联系人信息不完整，无法发送短信！", Toast.LENGTH_LONG).show();//提示成功
                }
            }
        });
        get_all_mem.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                List<mem> arr=new ArrayList<mem>();
                arr=db.get_all_mem();
                String data="";
                for(int i=0;i<arr.size();i++){
                    data+="\n"+arr.get(i).toString();
                }
                display_mem.setText(data);
                Toast.makeText(SendActivity.this, "成功显示所有联系人信息!", Toast.LENGTH_LONG).show();//提示成功
            }
        });
        help.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
              windows(5);
            }
        });
    }
    private  void init(){
        mem_id.setText("");
        phone_type_Spin.setSelection(0,true);
        set_or_not_Spin.setSelection(0,true);
        name.setText("");
        phone_num.setText("");
        search_type_Spin.setSelection(0,true);
        search_msg.setText("");
    }
    protected void sendSMSMessage(String phoneNo,String message) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);//自定义的code
        }
        if(phoneNo.equals(""))
            phoneNo = set_num;
        if(message.equals(""))
            message = "您已被设置为此号码的紧急联系人,若有疑问可以回复此短信。";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> texts = smsManager.divideMessage(message);
            for (String text : texts) {
                smsManager.sendTextMessage(phoneNo, null, text, null, null);//分别发送每一条短信
            }
            Toast.makeText(getApplicationContext(), "紧急联系人确认短信已经发送出去",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "确认短信发送失败.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void windows(int x){
        if (x==1) {
            new AlertDialog.Builder(this)
                    .setTitle("系统提示：")
                    .setMessage("确定添加该联系人吗？（若设置为紧急联系人将会发送确认信息）")
                    .setPositiveButton("确认添加", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 1;
                            add_handler.sendMessage(msg);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 0;
                            add_handler.sendMessage(msg);
                        }
                    })
                    .show();
        }
        else if(x==2){
            new AlertDialog.Builder(this)
                    .setTitle("系统提示：")
                    .setMessage("确定删除该联系人吗？(一旦删除不可恢复)")
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
                    .setMessage("确定修改该联系人信息吗？(一旦修改不可恢复)")
                    .setPositiveButton("确认修改", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 1;
                            mod_handler.sendMessage(msg);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 0;
                            mod_handler.sendMessage(msg);
                        }
                    })
                    .show();
        }
        else  if(x==4){
            new AlertDialog.Builder(this)
                    .setTitle("系统提示：")
                    .setMessage("确定发送短信给指定联系人吗？(一旦发送无法撤回)")
                    .setPositiveButton("确认发送", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 1;
                            send_handler.sendMessage(msg);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Message msg = new Message();
                            msg.what = 0;
                            send_handler.sendMessage(msg);
                        }
                    })
                    .show();
        }
        else if(x==5){
            new  AlertDialog.Builder(this)
                    .setTitle("用户帮助" )
                    .setMessage("①添加联系人时不用填写编号(即第一个空不用)，需填写其它完整信息\n②查找联系人的方式有：通过编号、是否设置为" +
                            "紧急联系人、姓名、号码、号码方式来查询，需要在'查询所有'按钮前填写相关信息然后点击查询\n③修改联系人信息时，需要找到编号并填写(" +
                            "编号一定需要)\n④可以通过'查询所有'的方式来群发短信，点击'立即发送短信'即可群发制定联系人\n⑤点击查看所有联系人可以查看所有联系人详细信息")
                    .setPositiveButton("确定" ,  null )
                    .show();
        }
    }
}
