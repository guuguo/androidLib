package com.guuguo.android.lib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.Config.ARGB_8888
import android.util.AttributeSet
import android.widget.FrameLayout
import com.guuguo.android.lib.extension.changeAlpha
import java.lang.IllegalArgumentException

class ShadowFrameLayout : FrameLayout {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    var shadowColor: Int = Color.BLACK
    private var shadow: Bitmap? = null
    private var bgBitmap: Bitmap? = null
    var shadowAlpha = 255
    private var shadowTranslationY = 8f
    private var blurShadowRadius = 24f
    var cardShadowRadius = 0f

    private var shadowScale = 1f

    /**bg*/
    private var bgColor = 0
    private var bgColorEnd = 0

    /**triangle*/
    private var triangleHeight = 0f
    private var triangleWidth = 80f
    private var triangleCubicRadius = 10f

    /**stroke*/
    private var strokeColor = Color.GRAY
    private var strokeWidth = 0f

    /**0 左 1 上 2 右 3 下*/
    var triangleAlign = TRIANGLE_ALIGN_RIGHT
    var triangleStartExtent = 150f

    companion object {
        val TRIANGLE_ALIGN_LEFT = 0
        val TRIANGLE_ALIGN_TOP = 1
        val TRIANGLE_ALIGN_RIGHT = 2
        val TRIANGLE_ALIGN_BOTTOM = 3
    }

    //    private var smallBlurRadius = 8f
    private var padding = blurShadowRadius.toInt()
    private val childBounds = RectF()

