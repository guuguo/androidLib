package top.guuguo.myapplication.ui.fragment

import android.app.Activity
import android.support.v4.app.Fragment
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.guuguo.android.dialog.dialog.CupertinoWarningDialog
import com.guuguo.android.dialog.dialog.CustomAlertDialog
import com.guuguo.android.dialog.dialog.DefaultWarningDialog
import com.guuguo.android.dialog.dialog.NewEditAlertDialog
import com.guuguo.android.dialog.utils.DialogHelper
import com.guuguo.android.dialog.utils.DialogSettings
import com.guuguo.android.lib.app.BaseCupertinoTitleActivity
import com.guuguo.android.lib.app.LBaseActivitySupport
import com.guuguo.android.lib.app.LBaseFragmentSupport
import com.guuguo.android.lib.app.SupportFragment
import com.guuguo.android.lib.extension.dpToPx
import com.guuguo.android.lib.extension.log
import io.reactivex.Completable
import kotlinx.android.synthetic.main.fragment_dialog.*
import top.guuguo.myapplication.R
import top.guuguo.myapplication.WarningDialog
import java.util.concurrent.TimeUnit

@Route(path = "/demo/dialog")
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
        btn_warning_type.setOnClickListener {
            when {
                DialogHelper.warningDialogClass.name == CupertinoWarningDialog::class.java.name -> DialogHelper.warningDialogClass = DefaultWarningDialog::class.java
                DialogHelper.warningDialogClass.name == DefaultWarningDialog::class.java.name -> DialogHelper.warningDialogClass = WarningDialog::class.java
                else -> DialogHelper.warningDialogClass =CupertinoWarningDialog::class.java
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
            NewEditAlertDialog(activity).paddingVertical(10.dpToPx()).title("填写").btnText("取消", "完成").btnClick({ it.dismiss() }, { it.dismiss() }).show()
        }
        btn_alert_custom.setOnClickListener {
            CustomAlertDialog(activity).contentView(TextView(activity)).title("填写").show()
        }
    }

    companion object {
        fun intentTo(activity: Activity) {
            LBaseActivitySupport.intentTo(activity, DialogFragment::class.java, BaseCupertinoTitleActivity::class.java)
            "dialog fragment".log()
            "dialog fragment 2".log("可怕")
        }
        fun intentTo(fragment: SupportFragment) {
            LBaseActivitySupport.intentTo(fragment, DialogFragment::class.java, BaseCupertinoTitleActivity::class.java)
            "dialog fragment".log()
            "dialog fragment 2".log("可怕")
        }
    }
}