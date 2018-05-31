package com.guuguo.android.lib.utils.cache

import com.guuguo.android.lib.BaseApplication
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import java.io.Serializable

/** 转成带缓存的网络请求,Pair<T,Boolean> Boolean代表是否是从缓存读取的的结果*/
class ACacheTransform<T : Serializable>(var key: String) : ObservableTransformer<T, Pair<T, Boolean>> {
    val FROM_NET = 0
    val FROM_CACHE = 1
    val FROM_CACHE_AND_NET = 2
    var fromType = FROM_CACHE_AND_NET

    fun fromCache() = this.also { fromType = FROM_CACHE }
    fun fromNet() = this.also { fromType = FROM_NET }
    fun fromCacheAndNet() = this.also { fromType = FROM_CACHE_AND_NET }

    val aCache = ACache.get(BaseApplication.get())
    override fun apply(upstream: Observable<T>): Observable<Pair<T, Boolean>> {
        return Observable.create<Pair<T, Boolean>> { e ->

            val getFromNet = {
                upstream.subscribe({
                    aCache.put(key, it)
                    e.onNext(it to false)
                    e.onComplete()
                }, {
                    e.onError(it)
                })
            }
            val getFromCache = {
                val res = try {
                    aCache.getAsObject(key) as T
                } catch (e: Exception) {
                    null
                }
                if (res != null)
                    e.onNext(res to true)
            }
            when (fromType) {
                FROM_NET -> getFromNet()
                FROM_CACHE -> getFromCache()
                FROM_CACHE_AND_NET -> {
                    getFromNet()
                    getFromCache()
                }
            }

        }
    }
}
