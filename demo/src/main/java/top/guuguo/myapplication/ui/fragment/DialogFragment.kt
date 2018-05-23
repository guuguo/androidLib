package top.guuguo.myapplication.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.flyco.dialog.listener.OnBtnClickL
import com.guuguo.android.lib.app.BaseCupertinoTitleActivity
import com.guuguo.android.lib.app.LBaseActivitySupport
import com.guuguo.android.lib.app.LBaseFragmentSupport
import com.guuguo.android.lib.extension.dpToPx
import com.guuguo.android.lib.widget.CustomMultiWaveView
import kotlinx.android.synthetic.main.fragment_dialog.*
import kotlinx.android.synthetic.main.fragment_waveview.*
import top.guuguo.dividerview.DividerDrawable
import top.guuguo.myapplication.R
import top.guuguo.myapplication.ui.activity.BaseTitleActivity

class DialogFragment : LBaseFragmentSupport() {
    override fun getLayoutResId() = R.layout.fragment_dialog
    override fun getHeaderTitle() = "dialogFragment"
    override fun initView() {
        super.initView()
        btn_loading.setOnClickListener { dialogLoadingShow("加载中", true)}
        btn_error.setOnClickListener { dialogErrorShow("出错了")}
        btn_message.setOnClickListener {  dialogMsgShow("天气很好", "知道了", null)}
        btn_warning.setOnClickListener {  dialogWarningShow("确定继续吗", "取消", "确定", OnBtnClickL {
        })}
        btn_success.setOnClickListener { dialogCompleteShow("可以了")}
    }
    companion object {
        fun intentTo(activity: Activity) {
            LBaseActivitySupport.intentTo(activity, DialogFragment::class.java, BaseCupertinoTitleActivity::class.java)
        }
    }
}