package top.guuguo.myapplication.ui.fragment

import android.app.Activity
import android.widget.TextView
import com.guuguo.android.dialog.dialog.CustomAlertDialog
import com.guuguo.android.dialog.dialog.EditAlertDialog
import com.guuguo.android.dialog.utils.DialogSettings
import com.guuguo.android.lib.app.BaseCupertinoTitleActivity
import com.guuguo.android.lib.app.LBaseActivitySupport
import com.guuguo.android.lib.app.LBaseFragmentSupport
import io.reactivex.Completable
import kotlinx.android.synthetic.main.fragment_dialog.*
import top.guuguo.myapplication.R
import java.util.concurrent.TimeUnit

class DialogFragment : LBaseFragmentSupport() {
    override fun getLayoutResId() = R.layout.fragment_dialog
    override fun getHeaderTitle() = "dialogFragment"
    override fun isNavigationBack() = false
    override fun initView() {
        super.initView()
        btn_theme.setOnClickListener {
            if (DialogSettings.tip_theme == DialogSettings.THEME_LIGHT) {
                DialogSettings.tip_theme = DialogSettings.THEME_DARK
            } else {
                DialogSettings.tip_theme = DialogSettings.THEME_LIGHT
            }
        }
        btn_loading.setOnClickListener {
            dialogLoadingShow("加载中")
            Completable.complete().delay(2, TimeUnit.SECONDS).subscribe {
                dialogDismiss()
            }
        }
        btn_error.setOnClickListener { dialogErrorShow("出错了") }
        btn_message.setOnClickListener { dialogMsgShow("天气很好", "知道了", null) }
        btn_warning.setOnClickListener {
            dialogWarningShow("确定继续吗", "取消", "确定")
        }
        btn_success.setOnClickListener { dialogCompleteShow("可以了哈哈哈哈你好啊 啊啊 啊") }
        btn_alert_edit.setOnClickListener {
            EditAlertDialog(activity).title("填写").show()
        }
        btn_alert_custom.setOnClickListener {
            CustomAlertDialog(activity).contentView(TextView(activity)).title("填写").show()
        }
    }

    companion object {
        fun intentTo(activity: Activity) {
            LBaseActivitySupport.intentTo(activity, DialogFragment::class.java, BaseCupertinoTitleActivity::class.java)
        }
    }
}