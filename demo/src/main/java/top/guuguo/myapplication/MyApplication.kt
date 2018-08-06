package top.guuguo.myapplication

import android.support.v4.content.ContextCompat
import com.guuguo.android.lib.BaseApplication
import com.guuguo.android.lib.utils.LogUtil

/**
 * mimi 创造于 2017-07-20.
 * 项目 androidLib
 */
class MyApplication : BaseApplication() {
    companion object {
        val instance by lazy {
            BaseApplication.get() as MyApplication
        }
    }

    override fun init() {
        LogUtil.init(true)
    }
}