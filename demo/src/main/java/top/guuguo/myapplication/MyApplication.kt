package top.guuguo.myapplication

import com.guuguo.android.lib.BaseApplication

/**
 * mimi 创造于 2017-07-20.
 * 项目 androidLib
 */
class MyApplication : BaseApplication() {
    companion object {
        val instance by lazy {
            BaseApplication.INSTANCE as MyApplication
        }
    }

    override fun init() {
    }
}