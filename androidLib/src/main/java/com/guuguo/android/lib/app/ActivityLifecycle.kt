package com.guuguo.android.lib.app

import android.app.Activity
import android.app.Application
import android.os.Bundle

import java.util.LinkedList

/**
 * Created by Hiroshi on 2018/2/13.
 */

class ActivityLifecycle : Application.ActivityLifecycleCallbacks {

    val mActivityList: MutableList<Activity>

    init {
        mActivityList = LinkedList()
    }

    fun clear() {
        for (activity in mActivityList) {
            activity.finish()
        }
        mActivityList.clear()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {
        mActivityList.add(activity)
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        mActivityList.remove(activity)
    }

}
