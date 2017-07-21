package com.guuguo.android.lib.extension

import android.content.Context

/**
 * Created by 大哥哥 on 2016/10/21 0021.
 */

fun Context.getScreeWidth(): Int {
    return this.resources.displayMetrics.widthPixels
}

fun Context.getScreeHeight(): Int {
    return this.resources.displayMetrics.heightPixels
}
