package top.guuguo.myapplication.ui

import android.app.Activity
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.doOnLayout
import androidx.core.view.doOnNextLayout
import androidx.core.view.updateLayoutParams
import com.guuguo.android.dialog.base.BaseDialog
import com.guuguo.android.lib.extension.dpToPx
import com.guuguo.android.lib.utils.DisplayUtil
import top.guuguo.myapplication.R

/**
 * PackageName com.bianla.app.widget.dialog
 * Created by admin on 2016/11/29.
 */

class HomeGuideDialog(activity: Activity, var targetView: View, var guildType: Int) : BaseDialog<HomeGuideDialog>(activity) {
    override fun setUiBeforShow() {
        findViewById<View>(R.id.iv_i_know).setOnClickListener {
            dismiss()
        }
        val arrow = findViewById<ImageView>(R.id.iv_arrow)
        val hint = findViewById<ImageView>(R.id.tv_hint)
        val circleView = findViewById<ImageView>(R.id.iv_target_circle)
        val btn = findViewById<ImageView>(R.id.iv_i_know)
        when (guildType) {
            TYPE_WEIGHT -> hint.setImageResource(R.drawable.home_guid_weight)
            TYPE_INPUT_WEIGHT -> hint.setImageResource(R.drawable.home_guid_input_weight)
        }
        targetView.doOnNextLayout {
            it.apply {
                val location = IntArray(2)
                getLocationOnScreen(location)
                val mLeft = (location[0] + width / 2) - circleView.width / 2
                var mTop = 0
                var mBottom = 0
                if (location[1] > getScreenHeight() / 2) {
                    val content = mOnCreateView as LinearLayout
                    content.removeAllViews()
                    content.addView(btn)
                    content.addView(hint)
                    content.addView(arrow)
                    content.addView(circleView)
                    content.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                    mBottom = getScreenHeight()-(location[1] + measuredHeight / 2) - circleView.height / 2
                    arrow.setImageResource(R.drawable.guild_arrow_right_bottom)
                    if (location[0] < DisplayUtil.getScreenWidth() / 2) {
                        arrow.scaleX = -1f
                        arrow.scaleY = 1f
                    }
                } else {
                    mTop = (location[1] + measuredHeight / 2) - circleView.height / 2
                    arrow.setImageResource(R.drawable.guild_arrow_left_top)
                    if (location[0] >= DisplayUtil.getScreenWidth() / 2) {
                        arrow.scaleX = -1f
                        arrow.scaleY = 1f
                    }
                }
                if (location[0] < DisplayUtil.getScreenWidth() / 2) {
                    arrow.updateLayoutParams<LinearLayout.LayoutParams> {
                        leftMargin = location[0] + measuredWidth / 2 + 15.dpToPx()
                    }
                } else {
                    arrow.updateLayoutParams<LinearLayout.LayoutParams> {
                        leftMargin = location[0] + measuredWidth / 2 - 15.dpToPx()
                    }
                }


                circleView.updateLayoutParams<LinearLayout.LayoutParams> {
                    leftMargin = mLeft
                    topMargin = mTop
                    bottomMargin=mBottom
                }
            }
        }
    }

    override fun onCreateView(): View {
        val view = layoutInflater.inflate(R.layout.home_layout_guild_home_without_scale, null)
        widthRatio(1f)
        heightRatio(1f)
        return view
    }

    private var mWidth: Int = 0

    //guid_type
    companion object {
        val TYPE_WEIGHT = 0
        val TYPE_INPUT_WEIGHT = 1
        val TYPE_STUDENTS_AUDIT = 2

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss()
        }
        return super.onKeyDown(keyCode, event)
    }
}
