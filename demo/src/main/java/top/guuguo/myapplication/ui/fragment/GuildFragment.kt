package top.guuguo.myapplication.ui.fragment

import android.app.Activity
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.guuguo.android.lib.app.BaseCupertinoTitleActivity
import com.guuguo.android.lib.app.LBaseActivitySupport
import com.guuguo.android.lib.app.LBaseFragment
import com.guuguo.android.lib.app.LBaseFragmentSupport
import com.guuguo.android.lib.extension.dpToPx
import com.guuguo.android.lib.widget.CustomMultiWaveView
import kotlinx.android.synthetic.main.fragment_guild.*
import kotlinx.android.synthetic.main.fragment_waveview.*
import top.guuguo.myapplication.R
import top.guuguo.myapplication.R.id.btn
import top.guuguo.myapplication.R.id.wave_view
import top.guuguo.myapplication.databinding.FragmentGuildBinding
import top.guuguo.myapplication.ui.HomeGuideDialog

class GuildFragment : LBaseFragment() {
    override fun getLayoutResId() = R.layout.fragment_guild
    override fun getHeaderTitle() = "引导"
    override fun isNavigationBack() = true
    lateinit var binding: FragmentGuildBinding
    override fun setLayoutResId(inflater: LayoutInflater?, resId: Int, container: ViewGroup?): View {
        return super.setLayoutResId(inflater, resId, container).also { binding = DataBindingUtil.bind(it)!! }
    }

    override fun initView() {
        super.initView()

        activity.let {
            if (it is BaseCupertinoTitleActivity) {
                it.getFunctionView().drawable = ContextCompat.getDrawable(activity, R.drawable.ic_search)
            }
        }
        HomeGuideDialog(activity, binding.btn, HomeGuideDialog.TYPE_INPUT_WEIGHT).show()
        HomeGuideDialog(activity, binding.btnTop, HomeGuideDialog.TYPE_INPUT_WEIGHT).show()
        HomeGuideDialog(activity, (activity as BaseCupertinoTitleActivity).getFunctionView(), HomeGuideDialog.TYPE_INPUT_WEIGHT).show()
    }

    companion object {
        fun intentTo(activity: Activity) {
            LBaseActivitySupport.intentTo(activity, GuildFragment::class.java, BaseCupertinoTitleActivity::class.java)
        }
    }
}