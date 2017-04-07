package com.guuguo.android.lib.helper

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.guuguo.android.R

/**
 * Created by guodeqing on 16/6/5.
 */
class ToolBarHelper { /*上下文，创建view的时候需要用到*/
    private var mContext: Context? = null /*base com.hesheng.orderpad.view*/
    private var mContentView: CoordinatorLayout? = null /*用户定义的view*/
    private var mUserView: View? = null
    /*mToolbar*/
    var toolBar: Toolbar? = null
        private set /*视图构造器*/
    private var mInflater: LayoutInflater? = null
    private var mAppBarView: AppBarLayout? = null

    fun getmAppBarView(): AppBarLayout? {
        return mAppBarView
    }


    constructor(context: Context, layoutId: Int) {
        this.mContext = context
        mInflater = LayoutInflater.from(mContext) /*初始化整个内容*/
        mUserView = mInflater!!.inflate(layoutId, null)
    }

    @JvmOverloads constructor(context: Context, layoutId: Int, toolbarResId: Int, isToolbarManual: Boolean = false) {
        this.mContext = context
        mInflater = LayoutInflater.from(mContext) /*初始化整个内容*/
        initContentView(layoutId, toolbarResId, isToolbarManual)

    }

    private fun initContentView(layoutId: Int, toolbarResId: Int, isToolbarManual: Boolean = false) { /*直接创建一个布局，作为视图容器的父容器*/
        mUserView = mInflater!!.inflate(layoutId, null, false)
        if (mUserView is CoordinatorLayout) {
            mContentView = mUserView as CoordinatorLayout
            initToolBar(mContentView!!, toolbarResId)
        } else {
            mContentView = CoordinatorLayout(mContext!!)
            val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            mContentView!!.layoutParams = params
            if (!isToolbarManual)
            /*初始化用户定义的布局*/ {
                initToolBar(mContentView!!, toolbarResId)
                initUserView(mContentView!!, layoutId) /*初始化toolbar*/
            } else {
                initManualBehaviorUserView(mContentView!!, layoutId)
                initToolBar(mContentView!!, toolbarResId)/*初始化toolbar*/
            }
        }

    }


    private fun initToolBar(parent: CoordinatorLayout, resId: Int) { /*通过inflater获取toolbar的布局文件*/
        val view = mInflater!!.inflate(resId, parent, false)
        toolBar = view.findViewById(R.id.id_tool_bar) as Toolbar
        mAppBarView = view.findViewById(R.id.appbar) as AppBarLayout
        parent.addView(view)
    }

    private fun initUserView(parent: CoordinatorLayout, id: Int) {
        val params = CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        params.behavior = AppBarLayout.ScrollingViewBehavior()
        parent.addView(mUserView, params)

    }

    private fun initManualBehaviorUserView(parent: CoordinatorLayout, id: Int) {
        mUserView = mInflater!!.inflate(id, null, false)
        val params = CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        parent.addView(mUserView, params)
    }

    val contentView: View?
        get() {
            if (mContentView == null)
                return mUserView
            else
                return mContentView
        }
}