    //    private val shadowBounds = RectF()
    private var bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bgPaint = Paint()
    private val bgMaskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private lateinit var mXfermode: Xfermode
    private val mPorterDuffMode = PorterDuff.Mode.DST_IN
    fun init(attrs: AttributeSet) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.ShadowFrameLayout)
        shadowTranslationY = a.getDimension(R.styleable.ShadowFrameLayout_shadowTranslationY,
                shadowTranslationY)

        shadowAlpha = a.getFloat(R.styleable.ShadowFrameLayout_cardShadowAlpha,
                shadowAlpha.toFloat()).toInt().coerceIn(0, 255)
        blurShadowRadius = a.getDimension(R.styleable.ShadowFrameLayout_blurShadowRadius, blurShadowRadius)
        cardShadowRadius = a.getDimension(R.styleable.ShadowFrameLayout_cardShadowRadius, cardShadowRadius)
        shadowScale = a.getFloat(R.styleable.ShadowFrameLayout_shadowScale, shadowScale).coerceIn(0f, 1f)

        bgColor = a.getColor(R.styleable.ShadowFrameLayout_sfl_cardBackgroundColor, bgColor)
        bgColorEnd = a.getColor(R.styleable.ShadowFrameLayout_sfl_cardBackgroundColorEnd, bgColor)
        strokeColor = a.getColor(R.styleable.ShadowFrameLayout_sfl_cardStrokeColor, strokeColor)
        strokeWidth = a.getDimension(R.styleable.ShadowFrameLayout_sfl_cardStrokeWidth, strokeWidth)
        triangleAlign = a.getInteger(R.styleable.ShadowFrameLayout_sfl_cardTriangleAlign, triangleAlign)
        triangleStartExtent = a.getDimension(R.styleable.ShadowFrameLayout_sfl_cardTriangleStartExtent, triangleStartExtent)
        triangleCubicRadius = a.getDimension(R.styleable.ShadowFrameLayout_sfl_cardTriangleCubicRadius, triangleCubicRadius)
        triangleHeight = a.getDimension(R.styleable.ShadowFrameLayout_sfl_cardTriangleHeight, triangleHeight)
        triangleWidth = a.getDimension(R.styleable.ShadowFrameLayout_sfl_cardTriangleWidth, triangleWidth)


        padding = blurShadowRadius.toInt()
        shadowColor = a.getColor(R.styleable.ShadowFrameLayout_cardShadowColor, 0)
        a.recycle()
        setWillNotDraw(false)

        initPaint()
    }

    private fun initPaint() {
        mXfermode = PorterDuffXfermode(mPorterDuffMode);
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (shadow == null) {
            return
        }
        canvas.drawBitmap(shadow, -blurShadowRadius, -blurShadowRadius, bitmapPaint)
        if (strokeWidth > 0) {
            strokePaint.style = Paint.Style.STROKE
            strokePaint.strokeWidth = strokeWidth
            strokePaint.color = strokeColor
            canvas.drawRoundRect(childBounds, cardShadowRadius, cardShadowRadius, strokePaint)
            canvas.drawPath(trianglePath, strokePaint)
        }

        canvas.drawBitmap(bgBitmap, 0f, 0f, bitmapPaint)
    }

    lateinit var trianglePath: Path

    private fun createShadows() {
        try {
            ///大小有改变或者为空重建bitmap
            if (shadow == null || shadow!!.width != measuredWidth || shadow!!.height != measuredHeight) {
                ///阴影可以绘制出范围外 超出 blurShadowRadius 的大小
                shadow = Bitmap.createBitmap(measuredWidth + blurShadowRadius.toInt() * 2, measuredHeight + blurShadowRadius.toInt() * 2, ARGB_8888)
            } else {
                shadow?.eraseColor(Color.TRANSPARENT)
            }
            if (bgBitmap == null || bgBitmap!!.width != measuredWidth || bgBitmap!!.height != measuredHeight) {
                bgBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, ARGB_8888)
            } else {
                bgBitmap?.eraseColor(Color.TRANSPARENT)
            }
            trianglePath = Path()
            val p1 = triangleTriple.first
            val p2 = triangleTriple.second
            val p3 = triangleTriple.third

            val point1: Pair<Float, Float>
            val point2: Pair<Float, Float>
            when (triangleAlign) {
                1 -> {
                    val ratio = (p2.x - p1.x) / (p2.y - p1.y)
                    point1 = p2.x + ratio * triangleCubicRadius to p2.y + triangleCubicRadius
                    point2 = p2.x - ratio * triangleCubicRadius to p2.y + triangleCubicRadius
                }
                3 -> {
                    val ratio = -(p2.x - p1.x) / (p2.y - p1.y)
                    point1 = p2.x + ratio * triangleCubicRadius to p2.y - triangleCubicRadius
                    point2 = p2.x - ratio * triangleCubicRadius to p2.y - triangleCubicRadius
                }
                2 -> {
                    val ratio = -(p2.y - p1.y) / (p2.x - p1.x)
                    point1 = p2.x - triangleCubicRadius to p2.y + ratio * triangleCubicRadius
                    point2 = p2.x - triangleCubicRadius to p2.y - ratio * triangleCubicRadius
                }
                else -> {
                    val ratio = (p2.y - p1.y) / (p2.x - p1.x)
                    point1 = p2.x + triangleCubicRadius to p2.y + ratio * triangleCubicRadius
                    point2 = p2.x + triangleCubicRadius to p2.y - ratio * triangleCubicRadius
                }
            }


            /** align 是上的时候  p1是左  p2 是上 p3 是右
             * */
            trianglePath.moveTo(p1.x, p1.y)// 此点为多边形的起点
            trianglePath.lineTo(point1.first, point1.second)
            trianglePath.quadTo(p2.x, p2.y, point2.first, point2.second)
            trianglePath.lineTo(p3.x, p3.y)

            trianglePath.close() // 使这些点构成封闭的多边形

            createShadow(shadow)
            createBg(bgBitmap)
        } catch (e: Exception) {
        }
    }

    /**
     * 绘制阴影的bitmap
     * @param bitmap Bitmap? 绘制阴影的载体
     * @param path Path 三角形路径
     */
    private fun createShadow(bitmap: Bitmap?) {
        val canvas = Canvas()
        canvas.setBitmap(bitmap)

        shadowPaint.style = Paint.Style.FILL
        shadowPaint.color = shadowColor.changeAlpha(shadowAlpha)
        shadowPaint.setShadowLayer(blurShadowRadius, 0f, shadowTranslationY, shadowPaint.color);

        canvas.scale(shadowScale, shadowScale, 1.5f * childBounds.left + 0.5f * childBounds.right, childBounds.bottom)
        canvas.save()
        canvas.translate(blurShadowRadius,blurShadowRadius)

        canvas.drawRoundRect(childBounds, cardShadowRadius, cardShadowRadius, shadowPaint)
        if (triangleHeight > 0)
            canvas.drawPath(trianglePath, shadowPaint);
        canvas.restore()
    }

    /**
     * 绘制背景的bitmap
     * @param bitmap Bitmap? 绘制背景的载体
     * @param path Path 三角形路径
     */
    private fun createBg(bitmap: Bitmap?) {
        val canvas = Canvas()
        canvas.setBitmap(bitmap)

        //将绘制操作保存到新的图层，因为图像合成是很昂贵的操作，将用到硬件加速，这里将图像合成的处理放到离屏缓存中进行
        val saveCount = canvas.saveLayer(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), bgPaint, Canvas.ALL_SAVE_FLAG)

        //绘制背景色 DST
        val linearGradient = LinearGradient(childBounds.left, measuredHeight / 2f, childBounds.right, measuredHeight / 2f,
                bgColor,
                bgColorEnd, Shader.TileMode.CLAMP)
        bgPaint.shader = linearGradient
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), bgPaint)


        //设置遮罩混合模式
        bgPaint.shader = null
        bgPaint.xfermode = mXfermode
        bgPaint.color = bgColor
        bgPaint.style = Paint.Style.FILL

        val srcBitmap = createMask()
        canvas.drawBitmap(srcBitmap, 0f, 0f, bgPaint)
        //清除混合模式
        bgPaint.xfermode = null
        //还原画布
        canvas.restoreToCount(saveCount)
    }

    /** 绘制遮罩的bitmap */
    private fun createMask(): Bitmap {
        val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        val canvas = Canvas(bitmap)
        bgMaskPaint.color = bgColor

        //绘制圆角矩形遮罩
        canvas.drawRoundRect(childBounds, cardShadowRadius, cardShadowRadius, bgMaskPaint)
        if (triangleHeight > 0) {
            //绘制三角形遮罩
            canvas.drawPath(trianglePath, bgMaskPaint)
        }
        return bitmap
    }

    val tempRect = Rect()


    /** 三角形的三个点位 */
    var triangleTriple: Triple<PointF, PointF, PointF> = Triple(PointF(), PointF(), PointF())

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val child = getChildAt(0)

        child.getDrawingRect(tempRect)
        if (tempRect.height() == 0 || tempRect.width() == 0)
            return
        childBounds.set(tempRect)
        childBounds.offset(child.x, child.y)

        initTrianglePoint()
        createShadows()
    }

