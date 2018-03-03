package com.guuguo.android.lib.widget.simpleview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.guuguo.android.R

//import com.wang.avi.AVLoadingIndicatorView

class SimpleView {

    val view: View
    val viewHolder: SimpleViewHolder

    var drawable: Drawable? = null
    constructor(context: Context) : this(context, null)
    constructor(context: Context, viewGroup: ViewGroup?) {
        view = View.inflate(context, R.layout.base_include_simple_empty_view, viewGroup)
        viewHolder = SimpleViewHolder(view)
    }
}