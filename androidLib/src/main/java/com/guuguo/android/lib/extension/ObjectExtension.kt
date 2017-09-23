package com.guuguo.android.lib.extension

fun Object.getNameAndHashCode(): String {
    return javaClass.simpleName + "@" + Integer.toHexString(System.identityHashCode(this))
}