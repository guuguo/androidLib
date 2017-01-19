package com.guuguo.learnsave.extension

import android.content.Context

/**
 * Created by 大哥哥 on 2016/10/21 0021.
 */


fun Int.pxToDp(context: Context): Int {
    return (this / context.resources.displayMetrics.density + 0.5f).toInt()
}

fun Int.dpToPx(context: Context): Int {
    return (this * context.resources.displayMetrics.density + 0.5f).toInt()
}
