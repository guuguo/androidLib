package top.guuguo.myapplication.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.guuguo.android.lib.app.BaseCupertinoTitleActivity
import com.guuguo.android.lib.app.LBaseActivitySupport
import com.guuguo.android.lib.app.LBaseFragmentSupport
import com.guuguo.android.lib.extension.dpToPx
import com.guuguo.android.lib.widget.CustomMultiWaveView
import kotlinx.android.synthetic.main.fragment_waveview.*
import top.guuguo.dividerview.DividerDrawable
import top.guuguo.myapplication.R
import top.guuguo.myapplication.ui.activity.BaseTitleActivity

class WaveViewFragment : LBaseFragmentSupport() {
    override fun getLayoutResId() = R.layout.fragment_waveview
    override fun getHeaderTitle() = "waveView"
    override fun initView() {
        super.initView()
        wave_view.addWaves(arrayOf(
                CustomMultiWaveView.WaveBean(1000f, 4500, 48.dpToPx())
                , CustomMultiWaveView.WaveBean(1000f, 4200, 44.dpToPx())
                , CustomMultiWaveView.WaveBean(1000f, 3800, 40.dpToPx(), Color.WHITE)))
        wave_view.start()

        activity.let {
            if (it is BaseCupertinoTitleActivity) {
                it.getFunctionView().drawable = ContextCompat.getDrawable(activity, R.drawable.ic_search)
            }
        }
    }

    companion object {
        fun intentTo(activity: Activity) {
            LBaseActivitySupport.intentTo(activity, WaveViewFragment::class.java, BaseCupertinoTitleActivity::class.java)
        }
    }
}