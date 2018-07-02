package com.guuguo.android.lib.widget.bindingAdapter

import android.databinding.BindingAdapter
import android.databinding.BindingMethod
import android.databinding.BindingMethods
import android.support.annotation.RestrictTo
import android.view.View
import android.view.ViewTreeObserver

import com.guuguo.android.lib.widget.FunctionTextView


@BindingMethods(BindingMethod(type = FunctionTextView::class, attribute = "android:text", method = "setText"))
class FunctionTextViewBindingAdapter

// 根据View的高度和宽高比，设置高度
@BindingAdapter("heightWidthRatio")
fun View.setWidthHeightRatio(ratio: Double) {
    this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val width = this@setWidthHeightRatio.width
            if (width > 0) {
                this@setWidthHeightRatio.layoutParams.height = (width * ratio).toInt()
                this@setWidthHeightRatio.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })
}