package com.guuguo.android.lib.databinding

import android.databinding.BindingAdapter
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import com.guuguo.android.lib.extension.getColorCompat
import com.guuguo.android.lib.widget.roundview.*


@BindingAdapter("app:rv_backgroundColor")
fun RoundTextView.setRvBackGroundColor(color: Int) {
    this.delegate.backgroundColor = color
}
@BindingAdapter("app:rv_strokeColor")
fun RoundTextView.setRvStrokeColor(color: Int) {
    this.delegate.strokeColor = color
}
@BindingAdapter("app:rv_backgroundColorRes")
fun RoundTextView.setRvBackGroundColorRes(@ColorRes color: Int) {
    this.delegate.backgroundColor = context.getColorCompat(color)
}

@BindingAdapter("app:textColorRes")
fun RoundTextView.setCustomTextColor(@ColorRes color: Int) {
    this.setTextColor(context.getColorCompat(color))
}

@BindingAdapter("app:rv_backgroundColor")
fun RoundBgImageView.setRvBackGroundColor(color: Int) {
    this.delegate.backgroundColor = color
}

@BindingAdapter("app:rv_backgroundColor")
fun RoundConstraintLayout.setRvBackGroundColor(color: Int) {
    this.delegate.backgroundColor = color
}

@BindingAdapter("app:rv_backgroundColor")
fun RoundLinearLayout.setRvBackGroundColor(color: Int) {
    this.delegate.backgroundColor = color
}

@BindingAdapter("app:rv_backgroundColor")
fun RoundFrameLayout.setRvBackGroundColor(color: Int) {
    this.delegate.backgroundColor = color
}
