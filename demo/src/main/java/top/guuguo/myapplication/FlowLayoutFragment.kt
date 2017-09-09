package top.guuguo.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.guuguo.android.lib.app.LBaseFragmentSupport
import com.guuguo.android.lib.app.LNBaseActivity
import com.guuguo.android.lib.extension.dpToPx
import kotlinx.android.synthetic.main.fragment_dividerview.*
import top.guuguo.dividerview.DividerDrawable

class FlowLayoutFragment : LBaseFragmentSupport() {
    override fun getLayoutResId() = R.layout.fragment_flowlayout
    override fun initView() {
        super.initView()

    }

    companion object {
        fun intentTo(activity: Activity) {
            val intent = Intent(activity, BaseTitleActivity::class.java)
            intent.putExtra(LNBaseActivity.SIMPLE_ACTIVITY_INFO, FlowLayoutFragment::class.java)
            val bundle = Bundle()
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }
}
