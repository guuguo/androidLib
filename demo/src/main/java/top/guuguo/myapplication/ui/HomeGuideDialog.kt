package top.guuguo.myapplication.ui

import android.app.Activity
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import com.guuguo.android.dialog.base.BaseDialog
import com.guuguo.android.lib.extension.dpToPx
import com.guuguo.android.lib.utils.DisplayUtil
import top.guuguo.myapplication.R

/**
 * PackageName com.bianla.app.widget.dialog
 * Created by admin on 2016/11/29.
 */

class HomeGuideDialog(activity: Activity, var targetView: View,var guildType: Int) : BaseDialog<HomeGuideDialog>(activity) {
    override fun setUiBeforShow() {
        findViewById<View>(R.id.iv_i_know).setOnClickListener {
            dismiss()
        }
        val arrow = findViewById<ImageView>(R.id.iv_arrow)
        val hint = findViewById<ImageView>(R.id.tv_hint)
        val circleView = findViewById<ImageView>(R.id.iv_target_circle)
        when (guildType) {
            TYPE_WEIGHT -> hint.setImageResource(R.drawable.home_guid_weight)
            TYPE_INPUT_WEIGHT -> hint.setImageResource(R.drawable.home_guid_input_weight)
        }
        circleView.post {
            targetView.apply {
                val location = IntArray(2)
                getLocationOnScreen(location)
                val mLeft = (location[0] + width / 2) - circleView.width / 2
                val mTop = (location[1] + height / 2) - circleView.height / 2
                if (location[0] < DisplayUtil.getScreenWidth() / 2) {
                    arrow.setImageResource(R.drawable.guild_arrow_left)
                    arrow.updateLayoutParams<LinearLayout.LayoutParams> {
                        leftMargin = location[0] + width / 2 + 15.dpToPx()
                    }
                } else {
                    arrow.setImageResource(R.drawable.guild_arrow_right)
                    arrow.updateLayoutParams<LinearLayout.LayoutParams> {
                        leftMargin = location[0] + width / 2 - 15.dpToPx()
                    }
                }

                circleView.updateLayoutParams<LinearLayout.LayoutParams> {
                    leftMargin = mLeft
                    topMargin = mTop
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

//    init {
//
//        setCancelable(false)
//        mWidth = activity.windowManager.defaultDisplay.width
//        window!!.requestFeature(Window.FEATURE_NO_TITLE)
//        setContentView(R.layout.home_layout_guild_home_without_scale)
//        val layoutParams = window!!.attributes
//        layoutParams.width = mWidth
//        layoutParams.height = activity.windowManager.defaultDisplay.height
//        window!!.attributes = layoutParams
//        SystemBarHelper.immersiveStatusBar(window, 0f)
//
//
//    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss()
        }
        return super.onKeyDown(keyCode, event)
    }
}
