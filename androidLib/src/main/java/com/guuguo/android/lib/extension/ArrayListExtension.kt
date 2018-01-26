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

fun <K, V> Map<K, V>?.safe(): Map<K, V> {
    if (this == null)
        return HashMap()
    else {
        return this;
    }
}

fun <T> List<T>?.toListString(separate:String="  "): String {
    if(this==null)
        return ""
    val str = StringBuilder()
    forEach { str.append(it.toString()+separate) }
    return str.toString()
}
