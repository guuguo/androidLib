package com.guuguo.android.lib.widget.simpleview

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.guuguo.android.R
import com.guuguo.android.lib.widget.roundview.RoundTextView

/**
 * mimi 创造于 2017-06-17.
 * 项目 order
 */

class SimpleViewHolder(var view: View) {
    val mLLLayout: LinearLayout = view.findViewById<LinearLayout>(R.id.layoutEmpty)
    val mImg: ImageView = view.findViewById(R.id.img)
    val mTvText: TextView = view.findViewById(R.id.tv_text)
    val mBtn: RoundTextView = view.findViewById(R.id.btn_empty)

    fun loading(msg: String, loadingDrawable: Drawable = DoubleCircleDrawable(view.context.resources.displayMetrics.density)): SimpleViewHolder {
        mImg.visibility = View.VISIBLE
        mBtn.visibility = View.GONE
        mImg.setImageDrawable(loadingDrawable)
        if (loadingDrawable is Animatable)
            loadingDrawable.start()
        mTvText.text = msg
        return this
    }

    fun state(text: String, imgRes: Int = 0, btnText: String? = "", listener: View.OnClickListener? = null): SimpleViewHolder {
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
}
