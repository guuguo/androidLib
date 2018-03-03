package com.guuguo.android.lib.widget.simpleview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.guuguo.android.R

/**
 * guuguo 创造于 16/5/28.
 * 项目 androidLib
 */
class StateLayout : FrameLayout {
    
    var contentView: View? = null
    var currentView: View? = null
    var simpleView: View? = null
    var viewHolder: SimpleViewHolder? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @Deprecated(message= "名字意义不清楚",replaceWith =ReplaceWith("showError()"))
    fun showErrorWithImage(text: String, btnText: String? = "", listener: OnClickListener? = null, imgRes: Int = R.drawable.state_error) {
        showState(text,btnText,listener,imgRes)
    }

    @Deprecated(message= "名字意义不清楚",replaceWith =ReplaceWith("showEmpty()"))
    fun showEmptyWithImage(text: String, btnText: String? = "", listener: OnClickListener, imgRes: Int = R.drawable.empty_cute_girl_box) {
        showState(text,btnText,listener,imgRes)
    }
    fun showError(text: String, btnText: String? = "", listener: OnClickListener? = null, imgRes: Int = R.drawable.state_error) {
        showState(text,btnText,listener,imgRes)
    }

    fun showEmpty(text: String, btnText: String? = "", listener: OnClickListener?=null, imgRes: Int = R.drawable.empty_cute_girl_box) {
        showState(text,btnText,listener,imgRes)
    }
    fun showText(text: String) {
        showState(text)
    }


    fun showState(text: String, btnText: String? = "", listener: OnClickListener? = null,imgRes:Int=0) {
        initContentView()
        showSimpleView().state(text, imgRes, btnText,listener)
    }

    fun showLoading(message: String) {
        initContentView()
        showSimpleView().loading(message)
    }

    fun restore() {
        initContentView()
        showContentView()
    }


    private fun showSimpleView(): SimpleViewHolder {
        if (simpleView == null) {
            simpleView = LayoutInflater.from(context).inflate(R.layout.base_include_simple_empty_view, this, false)
            viewHolder = SimpleViewHolder(simpleView!!)
        }
        if (simpleView != currentView) {
            removeAllViews()
            addView(simpleView!!)
            currentView = simpleView
        }
        return viewHolder!!
    }

    private fun showContentView() {
        if (contentView != currentView) {
            removeAllViews()
            addView(contentView!!)
            currentView = contentView
        }
    }

    private fun initContentView() {
        if (contentView == null) {
            contentView = getChildAt(0)
            currentView = contentView
        }
    }

    override fun addView(child: View) {
        if (childCount > 0) {
            throw IllegalStateException("StateLayout can host only one direct child")
        }
        super.addView(child, -1)
    }

    override fun addView(child: View, index: Int) {
        if (childCount > 0) {
            throw IllegalStateException("StateLayout can host only one direct child")
        }
        super.addView(child, index)
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        if (childCount > 0) {
            throw IllegalStateException("StateLayout can host only one direct child")
        }
        super.addView(child, params)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (childCount > 0) {
            throw IllegalStateException("StateLayout can host only one direct child")
        }
        super.addView(child, index, params)
    }

}