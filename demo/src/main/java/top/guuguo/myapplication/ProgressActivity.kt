package top.guuguo.myapplication

import android.app.Activity
import android.content.Intent
import com.guuguo.android.lib.app.LBaseActivitySupport
import com.guuguo.android.lib.app.LNBaseActivity
import kotlinx.android.synthetic.main.activity_progress.*

class ProgressActivity : LBaseActivitySupport() {
    override fun getLayoutResId() = R.layout.activity_progress
    override fun initView() {
        super.initView()
    
    }

    companion object {
        fun intentTo(activity: Activity) {
            val intent = Intent(activity, ProgressActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
