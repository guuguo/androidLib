package com.guuguo.android.lib.net

import android.accounts.NetworkErrorException
import android.text.TextUtils
import com.guuguo.android.lib.net.LBaseCallback.Companion.gson
import com.guuguo.android.lib.utils.NetWorkUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by guodeqing on 16/3/10.
 */
object ApiServer {
    var mOkHttpClient = OkHttpClient.Builder().connectTimeout(7, TimeUnit.SECONDS).build()
    fun apiPost(httpUrl: String, imgUrls: String, headers: HashMap<String, String>, formBodyParams: HashMap<String, String>?, jsonBodyParams: HashMap<String, String>?, callbackL: LBaseCallback<*>) {
        val request: Request
        val builder: Request.Builder
        if (formBodyParams != null && formBodyParams.isNotEmpty()) {
            val lBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
            for ((key, value) in formBodyParams) {
                lBodyBuilder.addFormDataPart(key, value + "")
            }
            val img = File(imgUrls)
            if (!TextUtils.isEmpty(imgUrls))
                lBodyBuilder.addFormDataPart("photo", img.name, RequestBody.create(MediaType.parse("image/png"), img))
            // mImgUrls为存放图片的url集合
            builder = Request.Builder()
            builder.post(lBodyBuilder.build())
            builder.url(httpUrl)
        } else {
            builder = Request.Builder()
        }
        if (jsonBodyParams != null && jsonBodyParams.isNotEmpty())
            builder.post(RequestBody.create(
                    MediaType.parse("application/json; charset=UTF-8"),
                    gson.toJson(jsonBodyParams)))// post json提交  
        for ((key, value) in headers) {
            builder.addHeader(key, value + "")
        }
        request = builder.url(httpUrl)
                .build()
        return executeRequest(callbackL, request)
    }

    fun apiPostWithImg(httpUrl: String, imgUrls: String, hashMap: HashMap<String, String>, callbackL: LBaseCallback<*>) {
        apiPost(httpUrl, imgUrls, HashMap(), hashMap, null, callbackL)
    }

    fun apiGet(httpUrl: String, hashMap: HashMap<String, String>, callbackL: LBaseCallback<*>) {

        val builder = Request.Builder()
        builder.get().url(jointURL(httpUrl, hashMap))
        val request = builder.build()
        return executeRequest(callbackL, request)
    }

    private fun executeRequest(callbackL: LBaseCallback<*>, request: Request) {
        val call = mOkHttpClient.newCall(request)
        return Single.create<String> { emiter ->
            if (!NetWorkUtil.isNetworkAvailable()) {
                emiter.onError(NetworkErrorException())
            } else {
                try {
                    emiter.onSuccess(call.execute().body()!!.string())
                } catch (e: Exception) {
                    if (!emiter.isDisposed)
                        emiter.onError(e)
                }
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(callbackL)
    }

    /**
     * 为HttpGet请求拼接多个参数

     * @author wangsong 2015-10-9
     * *
     * @param url
     * *
     * @param name
     * *
     * @param value
     */
    private fun jointURL(url: String, values: Map<String, String>): String {
        if (values.isEmpty())
            return url;
        val result = StringBuffer()
        result.append(url).append("?")
        val keys = values.keys
        for (key in keys) {
            result.append(key).append("=").append(values[key]).append("&")
        }
        return result.toString().substring(0, result.toString().length - 1)
    }


}
