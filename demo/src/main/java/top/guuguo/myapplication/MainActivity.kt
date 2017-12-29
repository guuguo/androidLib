package top.guuguo.myapplication

import android.view.View
import com.guuguo.android.lib.app.LBaseActivitySupport

class MainActivity : LBaseActivitySupport() {
    override fun getLayoutResId() = R.layout.activity_main
    fun onProgressClick(v: View) {
    }
    fun OnDividerViewClick(v: View) {
        DividerViewFragment.intentTo(activity);
    }

    fun onFlowLayoutClick(v: View) {
        FlowLayoutFragment.intentTo(activity)
    }
    fun onBannerClick(v:View){
        Banner2Fragment.intentTo(activity)
    }
}
