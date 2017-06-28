package com.haocong.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.georgeyang.doublescroll.BaseListFragment;

/**
 * Created by yangsp on 2016/11/14.
 */
public class Fragment4 extends BaseListFragment {
    public ScrollView scrollView;
    private DB_all_Server db;
    private TextView bwl_1,bwl_2,bwl_3,bwl_4,bwl_5;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(cn.georgeyang.doublescroll.R.layout.layout_scrollview,null);
        bwl_1 = (TextView) view.findViewById(cn.georgeyang.doublescroll.R.id.bwl_1);
        bwl_2 = (TextView) view.findViewById(cn.georgeyang.doublescroll.R.id.bwl_2);
        bwl_3 = (TextView) view.findViewById(cn.georgeyang.doublescroll.R.id.bwl_3);
        bwl_4 = (TextView) view.findViewById(cn.georgeyang.doublescroll.R.id.bwl_4);
        bwl_5 = (TextView) view.findViewById(cn.georgeyang.doublescroll.R.id.bwl_5);
        db=new DB_all_Server(this.getContext());
        scrollView = (ScrollView) view.findViewById(cn.georgeyang.doublescroll.R.id.scrollView);
        List<bwl_instance>arr=new ArrayList<bwl_instance>();
        String []data=new String[5];
        for(int i=0;i<5;i++){
            data[i]="";
        }
        try{
            arr=db.find_bwl_by_type("运动");
        }
        catch (Exception e){

        }
        try{
            for(int i=0;i<5;i++){
                data[i]="["+arr.get(i).getImportance()+"]Time："+arr.get(i).getTime()+" To do："+arr.get(i).getInfo();
            }
        }
        catch (Exception e){

        }
        bwl_1.setText(data[0]);
        bwl_2.setText(data[1]);
        bwl_3.setText(data[2]);
        bwl_4.setText(data[3]);
        bwl_5.setText(data[4]);
        return view;
    }

    @Override
    public View getSlidableView() {
        return scrollView;
    }
}
