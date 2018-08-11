package top.guuguo.myapplication.ui.fragment

import android.app.Activity
import com.guuguo.android.lib.app.BaseCupertinoTitleActivity
import com.guuguo.android.lib.app.LBaseActivitySupport
import com.guuguo.android.lib.app.LBaseFragmentSupport
import kotlinx.android.synthetic.main.fragment_test.*
import top.guuguo.myapplication.R

class TestFragment : LBaseFragmentSupport() {
    override fun getLayoutResId() = R.layout.fragment_test
    override fun getHeaderTitle() = "dialogFragment"
    override fun initView() {
        super.initView()
        search.searchClick = {
            state.showLoading("加载中")
            state.postDelayed({
                state.restore()
            },2000)
        }
        search.onBackClick={
            state.showEmpty("不行了",R.drawable.empty_cute_girl_box,null,null)
        }
        btn_theme.setOnClickListener {
            DialogFragment.intentTo(this)
        }
    }

    companion object {
        fun intentTo(activity: Activity) {
            LBaseActivitySupport.intentTo(activity, TestFragment::class.java, BaseCupertinoTitleActivity::class.java)
        }
    }
}