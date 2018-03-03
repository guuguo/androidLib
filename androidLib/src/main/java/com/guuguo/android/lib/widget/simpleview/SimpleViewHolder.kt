package com.guuguo.android.lib.widget.simpleview

import android.graphics.Color
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.guuguo.android.R
import com.guuguo.android.lib.extension.dpToPx
import com.guuguo.android.lib.utils.DisplayUtil
import com.guuguo.android.lib.widget.roundview.RoundTextView

/**
 * mimi 创造于 2017-06-17.
 * 项目 order
 */

class SimpleViewHolder(var view: View) {
    val mLayout: ViewGroup = view.findViewById(R.id.layoutEmpty)
    val mImg: ImageView = view.findViewById(R.id.img)
    val mTvText: TextView = view.findViewById(R.id.tv_text)
    val mBtn: RoundTextView = view.findViewById(R.id.btn_empty)

    fun loading(msg: String, loadingDrawable: Drawable =DoubleCircleDrawable(DisplayUtil.getDensity())): SimpleViewHolder {
        mImg.visibility = View.VISIBLE
        mBtn.visibility = View.GONE
        mImg.layoutParams.width=50.dpToPx()
        mImg.requestLayout()
        mImg.setImageDrawable(loadingDrawable)
        if (loadingDrawable is Animatable)
            loadingDrawable.start()
        if(msg.isEmpty())
            mTvText.visibility=View.GONE
        else
            mTvText.visibility=View.VISIBLE

        mTvText.text = msg
        return this
    }

    fun state(text: String, imgRes: Int = 0, btnText: String? = "", listener: View.OnClickListener? = null): SimpleViewHolder {
        mImg.layoutParams.width=100.dpToPx()
        mImg.requestLayout()
        if (imgRes == 0)
            mImg.visibility = View.GONE
        else {
            mImg.visibility = View.VISIBLE
            mImg.setImageResource(imgRes)
        }
        if (btnText.isNullOrEmpty())
            mBtn.visibility = View.GONE
        else {
            mBtn.visibility = View.VISIBLE
            mBtn.text = btnText
            mBtn.setOnClickListener (listener)
        }
        mTvText.text = text
        return this
    }
    fun showError(text: String, btnText: String? = "", listener: View.OnClickListener? = null, imgRes: Int = R.drawable.state_error) {
        showState(text, btnText, listener, imgRes)
    }

    fun showEmpty(text: String, btnText: String? = "", listener: View.OnClickListener?=null, imgRes: Int = R.drawable.empty_cute_girl_box) {
        showState(text, btnText, listener, imgRes)
    }

    fun showText(text: String) {
        showState(text)
    }


    fun showState(text: String, btnText: String? = "", listener: View.OnClickListener? = null, imgRes: Int = 0) {
        state(text, imgRes, btnText, listener)
    }

    fun showLoading(message: String) {
        loading(message)
    }
}
