package com.guuguo.android.lib.extension

import com.guuguo.android.lib.BaseApplication

/**
 * Created by mimi on 2016-11-17.
 */

fun String?.safe(): String {
    if (this == null)
        return "";
    else return this;
}
fun String?.toast(): String? {
    if (!isNullOrEmpty())
        BaseApplication.get().toast(this)
    return this
}