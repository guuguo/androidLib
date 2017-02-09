package com.guuguo.android.lib.helper;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.guuguo.android.R;

/**
 * Created by guodeqing on 16/6/5.
 */
public class ToolBarHelper { /*上下文，创建view的时候需要用到*/
    private Context mContext; /*base com.hesheng.orderpad.view*/
    private ViewGroup mContentView; /*用户定义的view*/
    private View mUserView;
    /*mToolbar*/
    private Toolbar mToolBar; /*视图构造器*/
    private LayoutInflater mInflater;
    private AppBarLayout mAppBarView;

    public AppBarLayout getmAppBarView() {
        return mAppBarView;
    }


    public ToolBarHelper(Context context, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext); /*初始化整个内容*/
        mUserView = mInflater.inflate(layoutId, null);
    }

    public ToolBarHelper(Context context, int layoutId, int toolbarResId) {
        this(context, layoutId, toolbarResId, false);
    }

    public ToolBarHelper(Context context, int layoutId, int toolbarResId, boolean isOverlay) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext); /*初始化整个内容*/
        ViewGroup layout;
        initContentView();
        if (!isOverlay)/*初始化用户定义的布局*/ {
            initToolBar(mContentView, toolbarResId);
            initUserView(mContentView, layoutId); /*初始化toolbar*/
//            layout = initLinearView();
//            initToolBar(layout, toolbarResId);
//            initUserView(layout, layoutId); /*初始化toolbar*/
        } else {
            initToolBar(mContentView, toolbarResId);
            initUserView(mContentView, layoutId); /*初始化toolbar*/
        }
    }

    private void initContentView() { /*直接创建一个布局，作为视图容器的父容器*/
        mContentView = new CoordinatorLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);
    }

    private LinearLayout initLinearView() { /*直接创建一个布局，作为视图容器的父容器*/
        LinearLayout mLLContentView = new LinearLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLLContentView.setOrientation(LinearLayout.VERTICAL);
        mLLContentView.setLayoutParams(params);
        mContentView.addView(mLLContentView);
        return mLLContentView;
    }

//    private RelativeLayout initRelativeView() { /*直接创建一个布局，作为视图容器的父容器*/
//        RelativeLayout mRlContentView = new RelativeLayout(mContext);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        mRlContentView.setLayoutParams(params);
//
//        mContentView.addView(mRlContentView);
//        return mRlContentView;
//    }

    private void initToolBar(ViewGroup parent, int resId) { /*通过inflater获取toolbar的布局文件*/
        View view = mInflater.inflate(resId, parent);
        mToolBar = (Toolbar) view.findViewById(R.id.id_tool_bar);
        mAppBarView = (AppBarLayout) view.findViewById(R.id.appbar);
//        parent.addView(view);
    }

    private void initUserView(ViewGroup parent, int id) {
        mUserView = mInflater.inflate(id, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        parent.addView(mUserView, params);
    }

    public View getContentView() {
        if (mContentView == null)
            return mUserView;
        else
            return mContentView;
    }


    public Toolbar getToolBar() {
        return mToolBar;
    }
}