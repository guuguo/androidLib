package com.guuguo.android.lib.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.guuguo.android.R
import com.guuguo.android.lib.extension.safe
import com.guuguo.android.lib.widget.roundview.RoundLinearLayout

/**
 * mimi 创造于 2017-07-06.
 * 项目 order
 */

class FunctionTextView : RoundLinearLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.textViewStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, R.style.Widget_AppCompat_Button)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.FunctionTextView, defStyleAttr, defStyleRes)
        initAttr(context, attributes)
        attributes.recycle()
        initView(attrs, defStyleAttr)
    }

    val ALIGN_LEFT = 0
    val ALIGN_TOP = 1
    val ALIGN_RIGHT = 2
    val ALIGN_BOTTOM = 3
    var imageView: AppCompatImageView? = null
    var textView: AppCompatTextView? = null
    private fun initView(attrs: AttributeSet?, defStyleAttr: Int) {
        textView = AppCompatTextView(context, attrs, defStyleAttr)
        imageView = AppCompatImageView(context)

        imageView?.background = textView?.background?.apply { mutate() }
        requestViews()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        imageView?.isEnabled = enabled
        textView?.isEnabled = enabled
    }

    private fun initAttr(context: Context, attributes: TypedArray) {
        text = attributes.getString(R.styleable.FunctionTextView_android_text).safe("")
        textStyle = attributes.getInt(R.styleable.FunctionTextView_android_textStyle, Typeface.NORMAL)
        drawableTintDefaultTextColor = attributes.getBoolean(R.styleable.FunctionTextView_ftv_drawableTintDefaultTextColor, false)
        drawableTint = attributes.getColor(R.styleable.FunctionTextView_ftv_drawableTint, 0)
        drawable = attributes.getDrawable(R.styleable.FunctionTextView_ftv_drawableSrc)

        drawableAlign = attributes.getInt(R.styleable.FunctionTextView_ftv_drawableAlign, 0)
        drawableWidth = attributes.getDimension(R.styleable.FunctionTextView_ftv_drawableWidth, -2f)
        drawableHeight = attributes.getDimension(R.styleable.FunctionTextView_ftv_drawableHeight, -2f)
        drawablePadding = attributes.getDimension(R.styleable.FunctionTextView_android_drawablePadding, 0f)

        gravity = attributes.getInt(R.styleable.FunctionTextView_android_gravity, Gravity.CENTER)
    }

    var text: String = ""
        set(value) {
            field = value
            textView?.apply {
                text = value
                isVisible = !value.isEmpty()
            }
            requestLayout()
        }

    var textSize: Float = 0f
        set(value) {
            field = value
            textView?.textSize = value
        }
    var textColor: Int = Color.BLACK
        set(value) {
            field = value
            textView?.setTextColor(value)
            setDrawableWithTint()
        }
        get() = textView?.currentTextColor.safe()
    var textStyle: Int = Typeface.NORMAL
        set(value) {
            field = value
            textView?.typeface = Typeface.defaultFromStyle(value);//加粗
        }
    var drawableTint: Int = 0
        set(value) {
            field = value
            setDrawableWithTint()
        }

    var drawableTintDefaultTextColor = false
    /**
     * left "0" />
     * top"1" />
     * right"2" />
     * bottom"3" />
     */
    var drawableAlign: Int? = null
    var drawable: Drawable? = null
        set(value) {
            field = value
            setDrawableWithTint()
        }

    private fun setDrawableWithTint() {
        drawable?.let {
            imageView?.visibility = View.VISIBLE
            if (drawableTint != 0) {
                val wrapped = DrawableCompat.wrap(drawable!!)
                DrawableCompat.setTint(wrapped, drawableTint)
                wrapped.mutate()
                imageView?.setImageDrawable(wrapped)
            } else if (drawableTint == 0 && drawableTintDefaultTextColor) {
                val color = textView?.currentTextColor.safe()
                val wrapped = DrawableCompat.wrap(it)
                wrapped.mutate()
                DrawableCompat.setTint(wrapped, color)
                imageView?.setImageDrawable(wrapped)
            } else {
                imageView?.setImageDrawable(it)
            }
            textView?.updateLayoutParams<LinearLayout.LayoutParams> {
                when (drawableAlign) {
                    ALIGN_LEFT -> marginStart = drawablePadding.toInt()
                    ALIGN_TOP -> topMargin = drawablePadding.toInt()
                    ALIGN_RIGHT -> marginEnd = drawablePadding.toInt()
                    ALIGN_BOTTOM -> bottomMargin = drawablePadding.toInt()
                }
            }
        } ?: {
            imageView?.visibility = View.GONE;textView?.updateLayoutParams<LinearLayout.LayoutParams> {
            setMargins(0, 0, 0, 0)
        }
        }.invoke()
    }

    var drawableWidth: Float = -2f
    var drawableHeight: Float = -2f
    var drawablePadding: Float = 0f


    fun requestViews() {
        removeAllViews()
        when (drawableAlign) {
            ALIGN_LEFT -> orientation = HORIZONTAL
            ALIGN_TOP -> orientation = VERTICAL
            ALIGN_RIGHT -> orientation = HORIZONTAL
            ALIGN_BOTTOM -> orientation = VERTICAL
        }

        //param
        val imageViewParams = LinearLayout.LayoutParams(drawableWidth.toInt(), drawableHeight.toInt())

        val textViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(0, 0, 0, 0)
            drawable?.let {
                when (drawableAlign) {
                    ALIGN_LEFT -> marginStart = drawablePadding.toInt()
                    ALIGN_TOP -> topMargin = drawablePadding.toInt()
                    ALIGN_RIGHT -> marginEnd = drawablePadding.toInt()
                    ALIGN_BOTTOM -> bottomMargin = drawablePadding.toInt()
                }
            }
        }
        textView?.setPadding(0, 0, 0, 0)
        when (drawableAlign) {
            ALIGN_LEFT, ALIGN_TOP -> {
                addView(imageView, imageViewParams)
                addView(textView, textViewParams)
            }
            ALIGN_RIGHT, ALIGN_BOTTOM -> {
                addView(textView, textViewParams)
                addView(imageView, imageViewParams)
            }
        }
//        if (drawableTint == 0 && drawableTintDefaultTextColor) {
//            drawableTint = textView?.currentTextColor.safe()
//        } else {
        setDrawableWithTint()
//        }


        if (text.isEmpty()) {
            textView?.visibility = View.GONE
        } else {
            textView?.visibility = View.VISIBLE
            textView?.text = text
        }
    }
}