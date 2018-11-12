package top.guuguo.myapplication.ui.fragment

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.guuguo.android.drawable.CircleRunDrawable
import com.guuguo.android.lib.app.BaseCupertinoTitleActivity
import com.guuguo.android.lib.app.LBaseActivity
import com.guuguo.android.lib.app.LBaseFragment
import com.guuguo.android.lib.extension.log
import com.guuguo.android.lib.widget.simpleview.StateLayout
import kotlinx.android.synthetic.main.fragment_test.*
import top.guuguo.myapplication.R

class TestFragment : LBaseFragment() {
    override fun getLayoutResId() = R.layout.fragment_test
    override fun getHeaderTitle() = "testFragment"
    var type = 0
    override fun initView() {
        activity.let {
            (it as? BaseCupertinoTitleActivity)?.darkBar()
        }
        super.initView()
        search.searchClick = {
            doit()

        }
        search.onBackClick = {
            state.showEmpty("不行了", R.drawable.empty_cute_girl_box, null, null)
        }
        btn_theme.setOnClickListener {
            DialogFragment.intentTo(this)
        }
        btn_change.setOnClickListener {
            doit()
        }
    }

    fun doit() {
        state.layoutRes = R.layout.widget_include_simple_empty_view1
        StateLayout.loadingDrawableClass = null
        state.showLoading("", true)
        when (type) {
            0 -> state.postDelayed({
                if (!state.isLoading)
                    state.showLoading("", true)
                state.postDelayed({
                    state.restore()
                }, 1000)
            }, 1000)
            1 -> state.postDelayed({
                state.showEmpty("不行了", R.drawable.empty_cute_girl_box)
            }, 1000)
            2 -> state.postDelayed({
                state.showError("网络异常，请稍候重试", "重试", listener = View.OnClickListener {
                    type = 2
                    doit()
                }, imgRes = 0)
            }, 1000)
            3 -> state.postDelayed({
                state.showCustomView(R.layout.dialog_custom_warning)
            }, 1000)
        }
        type++
        if (type == 4)
            type = 0
    }

    companion object {
        fun intentTo(activity: Activity) {
            "dialog fragment".log()
            "dialog fragment 2".log("可怕")
            LBaseActivity.intentTo(activity, TestFragment::class.java, BaseCupertinoTitleActivity::class.java)
        }
    }
}