package top.guuguo.myapplication.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.guuguo.android.lib.app.LBaseActivity
import kotlinx.android.synthetic.main.activity_progress.*
import top.guuguo.myapplication.R

@Route(path = "/test/progress")
class ProgressActivity : LBaseActivity() {
    override fun getLayoutResId() = R.layout.activity_progress
    override fun getToolBar() = findViewById<Toolbar>(R.id.id_tool_bar)
    override fun initView() {
        super.initView()
        state.showLoading(name)
    }

    companion object {
        fun intentTo(activity: Activity) {
            val intent = Intent(activity, ProgressActivity::class.java)
            activity.startActivity(intent)
        }
    }
    @JvmField
    @Autowired
    var name: String = ""

    override fun initVariable(savedInstanceState: Bundle?) {
        super.initVariable(savedInstanceState)
        ARouter.getInstance().inject(this)
    }

}
