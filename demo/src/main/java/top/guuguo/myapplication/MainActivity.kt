package top.guuguo.myapplication

import android.view.View
import com.guuguo.android.lib.app.LNBaseActivity

class MainActivity : LNBaseActivity() {
    override fun getLayoutResId() = R.layout.activity_main
    fun onProgressClick(v: View) {
       ProgressActivity.intentTo(activity)
    }

    override fun initView() {
        super.initView()
    }
    fun onSimpleViewClick(v: View) {

    }
}
