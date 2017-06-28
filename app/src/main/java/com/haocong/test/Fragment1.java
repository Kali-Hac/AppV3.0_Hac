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
public class Fragment1 extends BaseListFragment {
    public ScrollView scrollView;
    private DB_all_Server db;
    private TextView bwl_11,bwl_22,bwl_33,bwl_44,bwl_55;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(cn.georgeyang.doublescroll.R.layout.layout_scrollview2,null);
        bwl_11 = (TextView) view.findViewById(cn.georgeyang.doublescroll.R.id.bwl_11);
        bwl_22 = (TextView) view.findViewById(cn.georgeyang.doublescroll.R.id.bwl_22);
        bwl_33 = (TextView) view.findViewById(cn.georgeyang.doublescroll.R.id.bwl_33);
        bwl_44 = (TextView) view.findViewById(cn.georgeyang.doublescroll.R.id.bwl_44);
        bwl_55 = (TextView) view.findViewById(cn.georgeyang.doublescroll.R.id.bwl_55);
        db=new DB_all_Server(this.getContext());
        scrollView = (ScrollView) view.findViewById(cn.georgeyang.doublescroll.R.id.scrollView2);
        List<bwl_instance>arr=new ArrayList<bwl_instance>();
        String []data=new String[5];
        for(int i=0;i<5;i++){
            data[i]="";
        }
        try{
            arr=db.find_bwl_by_type("健康");
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
        bwl_11.setText(data[0]);
        bwl_22.setText(data[1]);
        bwl_33.setText(data[2]);
        bwl_44.setText(data[3]);
        bwl_55.setText(data[4]);
        return view;
    }

    @Override
    public View getSlidableView() {
        return scrollView;
    }
}
