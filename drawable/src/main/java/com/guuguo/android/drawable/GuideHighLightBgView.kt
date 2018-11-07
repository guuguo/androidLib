package com.guuguo.android.drawable

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.LinearLayout


class GuideHighLightBgView : LinearLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    init {
        setWillNotDraw(false)
    }

    var targetCX = 0f
    var targetCY = 0f
    var targetRadius = 0f
    var targetWidth = 0
    var targetHeight = 0

    var isCircle = true
    fun makeDstShape(canvas: Canvas): Bitmap {
        val bm = Bitmap.createBitmap(canvas.width, canvas.height, Bitmap.Config.ARGB_8888)
        val lCanvas = Canvas(bm)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE
        if (isCircle) {
            lCanvas.drawCircle(targetCX, targetCY, targetRadius, paint)
        } else {
            lCanvas.drawRect(targetCX - targetWidth / 2, targetCY - targetHeight / 2, targetCX + targetWidth / 2, targetCY + targetWidth / 2, paint)
        }
        return bm
    }

    private fun makeSrcRect(canvas: Canvas): Bitmap {
        val bm = Bitmap.createBitmap(canvas.width, canvas.height, Bitmap.Config.ARGB_8888);
        val lCanvas = Canvas(bm);
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.BLACK
        lCanvas.drawRect(RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat()), paint)
        return bm;
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val mSrcRect = makeSrcRect(canvas);
        val mDstCircle = makeDstShape(canvas);

        canvas.drawBitmap(mDstCircle, 0f, 0f, mPaint)
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
        mPaint.alpha = 160;
        canvas.drawBitmap(mSrcRect, 0f, 0f, mPaint)
    }

}