//    /** 上次的阴影信息，判断是否需要重建用 */
//    var lastCacheBean: ShadowInfo? = null
//
//    class ShadowInfo(var childBound: RectF,
//                     var shadowColor: Int,
//                     var shadowAlpha: Int,
//                     var shadowTranslationY: Float,
//                     var blurShadowRadius: Float,
//                     var cardShadowRadius: Float,
//                     var shadowScale: Float,
//                     var bgColor: Int,
//                     var bgColorEnd: Int,
//                     var triangleHeight: Float,
//                     var triangleWidth: Float,
//                     var triangleCubicRadius: Float,
//                     var strokeColor: Int,
//                     var strokeWidth: Float,
//                     var triangleAlign: Int,
//                     var triangleStartExtent: Float) {
//        fun isChangeTriangle(currentInfo: ShadowInfo) =
//                childBound.width() == currentInfo.childBound.width()
//                        && childBound.height() == currentInfo.childBound.height()
//                        && triangleAlign == currentInfo.triangleAlign
//                        && triangleStartExtent == currentInfo.triangleStartExtent
//                        && triangleWidth == currentInfo.triangleWidth
//                        && triangleHeight == currentInfo.triangleHeight
//                        && triangleCubicRadius == currentInfo.triangleCubicRadius
//
//        fun isNoChange(currentInfo: ShadowInfo) =
//                childBound.width() == currentInfo.childBound.width()
//                        && childBound.height() == currentInfo.childBound.height()
//                        && triangleAlign == currentInfo.triangleAlign
//                        && triangleStartExtent == currentInfo.triangleStartExtent
//                        && bgColor == currentInfo.bgColor
//                        && bgColorEnd == currentInfo.bgColorEnd
//                        && triangleWidth == currentInfo.triangleWidth
//                        && triangleHeight == currentInfo.triangleHeight
//                        && triangleCubicRadius == currentInfo.triangleCubicRadius
//                        && shadowScale == currentInfo.shadowScale
//                        && cardShadowRadius == currentInfo.cardShadowRadius
//                        && blurShadowRadius == currentInfo.blurShadowRadius
//                        && shadowTranslationY == currentInfo.shadowTranslationY
//                        && shadowAlpha == currentInfo.shadowAlpha
//                        && shadowColor == currentInfo.shadowColor
//
//    }

    fun Float.coerceInSafe(minimumValue: Float, maximumValue: Float): Float {
        return try {
            coerceIn(minimumValue, maximumValue)
        } catch (e: IllegalArgumentException) {
            0f
        }
    }

    /** 计算三角形的三个点位信息 */
    private fun initTrianglePoint() {
        ///不可绘制三角形的地方，triangleStartExtent 的偏移量
        val noTriangleExtent = if (triangleHeight != 0f && triangleWidth != 0f) cardShadowRadius + triangleWidth / 2 else 0f

        when (triangleAlign) {
            1 -> {
                val lTriangleStartExtent = triangleStartExtent.coerceInSafe(noTriangleExtent + childBounds.left, childBounds.right - noTriangleExtent)
                triangleTriple.first.apply { x = lTriangleStartExtent - triangleWidth / 2f; y = childBounds.top }
                triangleTriple.second.apply { x = lTriangleStartExtent; y = childBounds.top - triangleHeight }
                triangleTriple.third.apply { x = lTriangleStartExtent + triangleWidth / 2f; y = childBounds.top }
            }
            2 -> {
                val lTriangleStartExtent = triangleStartExtent.coerceInSafe(noTriangleExtent + childBounds.top, childBounds.bottom - noTriangleExtent)
                triangleTriple.first.apply { x = childBounds.right; y = lTriangleStartExtent - triangleWidth / 2f }
                triangleTriple.second.apply { x = childBounds.right + triangleHeight; y = lTriangleStartExtent }
                triangleTriple.third.apply { x = childBounds.right; y = lTriangleStartExtent + triangleWidth / 2 }
            }
            3 -> {
                val lTriangleStartExtent = triangleStartExtent.coerceInSafe(noTriangleExtent + childBounds.left, childBounds.right - noTriangleExtent)
                triangleTriple.first.apply { x = lTriangleStartExtent - triangleWidth / 2f;y = childBounds.bottom }
                triangleTriple.second.apply { x = lTriangleStartExtent;y = childBounds.bottom + triangleHeight }
                triangleTriple.third.apply { x = lTriangleStartExtent + triangleWidth / 2f;y = childBounds.bottom }
            }
            0 -> {
                val lTriangleStartExtent = triangleStartExtent.coerceInSafe(noTriangleExtent + childBounds.top, childBounds.bottom - noTriangleExtent)

                triangleTriple.first.apply { x = childBounds.left;y = lTriangleStartExtent - triangleWidth / 2f }
                triangleTriple.second.apply { x = childBounds.left - triangleHeight;y = lTriangleStartExtent }
                triangleTriple.third.apply { x = childBounds.left;y = lTriangleStartExtent + triangleWidth / 2f }
            }
        }
    }

}