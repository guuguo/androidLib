package top.guuguo.myapplication.ui.activity

import android.content.DialogInterface
import android.view.View
import com.guuguo.android.lib.app.LBaseActivitySupport
import com.guuguo.android.lib.extension.doAvoidDouble
import com.guuguo.android.lib.extension.log
import com.guuguo.android.lib.widget.dialog.DialogHelper
import com.guuguo.android.lib.widget.dialog.TipDialog
import com.guuguo.android.lib.widget.drawable.CircularDrawable
import top.guuguo.myapplication.ui.fragment.NavigatorLayoutFragment
import top.guuguo.myapplication.R
import top.guuguo.myapplication.ui.fragment.DividerViewFragment
import top.guuguo.myapplication.ui.fragment.FlowLayoutFragment
import top.guuguo.myapplication.ui.fragment.WaveViewFragment

class MainActivity : LBaseActivitySupport() {
    override fun getLayoutResId() = R.layout.activity_main
    fun onProgressClick(v: View) {
        v.doAvoidDouble {
            ProgressActivity.intentTo(activity)
        }
    }

    fun OnDividerViewClick(v: View) {
        v.doAvoidDouble {
            DividerViewFragment.intentTo(activity);
        }
    }

    fun onFlowLayoutClick(v: View) {
        v.doAvoidDouble {
            FlowLayoutFragment.intentTo(activity)
        }
    }

    fun onBannerClick(v: View) {
        v.doAvoidDouble {
            NavigatorLayoutFragment.intentTo(activity)
        }
    }

    fun onWaveClick(v: View) {
        v.doAvoidDouble {
            WaveViewFragment.intentTo(activity)
        }
    }

    fun onDialogShow(v: View) {
        v.doAvoidDouble {
            dialogLoadingShow("")
        }
    }

    override fun dialogLoadingShow(msg: String, canTouchCancel: Boolean, maxDelay: Long, listener: DialogInterface.OnDismissListener?): TipDialog? {
        return DialogHelper.dialogLoadingShow(activity, msg, canTouchCancel, maxDelay, listener, CircularDrawable())

    }

}
