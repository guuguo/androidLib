package com.guuguo.androidlib.utils

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.guuguo.androidlib.R
import com.wang.avi.AVLoadingIndicatorView

/**
 * Created by Barry on 16/5/28.
 */
class SimpleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private var text: String = ""
    private var hint: String = ""
    private var mOnClickListener: View.OnClickListener? = null
    private var mBtnText: String = ""
    private var isIconShow = true
    private var mIconRes = R.drawable.empty_cute_girl_box
    private var isWrapContent = true
    private var mStyle: Int = STYLE.normal
    private var mLoadingIndicator = ""
    private var mLoadingColor: Int = -1

    private val mLLLayout: LinearLayout
    private val mImg: ImageView
    private val mTvText: TextView
    private val mTvHint: TextView
    private val mBtn: Button
    private val mLoadingVIew: AVLoadingIndicatorView


    private var gravity = Gravity.CENTER


    object STYLE {
        val loading = 0
        val normal = 1
    }

    init {
        View.inflate(context, R.layout.simple_empty_view, this)
        mLLLayout = findViewById(R.id.layoutEmpty) as LinearLayout
        mTvText = findViewById(R.id.tv_text) as TextView
        mTvHint = findViewById(R.id.tv_hint) as TextView
        mImg = findViewById(R.id.img) as ImageView
        mBtn = findViewById(R.id.btn_empty) as Button
        mLoadingVIew = findViewById(R.id.avl_loading) as AVLoadingIndicatorView
    }

    fun style(style: Int): SimpleView {
        this.mStyle = style
        return this
    }


    fun loadingColor(color: Int): SimpleView {
        this.mLoadingColor = color
        return this
    }

    fun loadingIndicator(text: String): SimpleView {
        this.mLoadingIndicator = text
        return this
    }

    fun text(text: String): SimpleView {
        this.text = text
        return this
    }

    fun hint(text: String): SimpleView {
        this.hint = text
        return this
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

    public override fun onAttachedToWindow() {
        setUIBeforeShow()
        super.onAttachedToWindow()
    }

    private fun setUIBeforeShow() {
        /**wrap_content */
        if (!isWrapContent) {
            val params = FrameLayout.LayoutParams(mLLLayout.layoutParams)
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            mLLLayout.layoutParams = params
        }else{
            val params = FrameLayout.LayoutParams(mLLLayout.layoutParams)
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            mLLLayout.layoutParams = params
        }

        /**loading */
        if (mStyle == STYLE.loading) {
            mLoadingVIew.visibility = View.VISIBLE
            isIconShow = false
            if (!TextUtils.isEmpty(mLoadingIndicator))
                mLoadingVIew.setIndicator(mLoadingIndicator)
            if (mLoadingColor != -1)
                mLoadingVIew.setIndicatorColor(mLoadingColor)
        } else {
            mLoadingVIew.visibility = View.GONE
        }

        /**button */
        if (TextUtils.isEmpty(mBtnText))
            mBtn.visibility = View.GONE
        else {
            mBtn.visibility = View.VISIBLE
            mBtn.text = mBtnText
            if (mOnClickListener != null)
                mBtn.setOnClickListener(mOnClickListener)
        }

        /**text */
        if (!TextUtils.isEmpty(text)) {
            mTvText.visibility = View.VISIBLE
            mTvText.text = text
        } else if (TextUtils.isEmpty(hint)) {
            hint = "没有数据"
        } else {
            mTvText.visibility = View.GONE
        }

        /**hint */
        if (!TextUtils.isEmpty(hint)) {
            mTvHint.visibility = View.VISIBLE
            mTvHint.text = hint
        } else {
            mTvHint.visibility = View.GONE
        }

        /**icon */
        if (isIconShow) {
            mImg.visibility = View.VISIBLE
            mImg.setImageResource(mIconRes)
        } else {
            mImg.visibility = View.GONE
        }
    }
}