package top.guuguo.myapplication.ui.activity

import android.view.View
import com.guuguo.android.lib.app.LBaseActivitySupport
import top.guuguo.myapplication.ui.fragment.NavigatorLayoutFragment
import top.guuguo.myapplication.R
import top.guuguo.myapplication.ui.fragment.DividerViewFragment
import top.guuguo.myapplication.ui.fragment.FlowLayoutFragment

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
}
