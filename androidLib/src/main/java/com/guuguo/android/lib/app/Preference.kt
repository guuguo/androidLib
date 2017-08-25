package com.guuguo.android.util

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.guuguo.android.lib.utils.Utils
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by 大哥哥 on 2016/10/15 0015.
 */
class Preference<T>(val context: Context, var default: T, var typeToken: TypeToken<T>? = null) : ReadWriteProperty<Any?, T> {

    companion object {
        var appName = Utils.getContext().packageName.replace('.', '_')
        var dateFormat = "yyyy-MM-dd hh:mm:ss"
        var gson = GsonBuilder().setDateFormat(dateFormat).create()
        fun init(appName: String) {
            this.appName = appName
        }
    }

    val prefs by lazy {
        context.getSharedPreferences(appName, Context.MODE_PRIVATE)
    }
    var mValue: T? = null
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (mValue == null)
            mValue = findPreference(property.name, default)
        return mValue!!
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (mValue != value)
            putPreference(property.name, value)
        mValue = value
    }

    private fun <U> findPreference(name: String, default: U): U = with(prefs) {
        val res: Any? = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> {
                if (typeToken != null) {
                    gson.fromJson<U>(getString(name, ""), typeToken!!.type)
                } else {
                    throw IllegalArgumentException("typeToken is null")
                }
            }
        }
        res as U
    }

    private fun putPreference(name: String, value: T) = with(prefs.edit())
    {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> {
                putString(name, gson.toJson(value))
            }
        }.apply()
    }
}