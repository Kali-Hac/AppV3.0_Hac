package com.haocong.test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.georgeyang.doublescroll.BaseListFragment;


/**
 * Created by yangsp on 2016/11/14.
 */
public class SAdapter extends FragmentStatePagerAdapter {
    private List<BaseListFragment> list;
    private static final String[] titles = new String[]{"联系人","紧急","健康","运动","设置"};

    public SAdapter(FragmentManager fm) {
        super(fm);
        list = new ArrayList<>();
        list.add(new Fragment2_new());
        list.add(new Fragment3());
        list.add(new Fragment1());
        list.add(new Fragment4());
        list.add(new FragmentSina());
    }

    public View getSlidableView (int index) {
        return list.get(index).getSlidableView();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
