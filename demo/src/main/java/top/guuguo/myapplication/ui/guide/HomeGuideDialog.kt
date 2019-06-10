package top.guuguo.myapplication.ui.guide

import android.app.Activity
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.doOnNextLayout
import androidx.core.view.updateLayoutParams
import com.guuguo.android.dialog.base.BaseDialog
import com.guuguo.android.lib.widget.GuideHighLightBgView
import com.guuguo.android.lib.extension.dpToPx
import com.guuguo.android.lib.systembar.SystemBarHelper
import com.guuguo.android.lib.utils.DisplayUtil
import com.guuguo.android.lib.widget.ShadowFrameLayout
import top.guuguo.myapplication.R

/**
 * PackageName com.bianla.app.widget.dialog
 * Created by admin on 2016/11/29.
 */

class HomeGuideDialog(var activity: Activity, var targetView: View, var guildType: Int) : BaseDialog<HomeGuideDialog>(activity) {
    fun getLayoutResId() = R.layout.home_layout_guild_home_without_scale
    var padding = 0.dpToPx()
    /**0没有，1圆圈，2方形*/
    var targetShape = 2
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

        val sflMsg = findViewById<ShadowFrameLayout>(R.id.sfl_msg)
        val circleView = findViewById<ImageView>(R.id.iv_target_circle)
        val guideContent = findViewById<GuideHighLightBgView>(R.id.guild_content)
        val tvHint = findViewById<TextView>(R.id.tv_hint)
        guideContent.setOnClickListener {
            dismiss()
        }
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


                ///传进来的view的位置
                val location = IntArray(2)
                getLocationOnScreen(location)

                ///targetView的重中点位置
                val lCx = location[0] + width / 2
                val lCY = location[1] + height / 2
                guideContent.targetCX = lCx.toFloat()
                guideContent.targetCY = lCY.toFloat()
                when (targetShape) {
                    1 -> {
                        guideContent.isCircle = true
                        guideContent.targetRadius = Math.sqrt(circleWidth*circleWidth+circleHeight *circleHeight.toDouble()).toFloat()/ 2
                    }
                    2 -> {
                        guideContent.isCircle = false
                        guideContent.targetWidth = circleWidth
                        guideContent.targetHeight = circleHeight
                    }
                }
                guideContent.invalidate()

                val mLeft = lCx - circleWidth / 2
                var mTop = 0
                var mBottom = 0

                val bgLocation = IntArray(2)
                guideContent.getLocationOnScreen(bgLocation)
                //因为 topMargin 改变,measuredHeight 不会马上改变，所以直接测量计算的值拿来用
                var contentHeight = guideContent.measuredHeight
                //如果背景顶部是状态栏高度，则 margin 等于负的状态栏高度，适配三星部分手机状态栏顶不上去
                if (bgLocation[1] == SystemBarHelper.getStatusBarHeight(mContext)) {
                    guideContent.updateLayoutParams<LinearLayout.LayoutParams> {
                        contentHeight += SystemBarHelper.getStatusBarHeight(context)
                        topMargin = -SystemBarHelper.getStatusBarHeight(mContext)
                    }
                }

