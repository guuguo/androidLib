package com.guuguo.android.lib.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.content.res.ColorStateList
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.guuguo.android.R
import com.guuguo.android.lib.extension.dpToPx
import com.guuguo.android.lib.extension.safe

/**
 * mimi 创造于 2017-07-06.
 * 项目 order
 */

class FunctionTextView : LinearLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.FunctionTextView, defStyleAttr, 0)
        initAttr(context, attributes)
        attributes.recycle()
        initView()
    }

    private fun initAttr(context: Context, attributes: TypedArray) {
        text = attributes.getString(R.styleable.FunctionTextView_android_text).safe("")
        textSize = attributes.getDimension(R.styleable.FunctionTextView_android_textSize, 12.dpToPx().toFloat())
        textColor = attributes.getColor(R.styleable.FunctionTextView_android_textColor, Color.BLACK)
        drawableTint = attributes.getColor(R.styleable.FunctionTextView_ftv_drawableTint , 0)
        drawable = attributes.getDrawable(R.styleable.FunctionTextView_ftv_drawableSrc)

        drawableAlign = attributes.getInt(R.styleable.FunctionTextView_ftv_drawableAlign, 0)
        drawableWidth = attributes.getDimension(R.styleable.FunctionTextView_ftv_drawableWidth, -2f)
        drawableHeight = attributes.getDimension(R.styleable.FunctionTextView_ftv_drawableHeight, -2f)
        drawablePadding = attributes.getDimension(R.styleable.FunctionTextView_android_drawablePadding, 0f)
    }

    var text: String = ""
    var textSize: Float = 0f
    var textColor: Int = Color.BLACK
    var drawableTint: Int = 0
    /**
     * left "0" />
     * top"1" />
     * right"2" />
     * bottom"3" />
     */
    var drawableAlign: Int? = null
    var drawable: Drawable? = null
    var drawableWidth: Float = -2f
    var drawableHeight: Float = -2f
    var drawablePadding: Float = 0f

    val ALIGN_LEFT = 0
    val ALIGN_TOP = 1
    val ALIGN_RIGHT = 2
    val ALIGN_BOTTOM = 3
    lateinit var imageView: ImageView
    lateinit var textView: TextView
    private fun initView() {
        gravity=Gravity.CENTER
        textView = TextView(context)
        imageView = ImageView(context)

        requestViews()
    }

    private fun requestViews() {
        removeAllViews()
        when (drawableAlign) {
            ALIGN_LEFT -> orientation = HORIZONTAL
            ALIGN_TOP -> orientation = VERTICAL
            ALIGN_RIGHT -> orientation = HORIZONTAL
            ALIGN_BOTTOM -> orientation = VERTICAL
        }
        if (drawableTint !=0)
            drawable?.let {
                val wrapped = DrawableCompat.wrap(it)
                DrawableCompat.setTint(wrapped, drawableTint)
                imageView.setImageDrawable(wrapped)
            }
        else {
            imageView.setImageDrawable(drawable)
        }

        val imageViewParams = LayoutParams(drawableWidth.toInt(), drawableHeight.toInt())

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        textView.text = text
        textView.setTextColor(textColor)

        val textViewParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            when (drawableAlign) {
                ALIGN_LEFT -> marginStart = drawablePadding.toInt()
                ALIGN_TOP -> topMargin = drawablePadding.toInt()
                ALIGN_RIGHT -> marginEnd = drawablePadding.toInt()
                ALIGN_BOTTOM -> bottomMargin = drawablePadding.toInt()
            }
        }
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
    }

    private fun createColorStateList(normal: Int, pressed: Int, focused: Int, unable: Int): ColorStateList {
        val colors = intArrayOf(pressed, focused, normal, focused, unable, normal)
        val states = arrayOfNulls<IntArray>(6)
        states[0] = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
        states[1] = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_focused)
        states[2] = intArrayOf(android.R.attr.state_enabled)
        states[3] = intArrayOf(android.R.attr.state_focused)
        states[4] = intArrayOf(android.R.attr.state_window_focused)
        states[5] = intArrayOf()
        return ColorStateList(states, colors)
    }
}