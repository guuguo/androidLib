package com.guuguo.learnsave.extension

import android.content.Context
import com.guuguo.android.lib.BaseApplication

/**
 * Created by 大哥哥 on 2016/10/21 0021.
 */


fun Int.pxToDp(): Int {
    return (this / BaseApplication.getInstance().resources.displayMetrics.density + 0.5f).toInt()
}

fun Int.dpToPx(): Int {
    return (this * BaseApplication.getInstance().resources.displayMetrics.density + 0.5f).toInt()
}

@Deprecated("已经过时了", ReplaceWith("dpToPx()"))
fun Int.pxToDp(context: Context): Int {
    return (this / context.resources.displayMetrics.density + 0.5f).toInt()
}

@Deprecated("已经过时了", ReplaceWith("dpToPx()"))
fun Int.dpToPx(context: Context): Int {
    return (this * context.resources.displayMetrics.density + 0.5f).toInt()
}
