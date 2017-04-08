package com.guuguo.learnsave.extension

import android.app.Activity
import android.content.Context
import com.guuguo.android.lib.BaseApplication

/**
 * Created by 大哥哥 on 2016/10/21 0021.
 */

fun Activity.getScreeWidth(): Int {
    return this.resources.displayMetrics.widthPixels
}

fun Activity.getScreeHeight(): Int {
    return this.resources.displayMetrics.heightPixels
}