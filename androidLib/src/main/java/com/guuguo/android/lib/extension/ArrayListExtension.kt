package com.guuguo.android.lib.extension

import java.util.*

/**
 * Created by 大哥哥 on 2016/10/21 0021.
 */

fun <T> List<T>?.safe(): List<T> {
    if (this == null)
        return ArrayList()
    else {
        return this;
    }
}
