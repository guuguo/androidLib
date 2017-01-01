package com.guuguo.android.lib.helper;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guuguo.android.R;

/**
 * Created by guodeqing on 16/6/5.
 */
public class DrawerHelper { /*上下文，创建view的时候需要用到*/
    private Context mContext;
    /*base com.hesheng.orderpad.view*/
    private DrawerLayout mContentView;
    /*用户自定义view*/
    private View mUserView;
    /*视图构造器*/
    private LayoutInflater mInflater;
    private NavigationView mNavigationView;

    public DrawerHelper(Context context, View userContentView, int navigationViewResId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext); /*初始化整个内容*/
        initContentView();
        /*初始化用户定义的布局*/
        initUserView(userContentView);
        initDrawerView(navigationViewResId);
    }

    private void initContentView() {
        mContentView = new DrawerLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);
    }

    private void initDrawerView(int resId) {
        View mView = mInflater.inflate(resId, mContentView);
        mNavigationView = (NavigationView) mView.findViewById(R.id.nav_view);

    }

    private void initUserView(View content) {
        mUserView = content;
        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.addView(mUserView, params);

    }

    public DrawerLayout getContentView() {
        return mContentView;
    }

    public NavigationView getNavigationView() {
        return mNavigationView;
    }
}