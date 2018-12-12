package top.guuguo.myapplication.ui.guide;

import android.app.Activity
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.doOnNextLayout
import androidx.core.view.updateLayoutParams
import top.guuguo.myapplication.R

import com.guuguo.android.dialog.base.BaseDialog
import com.guuguo.android.drawable.GuideHighLightBgView
import com.guuguo.android.lib.extension.dpToPx
import com.guuguo.android.lib.systembar.SystemBarHelper
import com.guuguo.android.lib.utils.DisplayUtil

/**
 * PackageName com.bianla.app.widget.dialog
 * Created by admin on 2016/11/29.
 */

class HomeGuideDialog(var activity: Activity, var targetView: View, var guildType: Int) : BaseDialog<HomeGuideDialog>(activity) {
    fun getLayoutResId() = R.layout.home_layout_guild_home_without_scale
    var padding = 10.dpToPx()
    /**0没有，1圆圈，2方形*/
    var targetShape = 1
    private var targetWidth = 0
    private var targetHeight = 0
    fun initSize(width: Int, height: Int) = also {
        it.targetWidth = width
        it.targetHeight = height
    }

    override fun setUiBeforShow() {

        findViewById<View>(R.id.iv_i_know).setOnClickListener {
            dismiss()
        }

        val arrow = findViewById<ImageView>(R.id.iv_arrow)
        val hint = findViewById<TextView>(R.id.tv_hint)
        val circleView = findViewById<ImageView>(R.id.iv_target_circle)
        val btn = findViewById<View>(R.id.iv_i_know)
        val guideContent = findViewById<GuideHighLightBgView>(R.id.guild_content)

        setBeforeShow(this)
        targetView.requestLayout()
        targetView.doOnNextLayout {
            it.apply {
                if (targetWidth == 0)
                    targetWidth = width
                if (targetHeight == 0)
                    targetHeight = height

                val circleWidth = targetWidth + padding * 2
                val circleHeight = targetHeight + padding * 2

                val location = IntArray(2)
                getLocationInWindow(location)
                var lCx = location[0] + width / 2
                var lCY = location[1] + height / 2
                guideContent.targetCX = lCx.toFloat()
                guideContent.targetCY = lCY.toFloat()
                when (targetShape) {
                    1 -> {
                        guideContent.isCircle = true
                        guideContent.targetRadius = targetWidth / 2f
                    }
                    2 -> {
                        guideContent.isCircle = false
                        guideContent.targetWidth = targetWidth
                        guideContent.targetHeight = targetHeight
                    }
                }
                guideContent.invalidate()

                val mLeft = lCx - circleWidth / 2
                var mTop = 0
                var mBottom = 0

                val bgLocation = IntArray(2)
                guideContent.getLocationOnScreen(bgLocation)
                //如果背景顶部是状态栏高度，则 margin 等于负的状态栏高度，适配三星部分手机状态栏顶不上去
                if (bgLocation[1] == SystemBarHelper.getStatusBarHeight(mContext)) {
                    guideContent.updateLayoutParams<LinearLayout.LayoutParams> {
                        topMargin = -SystemBarHelper.getStatusBarHeight(mContext)
                        bottomMargin=SystemBarHelper.getStatusBarHeight(mContext)
                    }
                }

                if (location[1] > getScreenDisplayHeight() / 2) {
                    val content = mOnCreateView as LinearLayout
                    content.removeAllViews()
                    content.addView(btn)
                    content.addView(hint)
                    content.addView(arrow)
                    content.addView(circleView)
                    mTop = 0
                    content.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                    mBottom = guideContent.measuredHeight - (location[1] + measuredHeight / 2) - circleHeight / 2
                    arrow.setImageResource(R.drawable.guide_arrow_left)
                    if (location[0] < DisplayUtil.getScreenWidth() / 2) {
                        arrow.scaleX = -1f
                        arrow.scaleY = 1f
                    }
                } else {
                    mBottom = 0
                    mTop = lCY - circleHeight / 2
                    arrow.setImageResource(R.drawable.guide_arrow_left)
                    if (location[0] >= DisplayUtil.getScreenWidth() / 2) {
                        arrow.scaleX = -1f
                        arrow.scaleY = 1f
                    }
                }
                if (location[0] < DisplayUtil.getScreenWidth() / 2) {
                    arrow.updateLayoutParams<LinearLayout.LayoutParams> {
                        leftMargin = location[0] + measuredWidth / 2 + 5.dpToPx()
                    }
                } else {
                    arrow.updateLayoutParams<LinearLayout.LayoutParams> {
                        leftMargin = location[0] + measuredWidth / 2 - arrow.width + 5.dpToPx()
                    }
                }


                circleView.updateLayoutParams<LinearLayout.LayoutParams> {
                    leftMargin = mLeft
                    topMargin = mTop
                    bottomMargin = mBottom
                    width = circleWidth
                    height = circleHeight
                }

            }
        }

    }

    override fun show() {
        SystemBarHelper.immersiveStatusBar(window, 0f)
        super.show()
    }

    override fun onCreateView(): View {
        val view = layoutInflater.inflate(getLayoutResId(), null)
        widthRatio(1f)
        heightRatio(1f)
        dimEnabled(false)
        return view
    }

    private var mWidth: Int = 0

    //guid_type
    companion object {
        val TYPE_WEIGHT = 0
        val TYPE_INPUT_WEIGHT = 1
        val TYPE_STUDENTS_AUDIT = 2
        val TYPE_COMMUNITY = 3
        val TYPE_POST_DIARY = 4
        val TYPE_HEALTH_REPORT = 5

        val STR_HEALTH_REPORT = "健康报告已生成，赶快点击查看吧"
        val STR_WEIGHT = "点击这里一键上秤哦~"
        val STR_INPUT_WEIGHT = "点击这里录入体重哦~"
        val STR_CUSTOMER_MANAGER = "点击这里管理新增学员~"
        val STR_COMMUNITY_POST = "点击这里可以发帖哦~"
        val STR_POST_DIARY_PIC = "点击这里上传饮食照片哦~"

        fun setBeforeShow(dialog: HomeGuideDialog) {
            dialog.apply {
                val hint = findViewById<TextView>(R.id.tv_hint)
                val circleView = findViewById<ImageView>(R.id.iv_target_circle)
                when (guildType) {
                    TYPE_WEIGHT -> hint.text = STR_WEIGHT
                    TYPE_INPUT_WEIGHT -> hint.text = STR_INPUT_WEIGHT
                    TYPE_HEALTH_REPORT -> hint.text = STR_HEALTH_REPORT
                    TYPE_STUDENTS_AUDIT -> {
                        hint.text = STR_CUSTOMER_MANAGER
                        targetShape = 0
                    }
                    TYPE_COMMUNITY -> {
                        hint.text = STR_COMMUNITY_POST
                        initSize(DisplayUtil.dip2px(35f), DisplayUtil.dip2px(35f))
                    }
                    TYPE_POST_DIARY -> {
                        circleView.setImageResource(R.drawable.home_guide_rectangle)
                        hint.text = STR_POST_DIARY_PIC
                        padding = 0
                        targetShape = 0
//                val layoutParams = circleView.layoutParams;
//                layoutParams.width = DensityUtil.dip2px(activity, 90f)
//                layoutParams.height = DensityUtil.dip2px(activity, 90f)
//                circleView.layoutParams = layoutParams
                    }
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss()
        }
        return super.onKeyDown(keyCode, event)
    }
}
