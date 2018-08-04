package com.guuguo.android.dialog.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams
import android.widget.LinearLayout
import com.guuguo.android.dialog.utils.StatusBarUtils

abstract class BaseDialog<T : BaseDialog<T>> : Dialog {
    constructor(mContext: Context) : super(mContext) {
        this.mContext = mContext
    }

    constructor(mContext: Context, themeResId: Int) : super(mContext, themeResId) {
        this.mContext = mContext
    }

    protected lateinit var mContext: Context

    /** mTag(日志)  */
    protected var mTag: String
    /** (DisplayMetrics)设备密度  */
    lateinit protected var mDisplayMetrics: DisplayMetrics
    /** enable dismiss outside dialog(设置点击对话框以外区域,是否dismiss)  */
    protected var mCancel: Boolean = false
    /** dialog width scale(宽度比例)  */
    protected var mWidthRatio = 1f
    /** dialog height scale(高度比例)  */
    protected var mHeightRatio: Float = 0.toFloat()
    /** top container(最上层容器,显示阴影那个)  */
    lateinit protected var mContentTop: LinearLayout
    /** container to control dialog height(用于控制对话框高度)  */
    lateinit protected var mDialogContent: LinearLayout
    /** the child of mDialogContent you create.(创建出来的mLlControlHeight的直接子View)  */
    /** get actual created view(获取实际创建的View)  */
    lateinit var mOnCreateView: View
    /** max height(最大高度)  */
    protected var mMaxHeight: Float = 0.toFloat()
    /** show Dialog as PopupWindow(像PopupWindow一样展示Dialog)  */
    private var mIsPopupStyle: Boolean = false
    /** automatic dimiss dialog after given delay(在给定时间后,自动消失)  */
    private var mAutoDismiss: Boolean = false
    /** delay (milliseconds) to dimiss dialog(对话框消失延时时间,毫秒值)  */
    private var mAutoDismissDelay: Long = 1500
    private var mMarginBottom: Int = 0

    private val mHandler = Handler(Looper.getMainLooper())

    init {
        setDialogTheme()
        mTag = javaClass.simpleName
        setCanceledOnTouchOutside(true)
        Log.d(mTag, "constructor")
    }

    constructor(context: Context, isPopupStyle: Boolean) : this(context) {
        mIsPopupStyle = isPopupStyle
    }