                ///圈圈在下面的时候
                if (location[1] > getScreenDisplayHeight() / 2) {
                    val content = mOnCreateView as LinearLayout
                    content.removeAllViews()
                    content.addView(sflMsg)
                    content.addView(circleView)
                    mTop = 0
                    content.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                    mBottom = contentHeight - (location[1] + measuredHeight / 2) - circleHeight / 2

                    sflMsg.triangleAlign = ShadowFrameLayout.TRIANGLE_ALIGN_BOTTOM
                } else {
                    mBottom = 0
                    mTop = lCY - circleHeight / 2
                }
                when {
                    ///宽度小的时候，直接targetView居中显示
                    sflMsg.measuredWidth / 2 < Math.min(lCx, guideContent.measuredWidth - lCx) -> {
                        sflMsg.updateLayoutParams<LinearLayout.LayoutParams> {
                            gravity = Gravity.START
                            leftMargin = lCx - sflMsg.measuredWidth / 2
                        }
                        tvHint.gravity = Gravity.START
                        sflMsg.triangleStartExtent = sflMsg.measuredWidth / 2f
                    }

                    ///在左边的时候
                    location[0] < DisplayUtil.getScreenWidth() / 2 -> {
                        sflMsg.updateLayoutParams<LinearLayout.LayoutParams> {
                            gravity = Gravity.START
                        }
                        tvHint.gravity = Gravity.START
                        sflMsg.triangleStartExtent = lCx.toFloat()

                        ///在右边的时候
                    }
                    else -> {
                        sflMsg.updateLayoutParams<LinearLayout.LayoutParams> {
                            gravity = Gravity.END
                        }
                        tvHint.gravity = Gravity.END
                        sflMsg.triangleStartExtent = lCx.toFloat() - guideContent.measuredWidth + sflMsg.measuredWidth
                    }
                }
                ///tvHint 最大宽度减掉各种margin
                tvHint.maxWidth = guideContent.measuredWidth - 30.dpToPx() - 27.dpToPx() - 47.dpToPx().toInt()

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
        val TYPE_INPUT_WEIGHT = 1
        val TYPE_STUDENTS_AUDIT = 2
        val TYPE_COMMUNITY = 3
        val TYPE_POST_DIARY = 4
        val TYPE_HEALTH_REPORT = 5
        ///消息
        val TYPE_MSG = 6
        val TYPE_EVALUATE = 7 //上称
        val TYPE_EXCLUSIVE_COACH_SEARCH = 8
        val TYPE_EXCLUSIVE_COACH_TALK = 9
        ///V认证
        val TYPE_V_CERTIFICATION = 10
        ///糖化测验
        val TYPE_SACCHARIFICATION_EXAM = 11
        val TYPE_SWIPE_DELETE = 12

        private val STR_HEALTH_REPORT = "来这里查看你的身体健康数据呦~"
        private val STR_MSG = "消息通知迁到这里了呦~\n可以查看系统消息，教练聊天\n等消息了~"
        private val STR_INPUT_WEIGHT = "点击这里录入体重哦~"
        private val STR_CUSTOMER_MANAGER = "点击这里管理新增学员~"
        private val STR_COMMUNITY_POST = "点击这里可以发帖哦~"
        private val STR_POST_DIARY_PIC = "点击这里上传饮食照片哦~"
        private val STR_EVALUATE = "每天快速上秤"
        private val STR_EXCLUSIVE_COACH_SEARCH = "快来这里找到专属教练吧~"
        private val STR_EXCLUSIVE_COACH_TALK = "您的专属教练就在这里呦~，赶快联系吧~"
        private val STR_V_CERTIFICATION = "教练和V认证抢单都在这里呦~"
        private val STR_SACCHARIFICATION_EXAM = "新增功能，可以检测身体的糖化成份呦～"
        private val STR_SWIPE_DELETE = "左滑可删除此条消息~"


        fun setBeforeShow(dialog: HomeGuideDialog) {
            dialog.apply {
                val hint = findViewById<TextView>(R.id.tv_hint)
                val circleView = findViewById<ImageView>(R.id.iv_target_circle)
                when (guildType) {
                    TYPE_INPUT_WEIGHT -> hint.text = STR_INPUT_WEIGHT
                    TYPE_HEALTH_REPORT -> hint.text = STR_HEALTH_REPORT
                    TYPE_MSG -> hint.text = STR_MSG
                    TYPE_EVALUATE -> hint.text = STR_EVALUATE
                    TYPE_EXCLUSIVE_COACH_SEARCH -> hint.text = STR_EXCLUSIVE_COACH_SEARCH
                    TYPE_EXCLUSIVE_COACH_TALK -> hint.text = STR_EXCLUSIVE_COACH_TALK
                    TYPE_V_CERTIFICATION -> hint.text = STR_V_CERTIFICATION
                    TYPE_SACCHARIFICATION_EXAM -> hint.text = STR_SACCHARIFICATION_EXAM
                    TYPE_SWIPE_DELETE -> hint.text = STR_SWIPE_DELETE
                    TYPE_STUDENTS_AUDIT -> {
                        hint.text = STR_CUSTOMER_MANAGER
                        padding = 3.dpToPx()
                        targetShape = 1
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
