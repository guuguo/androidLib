package com.guuguo.android.lib.utils

import android.app.Activity
import java.util.*

/**
 * mimi 创造于 2017-04-12.
 * 项目 order
 */

object ScreenManager {

    //退出栈顶Activity
    private var activityStack: Stack<Activity> = Stack<Activity>()

    fun popActivity(activity: Activity?) {
        if (activity != null) {
            activity.finish()
            activityStack.remove(activity)
        }
        
    }

    //获得当前栈顶Activity

    fun currentActivity(): Activity? {
        if (activityStack.size == 0)
            return null
        val activity = activityStack.lastElement()
        return activity
    }

    //将当前Activity推入栈中
    fun pushActivity(activity: Activity) {
        activityStack.add(activity)
    }

    //退出栈中所有Activity
    fun popAllActivityExceptOne(cls: Class<*>) {
        while (true) {
            val activity = currentActivity() ?: break
            popActivity(activity)
        }
    }
}

