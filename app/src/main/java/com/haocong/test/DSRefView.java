package com.haocong.test;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import cn.georgeyang.doublescroll.DoubleScrollView;
import cn.georgeyang.doublescroll.ViewUtil;

/**
 * Created by george.yang on 16/11/16.
 */
public class DSRefView extends FrameLayout {
    public DSRefView(Context context) {
        super(context);
    }

    public DSRefView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DSRefView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DSRefView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate(getContext(), cn.georgeyang.doublescroll.R.layout.layout_dsview,this);
        imgScrollToTop = (ImageView) findViewById(cn.georgeyang.doublescroll.R.id.imgScrollToTop);
        imgScrollToTop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slideableView!=null) {
                    ViewUtil.scrollToTop(slideableView);
                }
                if (parentScrollView!=null) {
                    ViewUtil.scrollToTop(parentScrollView);
                }
                v.setVisibility(GONE);
            }
        });
        if (getContext() instanceof Activity) {
            Activity activity = (Activity) getContext();
            parentScrollView = ViewUtil.findView(activity.getWindow().getDecorView(), cn.georgeyang.doublescroll.DoubleScrollView.class);
        } else {
            parentScrollView = ViewUtil.findView(getRootView(), cn.georgeyang.doublescroll.DoubleScrollView.class);
        }
    }

    private View slideableView;
    private ImageView imgScrollToTop;
    public boolean isReleaseTouch () {
        return imgScrollToTop.getVisibility()==GONE;
    }

    public void setSlideableView (View view) {
        slideableView = view;
    }

    private DoubleScrollView parentScrollView;


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                imgScrollToTop.setVisibility(VISIBLE);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
