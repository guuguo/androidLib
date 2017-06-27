package com.guuguo.android.lib.app

import android.app.Activity
import java.util.*

/**
 * mimi 创造于 2017-04-12.
 * 项目 order
 */

object ActivityManager {

    //退出栈顶Activity
    private var activityStack: Stack<Activity> = Stack<Activity>()

    fun popActivity(): Activity {
        return activityStack.pop()
    }

    fun popActivity(activity: Activity) {
        activityStack.remove(activity)
    }

    //将当前Activity推入栈中
    fun pushActivity(activity: Activity) {
        activityStack.push(activity)
    }

    //退出栈中所有Activity
    fun popAllActivity() {
        while (!activityStack.empty()) {
            popActivity().finish()
        }
    }
}

