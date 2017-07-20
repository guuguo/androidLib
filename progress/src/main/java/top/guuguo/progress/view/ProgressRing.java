package top.guuguo.progress.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import top.guuguo.progress.R;
import top.guuguo.progress.util.DensityUtil;

/**
 * mimi 创造于 2017-07-20.
 * 项目 androidLib
 */

public class ProgressRing extends View {
    /**
     * id:&:R.color.
     */
    private Paint mPaint;
    private int progressbarWidth;
    private int progress;
    private int startAngle = 0;
    private int sweepAngle = 0;
    private int bgColor = Color.GRAY;
    private int progressColor = Color.BLUE;
    private boolean isAnimShow = false;

    public ProgressRing(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressRing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 父容器传过来的宽度方向上的模式  
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // 父容器传过来的高度方向上的模式  
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 父容器传过来的宽度的值  
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
                - getPaddingRight();
        // 父容器传过来的高度的值  
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingLeft()
                - getPaddingRight();

        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            // 判断条件为，宽度模式为Exactly，照着宽度来；  
            // 且高度模式不是Exaclty，代表设置的既不是fill_parent也不是具体的值，于是需要具体测量  
            // 且图片的宽高比已经赋值完毕，不再是0.0f  
            // 表示宽度确定，要测量高度  
            double minHeight = Math.min(Math.sin(Math.toRadians(startAngle)), Math.sin(Math.toRadians(startAngle - sweepAngle))) * (width / 2);
            height = (int) (width / 2 - minHeight);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                    MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void initByAttributes(TypedArray attributes) {
        progressbarWidth = attributes.getInteger(R.styleable.ProgressRing_cr_progressbar_width, DensityUtil.dip2px(getContext(), 4));
        progress = attributes.getInteger(R.styleable.ProgressRing_cr_progress, 0);
        bgColor = attributes.getColor(R.styleable.ProgressRing_cr_background_bar_color, Color.GRAY);
        progressColor = attributes.getInteger(R.styleable.ProgressRing_cr_progressbar_color, Color.BLUE);
        startAngle = attributes.getInteger(R.styleable.ProgressRing_cr_start_angle, 150);
        sweepAngle = attributes.getInteger(R.styleable.ProgressRing_cr_sweep_angle, 240);
        isAnimShow = attributes.getBoolean(R.styleable.ProgressRing_cr_anim_show, true);
    }

    private void initPaint() {
        // 初始化paint
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
    }

    private void drawBackground(Canvas canvas) {
        // 设置画笔相关属性
        mPaint.setColor(bgColor);
        mPaint.setStrokeWidth(progressbarWidth);
        mPaint.setStyle(Paint.Style.STROKE);

        RectF mRectF = getRoundRect(progressbarWidth);
        // 绘制圆圈，进度条背景
        canvas.drawArc(mRectF, startAngle, sweepAngle, false, mPaint);
    }

    //以宽为直径
    private RectF getRoundRect(int barWidth) {
        RectF mRectF = new RectF();
        mRectF.left = barWidth / 2; // 左上角x
        mRectF.top = barWidth / 2; // 左上角y
        mRectF.right = getWidth() - barWidth / 2; // 左下角x
        mRectF.bottom = getWidth() - barWidth / 2; // 右下角y
        return mRectF;
    }
}
