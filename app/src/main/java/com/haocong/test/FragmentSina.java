package com.haocong.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.georgeyang.doublescroll.BaseListFragment;
import cn.georgeyang.doublescroll.DSRefView;

/**
 * Created by george.yang on 16/11/15.
 */

public class FragmentSina extends BaseListFragment {
    private DSRefView layout;
    private SwipeRefreshLayout refreshLayout;
    private mem_DBServer db;
    private DB_all_Server db2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(cn.georgeyang.doublescroll.R.layout.layout_sina,null);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(cn.georgeyang.doublescroll.R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                },3000);
            }
        });
        db=new mem_DBServer(this.getContext());
        db2=new DB_all_Server(this.getContext());
        layout = (DSRefView) view.findViewById(cn.georgeyang.doublescroll.R.id.layout);
        ListView listView = (ListView) layout.findViewById(cn.georgeyang.doublescroll.R.id.listView);
        String[] data = new String[100];
        for (int i = 0; i < 100; i++) {
            data[i] = "";
        }
        List<mem>arr=new ArrayList<mem>();
        List<bwl_instance>arr2=new ArrayList<bwl_instance>();
        List<mem>arr3=new ArrayList<mem>();
        List<bwl_instance>arr4=new ArrayList<bwl_instance>();
        try{
            arr=db.FindMemBySet_or_not("是");
        }
        catch (Exception e){

        }
        try{
            arr2=db2.get_bwl();
        }
        catch (Exception e){

        }
        data[0]="是否开启GPS定位：  √";
        data[1]="是否开启紧急联系人短信通知：  ";
        if(arr.size()>0){
            data[1]+="√";
        }
        else{
            data[1]+="×";
        }
        data[2]="是否开启备忘录提醒：  ";
        if(arr2.size()>0)
            data[2]+="√";
        else
            data[2]+="×";
        data[3]="是否已设置加速度阈值：  ";
        try{
            arr3=db.FindMemByName("加速度阈值");
        }
        catch (Exception e){

        }
        if(arr3.size()>0) {
            data[3] += "√";
            data[4]+="加速度阈值为："+arr3.get(0).getPhone_num();
        }
        else {
            data[3] += "×";
            data[4]+="默认加速度阈值为：10";
        }
        try {
            arr4 = db2.find_bwl_by_type("定位记录");
        }
        catch (Exception e){

        }
        if(arr4.size()==0){
            data[6]+="暂无历史定位记录";
        }
        else {
            data[6] += "历史定位记录如下";
            for(int i=0;i<arr4.size();i+=2){
                int tt=i/2;
                data[7+i]+=arr4.get(tt).getTime();
                data[7+i+1]+=arr4.get(tt).getInfo();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        layout.setSlideableView(listView);
        return view;
    }


    @Override
    public View getSlidableView() {
        return layout;
    }
}
