package com.guuguo.android.lib.lifecycle

import android.app.Application
import com.guuguo.android.lib.utils.Utils

/**
 * Project: androidLib
 *
 * @author : guuguo
 * @since 2018/8/21
 */
object AppHelper {
    lateinit var app: Application
        private set

    fun init(app: Application) {
        AppHelper.app = app
        initLyfecycle()
        Utils.init(app)
    }

    lateinit var mActivityLifecycle: ActivityLifecycle

    fun getCurrentActivity() = mActivityLifecycle.mActivityList.lastOrNull()

    private fun initLyfecycle() {
        mActivityLifecycle = ActivityLifecycle()
        app.registerActivityLifecycleCallbacks(mActivityLifecycle)
    }

}