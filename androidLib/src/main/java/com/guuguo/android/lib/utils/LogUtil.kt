package com.guuguo.android.lib.utils

import android.util.Log

/**
 * Created by mimi on 2017-01-02.
 */

object LogUtil {
    var debug = true
    private val TAG by lazy { AppUtil.getAppName() }
    fun init(debug: Boolean) {
        this.debug = debug
    }

    fun i(info: String) {
        Log.i(TAG, info)
    }

    fun d(info: String) {
        if (debug)
            Log.i(TAG, info)
    }

    fun e(msg: String, e: Throwable? = null) {
        if (debug)
            Log.e(TAG, msg, e)
    }

    fun w(msg: String, e: Throwable? = null) {
        if (debug)
            Log.w(TAG, msg, e)
    }
}
