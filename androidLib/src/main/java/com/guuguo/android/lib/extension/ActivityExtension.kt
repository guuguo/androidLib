package com.guuguo.android.lib.extension

import android.app.Activity
import com.guuguo.android.lib.utils.LogUtil

/**
 * Created by 大哥哥 on 2016/10/21 0021.
 */

fun Activity.getScreeWidth(): Int {
    return this.resources.displayMetrics.widthPixels
}

fun Activity.getScreeHeight(): Int {
    return this.resources.displayMetrics.heightPixels
}

fun Activity.log(log: String) {
    LogUtil.i(log, javaClass.simpleName)
}