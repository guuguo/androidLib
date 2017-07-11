package com.guuguo.android.lib.view.simpleview

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.guuguo.android.R

//import com.wang.avi.AVLoadingIndicatorView

class SimpleView {

    private var text: String = ""
    private var mOnClickListener: View.OnClickListener? = null
    private var mBtnText: String = ""
    private var isIconShow = true
    private var mIconRes = R.drawable.empty_cute_girl_box
    private var isWrapContent = true
    private var mStyle: Int = STYLE.normal

    val view: View
    val viewHolder: SimpleViewHolder

    constructor(context: Context) : this(context, null)

    private val drawable: DoubleCircleDrawable

    constructor(context: Context, viewGroup: ViewGroup?) {
        view = View.inflate(context, R.layout.simple_empty_view, viewGroup)
        viewHolder = SimpleViewHolder(view)
        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View?) {
                setUIBeforeShow()
            }

            override fun onViewDetachedFromWindow(p0: View?) {
            }

        })
        drawable = DoubleCircleDrawable(context.resources.displayMetrics.density)
    }

    object STYLE {
        val loading = 0
        val normal = 1
    }

    init {

    }

    fun style(style: Int): SimpleView {
        this.mStyle = style
        return this
    }


    fun text(text: String): SimpleView {
        this.text = text
        return this
    }

    fun invalidate() {
        view.invalidate()
    }

    fun btnText(text: String): SimpleView {
        this.mBtnText = text
        return this
    }

    fun btnListener(listener: View.OnClickListener?): SimpleView {
        this.mOnClickListener = listener
        return this
    }

    fun icon(iconRes: Int): SimpleView {
        this.mIconRes = iconRes
        return this
    }

    fun setIconShow(isIconShow: Boolean): SimpleView {
        this.isIconShow = isIconShow
        return this
    }

    fun setWrapContent(isWrapContent: Boolean): SimpleView {
        this.isWrapContent = isWrapContent
        return this
    }

    private fun setUIBeforeShow() {
        /**wrap_content */
        if (!isWrapContent) {
            val params = viewHolder.mLLLayout.layoutParams
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            viewHolder.mLLLayout.layoutParams = params
        } else {
            val params = viewHolder.mLLLayout.layoutParams
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            viewHolder.mLLLayout.layoutParams = params
        }

        viewHolder.mImg.visibility = View.VISIBLE
        /**loading */
        if (mStyle == STYLE.loading) {
            viewHolder.mImg.setImageDrawable(drawable)
            drawable.start()
        } else if (isIconShow) {
            /**icon */
            viewHolder.mImg.setImageResource(mIconRes)
        } else {
            viewHolder.mImg.visibility = View.GONE
        }

        /**button */
        if (TextUtils.isEmpty(mBtnText))
            viewHolder.mBtn.visibility = View.GONE
        else {
            viewHolder.mBtn.visibility = View.VISIBLE
            viewHolder.mBtn.text = mBtnText
            if (mOnClickListener != null)
                viewHolder.mBtn.setOnClickListener(mOnClickListener)
        }

        /**text */
        if (!TextUtils.isEmpty(text)) {
            viewHolder.mTvText.visibility = View.VISIBLE
            viewHolder.mTvText.text = text
        } else {
            viewHolder.mTvText.visibility = View.GONE
        }

    }
}