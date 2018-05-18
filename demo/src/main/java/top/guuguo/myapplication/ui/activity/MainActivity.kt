package top.guuguo.myapplication.ui.activity

import android.content.DialogInterface
import android.view.View
import com.guuguo.android.lib.app.LBaseActivitySupport
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
        ProgressActivity.intentTo(activity)
    }
    fun OnDividerViewClick(v: View) {
        DividerViewFragment.intentTo(activity);
    }

    fun onFlowLayoutClick(v: View) {
        FlowLayoutFragment.intentTo(activity)
    }
    fun onBannerClick(v:View){
        NavigatorLayoutFragment.intentTo(activity)
    }
    fun onWaveClick(v:View){
        WaveViewFragment.intentTo(activity)
    }
    fun onDialogShow(v:View){
        dialogLoadingShow("")
    }

    override fun dialogLoadingShow(msg: String, canTouchCancel: Boolean, maxDelay: Long, listener: DialogInterface.OnDismissListener?): TipDialog? {
        return DialogHelper.dialogLoadingShow(activity, msg, canTouchCancel, maxDelay, listener,CircularDrawable())

    }

}
