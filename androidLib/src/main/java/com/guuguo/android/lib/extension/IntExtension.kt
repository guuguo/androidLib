package com.guuguo.android.lib.extension

import com.guuguo.android.lib.BaseApplication

/**
 * Created by 大哥哥 on 2016/10/21 0021.
 */


fun Int.pxToDp(): Int {
    return (this / BaseApplication.get().resources.displayMetrics.density + 0.5f).toInt()
}

fun Int.dpToPx(): Int {
    return (this * BaseApplication.get().resources.displayMetrics.density + 0.5f).toInt()
}

fun Int?.safe(): Int {
    if (this == null)
        return 0
    else return this
}