package top.guuguo.myapplication.ui.fragment

import android.app.Activity
import com.guuguo.android.lib.app.BaseCupertinoTitleActivity
import com.guuguo.android.lib.app.LBaseActivity
import com.guuguo.android.lib.app.LBaseActivitySupport
import com.guuguo.android.lib.app.LBaseFragment
import com.guuguo.android.lib.extension.log
import kotlinx.android.synthetic.main.fragment_test.*
import top.guuguo.myapplication.R

class TestFragment : LBaseFragment() {
    override fun getLayoutResId() = R.layout.fragment_test
    override fun getHeaderTitle() = "dialogFragment"
    var type = 0
    override fun initView() {
        super.initView()
        search.searchClick = {
            state.showLoading("加载中")
            when (type) {
                0 -> state.postDelayed({
                    state.restore()
                }, 2000)
                1->state.postDelayed({
                    state.showEmpty("不行了", R.drawable.empty_cute_girl_box)
                }, 2000)
                2->state.postDelayed({
                    state.showError("不行了")
                }, 2000)
            }
            type++
            if (type == 3)
                type = 0

        }
        search.onBackClick = {
            state.showEmpty("不行了", R.drawable.empty_cute_girl_box, null, null)
        }
        btn_theme.setOnClickListener {
            DialogFragment.intentTo(this)
        }
    }

    companion object {
        fun intentTo(activity: Activity) {
            "dialog fragment".log()
            "dialog fragment 2".log("可怕")
            LBaseActivity.intentTo(activity, TestFragment::class.java, BaseCupertinoTitleActivity::class.java)
        }
    }
}