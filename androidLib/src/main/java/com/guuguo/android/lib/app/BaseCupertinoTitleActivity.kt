package com.guuguo.android.lib.app

import android.support.v7.widget.Toolbar
import android.view.ViewGroup
import android.widget.TextView
import com.guuguo.android.R

class BaseCupertinoTitleActivity : LBaseActivitySupport() {
    override fun getLayoutResId() = R.layout.base_activity_cupertino_simple_back
    override fun getToolBar(): Toolbar = findViewById(R.id.id_tool_bar)
    override fun getAppBar(): ViewGroup? = findViewById<ViewGroup>(R.id.appbar)
    override fun initToolBar() {
        super.initToolBar()
        supportActionBar?.title=""
    }

    override fun getHeaderTitle()=null
    override fun setTitle(title: CharSequence?) {
        findViewById<TextView>(R.id.tv_title_bar).setText(title)
    }

}