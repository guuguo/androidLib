package com.guuguo.android.lib.utils

import com.orhanobut.logger.Logger

/**
 * Created by mimi on 2017-01-02.
 */

object LogUtil {
    val FULL = com.orhanobut.logger.LogLevel.FULL
    val NONE = com.orhanobut.logger.LogLevel.NONE

    fun init(tag: String, debug: Boolean) {
        Logger.init(tag)                 // default PRETTYLOGGER or use just init()
                .methodCount(2)                 // default 2
                .hideThreadInfo()               // default shown
                .logLevel(if (debug) FULL else NONE)        // default LogLevel.FULL
                .methodOffset(0) //default AndroidLogAdapter
    }

    fun i(info: String) {
        Logger.i(info)
    }

    fun e(e: Throwable, msg: String) {
        Logger.e(e, msg)
    }
}
