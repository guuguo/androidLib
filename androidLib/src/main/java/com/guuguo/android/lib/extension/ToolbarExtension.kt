package com.guuguo.android.lib.extension

import android.app.Activity
import android.support.v7.widget.Toolbar
import com.guuguo.android.R

/**
 * Created by 大哥哥 on 2016/10/24 0024.
 */

fun Toolbar.initNav(activity:Activity) {
    setNavigationIcon(R.drawable.abc_ic_arrow_drop_right_black_24dp)
    setNavigationOnClickListener { activity.onBackPressed() }
}