    /** set dialog theme(设置对话框主题)  */
    private fun setDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)// android:windowNoTitle
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))// android:windowBackground
        window!!.addFlags(LayoutParams.FLAG_DIM_BEHIND)// android:backgroundDimEnabled默认是true的
    }

    /**
     * inflate layout for dialog ui and return (填充对话框所需要的布局并返回)
     * <pre>
     * public View onCreateView() {
     * View inflate = View.inflate(mContext, R.layout.dialog_share, null);
     * return inflate;
     * }
    </pre> *
     */
    abstract fun onCreateView(): View

    open fun onViewCreated(inflate: View) {}

    /** set Ui data or logic opreation before attatched window(在对话框显示之前,设置界面数据或者逻辑)  */
    abstract fun setUiBeforShow()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(mTag, "onCreate")
        mDisplayMetrics = mContext.resources.displayMetrics
        mMaxHeight = (mDisplayMetrics.heightPixels - StatusBarUtils.getHeight(mContext)).toFloat()

        mContentTop = LinearLayout(mContext)
        mContentTop.gravity = Gravity.CENTER

        mDialogContent = LinearLayout(mContext)
        mDialogContent.orientation = LinearLayout.VERTICAL

        mOnCreateView = onCreateView()
        mDialogContent.addView(mOnCreateView)
        mContentTop.addView(mDialogContent)
        onViewCreated(mOnCreateView)

        if (mIsPopupStyle) {
            setContentView(mContentTop, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT))
        } else {
            setContentView(mContentTop, ViewGroup.LayoutParams(mDisplayMetrics.widthPixels, mMaxHeight.toInt()))
        }

        mContentTop.setOnClickListener {
            if (mCancel) {
                dismiss()
            }
        }

        mOnCreateView.isClickable = true
    }

    /**
     * when dailog attached to window,set dialog width and height and show anim
     * (当dailog依附在window上,设置对话框宽高以及显示动画)
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d(mTag, "onAttachedToWindow")

        setUiBeforShow()

        val width: Int
        if (mWidthRatio == 0f) {
            width = ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            width = (mDisplayMetrics.widthPixels * mWidthRatio).toInt()
        }

        val height: Int
        if (mHeightRatio == 0f) {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        } else if (mHeightRatio == 1f) {
            //            height = ViewGroup.LayoutParams.MATCH_PARENT;
            height = mMaxHeight.toInt()
        } else {
            height = (mMaxHeight * mHeightRatio).toInt()
        }

        mDialogContent.layoutParams = LinearLayout.LayoutParams(width, height).also { it.bottomMargin = mMarginBottom }
    }

    override fun setCanceledOnTouchOutside(cancel: Boolean) {
        this.mCancel = cancel
        super.setCanceledOnTouchOutside(cancel)
    }

    override fun show() {
        Log.d(mTag, "show")
        super.show()
    }


    override fun onStart() {
        super.onStart()
        Log.d(mTag, "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(mTag, "onStop")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d(mTag, "onDetachedFromWindow")
    }

    /** dismiss without anim(无动画dismiss)  */
    fun superDismiss() {
        super.dismiss()
    }

    /** dialog anim by styles(动画弹出对话框,style动画资源)  */
    fun show(animStyle: Int) {
        val window = window
        window!!.setWindowAnimations(animStyle)
        show()
    }

    /** show at location only valid for mIsPopupStyle true(指定位置显示,只对isPopupStyle为true有效)  */
    fun showAtLocation(gravity: Int, x: Int, y: Int) {
        if (mIsPopupStyle) {
            val window = window
            val params = window!!.attributes
            window.setGravity(gravity)
            params.x = x
            params.y = y
        }

        show()
    }

    /** show at location only valid for mIsPopupStyle true(指定位置显示,只对isPopupStyle为true有效)  */
    fun showAtLocation(x: Int, y: Int) {
        val gravity = Gravity.LEFT or Gravity.TOP//Left Top (坐标原点为右上角)
        showAtLocation(gravity, x, y)
    }

    /** set window dim or not(设置背景是否昏暗)  */
    fun dimEnabled(isDimEnabled: Boolean): T {
        if (isDimEnabled) {
            window!!.addFlags(LayoutParams.FLAG_DIM_BEHIND)
        } else {
            window!!.clearFlags(LayoutParams.FLAG_DIM_BEHIND)
        }
        return this as T
    }

    /** set dialog width scale:0-1(设置对话框宽度,占屏幕宽的比例0-1)  */
    fun widthRatio(widthScale: Float): T {
        this.mWidthRatio = widthScale
        return this as T
    }

    /** 设置底部margin,让dialog位置上移或者下移 */
    fun marginBottom(marginBottom: Int): T {
        this.mMarginBottom = marginBottom
        return this as T
    }

    /** set dialog height scale:0-1(设置对话框高度,占屏幕高的比例0-1)  */
    fun heightRatio(heightScale: Float): T {
        mHeightRatio = heightScale
        return this as T
    }

    /** automatic dimiss dialog after given delay(在给定时间后,自动消失)  */
    fun autoDismiss(autoDismiss: Boolean): T {
        mAutoDismiss = autoDismiss
        return this as T
    }

    /** set dealy (milliseconds) to dimiss dialog(对话框消失延时时间,毫秒值)  */
    fun autoDismissDelay(autoDismissDelay: Long): T {
        mAutoDismissDelay = autoDismissDelay
        return this as T
    }

    private fun delayDismiss() {
        if (mAutoDismiss && mAutoDismissDelay > 0) {
            mHandler.postDelayed({ dismiss() }, mAutoDismissDelay)
        }
    }

    /**  if AutoDismiss,prevent Touch (如果自动消失,阻止点击) */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return if (mAutoDismiss) {
            true
        } else super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        if (mAutoDismiss) {
            return
        }
        super.onBackPressed()
    }

    /** dp to px  */
    protected fun dp2px(dp: Float): Int {
        val scale = mContext.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}