package top.guuguo.myapplication.ui.activity

import android.app.Activity
import android.content.Intent
import com.guuguo.android.lib.app.LBaseActivitySupport
import kotlinx.android.synthetic.main.activity_progress.*
import top.guuguo.myapplication.R

class ProgressActivity : LBaseActivitySupport() {
    override fun getLayoutResId() = R.layout.activity_progress
    override fun initView() {
        super.initView()
        state.showLoading("好可怕")
    }

    companion object {
        fun intentTo(activity: Activity) {
            val intent = Intent(activity, ProgressActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
