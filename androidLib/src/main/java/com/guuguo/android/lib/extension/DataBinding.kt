package com.guuguo.android.lib.extension

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * mimi 创造于 2017/9/11.
 * 项目 androidLib
 */
/**
 * Extension method to simplify view binding.
 */
fun <T : ViewDataBinding> View.dataBind() = DataBindingUtil.bind<T>(this) as T
fun <T : ViewDataBinding> Activity.dataBind(resId: Int) = DataBindingUtil.setContentView<T>(this, resId)
fun <T : ViewDataBinding> Int.dataBind(inflater: LayoutInflater, viewGroup: ViewGroup? = null, attachToParent: Boolean = false)
        = DataBindingUtil.inflate<T>(inflater, this, viewGroup, attachToParent)