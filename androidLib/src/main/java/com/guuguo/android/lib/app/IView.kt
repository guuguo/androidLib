package com.guuguo.android.lib.app

import android.app.Dialog
import android.content.DialogInterface
import com.flyco.dialog.listener.OnBtnClickL
import com.guuguo.android.lib.widget.dialog.TipDialog
import com.guuguo.android.lib.widget.dialog.WarningDialog
import com.trello.rxlifecycle2.LifecycleProvider

/**
 * Package Name : com.hesheng.orderpad.app.base
 *
 * @author : guuguo
 * @since 2018/5/18
 */
interface IView<E>  : LifecycleProvider<E>{
    fun loadData()

    fun dialogLoadingShow(msg: String, canTouchCancel: Boolean = false, maxDelay: Long = 0, listener: DialogInterface.OnDismissListener? = null): TipDialog?

    fun dialogErrorShow(msg: String, listener: DialogInterface.OnDismissListener? = null, delayTime: Int = 1500): TipDialog?

    fun dialogCompleteShow(msg: String, listener: DialogInterface.OnDismissListener? = null, delayTime: Int = 800): TipDialog?

    fun dialogMsgShow(msg: String, btnText: String, listener: OnBtnClickL?): WarningDialog?

    fun dialogWarningShow(msg: String, cancelStr: String, confirmStr: String, listener: OnBtnClickL?): WarningDialog?

    fun showDialogOnMain(dialog: Dialog)

    fun dialogDismiss()

    var activity:LBaseActivitySupport
}