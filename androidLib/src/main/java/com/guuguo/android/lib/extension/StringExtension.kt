package com.guuguo.android.lib.extension

import com.guuguo.android.lib.BaseApplication
import com.guuguo.android.lib.utils.LogUtil
import java.util.regex.Pattern

/**
 * Created by mimi on 2016-11-17.
 */

fun String?.safe(default: String = ""): String {
    if (this == null)
        return default;
    else return this;
}

fun String?.toast(): String? {
    if (!isNullOrEmpty())
        BaseApplication.get().toast(this)
    return this
}

fun String?.log(tag: String = ""): String? {
    if (!isNullOrEmpty())
        if (tag.isNullOrEmpty())
            LogUtil.i(this!!)
        else LogUtil.i(tag, this!!)

    return this
}

fun String?.isEmail(): Boolean {
    val str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
    val p = Pattern.compile(str)
    val m = p.matcher(this.safe())
    return m.matches()
}