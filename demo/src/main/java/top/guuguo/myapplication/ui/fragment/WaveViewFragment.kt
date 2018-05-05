package top.guuguo.myapplication.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
    override fun getHeaderTitle()="waveView"
    override fun initView() {
        super.initView()

        wave_view.start()
    }

    companion object {
        fun intentTo(activity: Activity) {
            LBaseActivitySupport.intentTo(activity,  WaveViewFragment::class.java, BaseCupertinoTitleActivity::class.java)
        }
    }
}