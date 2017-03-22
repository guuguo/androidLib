package com.guuguo.android.lib.net

import android.accounts.NetworkErrorException
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.guuguo.android.R
import com.guuguo.android.lib.BaseApplication
import com.guuguo.android.lib.utils.LogUtil
import com.guuguo.learnsave.extension.safe
import io.reactivex.SingleObserver
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * guodeqing 创造于 16/6/4.
 * 项目 youku
 */
abstract class LBaseCallback<T>(protected val dataType: TypeToken<T>) : SingleObserver <String> {

    companion object {
        val normalDate = "yyyy-MM-dd hh:mm:ss.0"
        val gson = GsonBuilder().setDateFormat(normalDate).create()
    }


    override fun onSuccess(t: String?) {
        gsonExchange(t.safe())
    }

    abstract fun gsonExchange(result: String)

    override fun onError(e: Throwable?) {
        if (e != null) {
            LogUtil.e(e, e.message.safe())
            when (e) {
                is SocketTimeoutException -> onApiLoadError(BaseApplication.getInstance().getString(R.string.state_network_timeout))
                is NetworkErrorException -> onApiLoadError(BaseApplication.getInstance().getString(R.string.state_network_error))
                is UnknownHostException -> onApiLoadError(BaseApplication.getInstance().getString(R.string.state_network_unknown_host))
                is ConnectException -> onApiLoadError(BaseApplication.getInstance().getString(R.string.state_network_unknown_host))
                is IOException -> onApiLoadError(e.message.safe())
                else -> throw e
            }
        }
    }

    abstract fun onApiLoadSuccess(model: T?)

    abstract fun onApiLoadError(msg: String)
}
