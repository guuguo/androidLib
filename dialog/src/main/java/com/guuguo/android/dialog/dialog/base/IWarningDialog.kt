package com.guuguo.android.dialog.dialog.base

import android.content.Context
import com.guuguo.android.dialog.base.BaseDialog

//子类必须实现 construct(context)
abstract class  IWarningDialog (context: Context) : BaseDialog<IWarningDialog>(context) {

    abstract fun setTitle(title: String): IWarningDialog
    abstract fun setMessage(message: String): IWarningDialog
    abstract fun setBtnNum(btnNum: Int): IWarningDialog
    abstract fun setBtnText(vararg text: String): IWarningDialog
    abstract fun setBtnClick(vararg clicks: ((v: IWarningDialog) -> Unit)?): IWarningDialog
    abstract fun setPositiveBtnPosition(btnPosition: Int): IWarningDialog
}