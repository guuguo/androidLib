package com.guuguo.android.lib

import android.app.Application
import com.guuguo.android.lib.utils.ToastUtil
import com.guuguo.android.lib.utils.Utils
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers


/**
 * Created by guodeqing on 16/3/7.
 */
abstract class BaseApplication : Application() {

    override fun onCreate() {
        Utils.init(this)
        INSTANCE = this
        init()
        super.onCreate()
    }

    protected abstract fun init()

    fun toast(msg: String, isShortToast: Boolean = true) {
        Completable.create { e ->
            if (isShortToast)
                ToastUtil.showSingletonToast(msg)
            else
                ToastUtil.showSingleLongToast(msg)
            e.onComplete()
        }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    companion object {

         lateinit var INSTANCE: BaseApplication

        fun get(): BaseApplication {
            return INSTANCE
        }
    }

}
