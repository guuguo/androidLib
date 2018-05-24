package top.guuguo.myapplication.ui.activity

import android.view.View
import com.guuguo.android.lib.app.LBaseActivitySupport
import com.guuguo.android.lib.extension.doAvoidDouble
import top.guuguo.myapplication.R
import top.guuguo.myapplication.ui.fragment.*

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
            DialogFragment.intentTo(activity)
        }
    }
}
