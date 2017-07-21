package com.guuguo.android.lib.view.progress

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.guuguo.android.R
import com.guuguo.android.lib.extension.dpToPx


/**
 * mimi 创造于 2017-07-20.
 * 项目 androidLib
 */

class ProgressRing(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : View(context, attrs) {
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    /**
     * id:&:R.color.
     */
    private lateinit var mBgPaint: Paint
    private lateinit var mProgressPaint: Paint
    private var progressbarWidth: Int = 0
    private var progress = 20
    private var startAngle = 0
    private var sweepAngle = 0
    private var bgEndColor = Color.GRAY
    private var bgStartColor = Color.GRAY
    private var bgMidColor = Color.GRAY
    private var progressStartColor = Color.YELLOW
    private var progressEndColor = Color.BLUE
    private var isAnimShow = false

    init {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0)
        initByAttributes(attributes)
        attributes.recycle()
        initPaint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val width = View.MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        var height = View.MeasureSpec.getSize(heightMeasureSpec) - paddingLeft - paddingRight

        if (widthMode == View.MeasureSpec.EXACTLY && heightMode != View.MeasureSpec.EXACTLY) {
            val minHeight = Math.min(Math.sin(Math.toRadians(startAngle.toDouble())), Math.sin(Math.toRadians((startAngle - sweepAngle).toDouble()))) * (width / 2)
            height = (width / 2 - minHeight).toInt()
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height,
                    View.MeasureSpec.EXACTLY)
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    protected fun initByAttributes(attributes: TypedArray) {
        progressbarWidth = attributes.getInteger(R.styleable.ProgressRing_cr_progressbar_width, 5.dpToPx())
        progress = attributes.getInteger(R.styleable.ProgressRing_cr_progress, progress)
        bgStartColor = attributes.getColor(R.styleable.ProgressRing_cr_background_bar_start_color, Color.GRAY)
        bgMidColor = attributes.getColor(R.styleable.ProgressRing_cr_background_bar_mid_color, Color.GRAY)
        bgEndColor = attributes.getColor(R.styleable.ProgressRing_cr_background_bar_end_color, Color.GRAY)
        progressStartColor = attributes.getColor(R.styleable.ProgressRing_cr_progressbar_start_color, Color.YELLOW)
        progressEndColor = attributes.getColor(R.styleable.ProgressRing_cr_progressbar_end_color, Color.BLUE)
        startAngle = attributes.getInteger(R.styleable.ProgressRing_cr_start_angle, 150)
        sweepAngle = attributes.getInteger(R.styleable.ProgressRing_cr_sweep_angle, 240)
        isAnimShow = attributes.getBoolean(R.styleable.ProgressRing_cr_anim_show, true)
    }

    private fun initPaint() {
        mBgPaint = Paint()
        mBgPaint.color = bgEndColor
        mBgPaint.strokeWidth = progressbarWidth.toFloat()
        mBgPaint.isAntiAlias = true
        mBgPaint.style = Paint.Style.STROKE

        mProgressPaint = Paint()
        mProgressPaint.strokeCap = Paint.Cap.ROUND
        mBgPaint.isAntiAlias = true
        mProgressPaint.color = progressEndColor
        mProgressPaint.strokeWidth = progressbarWidth.toFloat()
        mProgressPaint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawProgress(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        val progressAngle = sweepAngle * progress / 100

        val mRectF = getRoundRect(progressbarWidth)
        for (it in 0..sweepAngle - progressAngle - 1) {
            if (it == 0 || it == sweepAngle - progressAngle - 1)
                mBgPaint.strokeCap = Paint.Cap.ROUND
            else
                mBgPaint.strokeCap = Paint.Cap.BUTT

            mBgPaint.color = getGradient(it / (sweepAngle - progressAngle.toFloat()), Color.parseColor("#33333333"), bgEndColor)
            canvas.drawArc(mRectF, it + startAngle + progressAngle.toFloat(), 1f, false, mBgPaint)
        }
    }

    private fun drawProgress(canvas: Canvas) {
        val progressAngle = sweepAngle * progress / 100

        val mRectF = getRoundRect(progressbarWidth)
        canvas.drawArc(mRectF, startAngle.toFloat(), progressAngle.toFloat(), false, mProgressPaint)
    }

    //以宽为直径
    private fun getRoundRect(barWidth: Int): RectF {
        val mRectF = RectF()
        mRectF.left = (barWidth / 2).toFloat() // 左上角x
        mRectF.top = (barWidth / 2).toFloat() // 左上角y
        mRectF.right = (width - barWidth / 2).toFloat() // 左下角x
        mRectF.bottom = (width - barWidth / 2).toFloat() // 右下角y
        return mRectF
    }

    /**
     * 修改颜色透明度

     * @param color
     * *
     * @param alpha
     * *
     * @return
     */
    fun getGradient(fraction: Float, startColor: Int, endColor: Int): Int {
        val sAlpha = Color.alpha(startColor)
        val sRed = Color.red(startColor)
        val sGreen = Color.green(startColor)
        val sBlue = Color.blue(startColor)

        val eAlpha = Color.alpha(endColor)
        val eRed = Color.red(endColor)
        val eGreen = Color.green(endColor)
        val eBlue = Color.blue(endColor)

        val currentAlpha = getCurrent(sAlpha, eAlpha, fraction)
        val currentRed = getCurrent(sRed, eRed, fraction)
        val currentGreen = getCurrent(sGreen, eGreen, fraction)
        val currentBlue = getCurrent(sBlue, eBlue, fraction)
        return Color.argb(currentAlpha, currentRed, currentGreen, currentBlue)
    }

    fun getCurrent(sNum: Int, eNum: Int, fraction: Float): Int {
        return (sNum + (eNum - sNum) * fraction).toInt()
    }

}
