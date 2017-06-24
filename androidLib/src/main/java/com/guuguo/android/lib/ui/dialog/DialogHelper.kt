package com.guuguo.android.lib.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.Gravity
import com.flyco.dialog.listener.OnBtnClickL
import com.guuguo.android.lib.utils.CommonUtil
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by mimi on 2016-11-11.
 */
object DialogHelper {
    val callMaps = HashMap<Context, CompositeDisposable>()
    private var mLoadingDialog: StateDialog? = null

    fun addCall(context: Context, dispose: Disposable) {
        if (callMaps.containsKey(context))
            callMaps[context]?.add(dispose)
        else {
            val composite = CompositeDisposable()
            callMaps.put(context, composite)
            composite.add(dispose)
        }
    }

    fun clearCalls(context: Context) {
        callMaps[context]?.clear()
        callMaps.remove(context)
    }

    fun dialogLoadingShow(context: Context, msg: String, canTouchCancel: Boolean = false, maxDelay: Long = 0, listener: DialogInterface.OnDismissListener? = null) {
        var msg = msg
        if (TextUtils.isEmpty(msg))
            msg = "加载中"
        if (mLoadingDialog == null)
            mLoadingDialog = StateDialog(context)
        mLoadingDialog!!.stateStyle(StateDialog.STATE_STYLE.loading)
                .content(msg)

        if (maxDelay > 0)
            dialogDismiss(context, maxDelay, mLoadingDialog, listener)
        mLoadingDialog!!.setCanceledOnTouchOutside(canTouchCancel)
        showDialogOnMain(context, mLoadingDialog!!)
    }

    fun dialogMsgShow(context: Context, msg: String, btnText: String, listener: OnBtnClickL?) {
        val normalDialog = WarningDialog(context)
                .contentGravity(Gravity.CENTER)
                .content(CommonUtil.getSafeString(msg))
                .btnNum(1)
                .btnText(btnText)
        normalDialog.setOnBtnClickL(OnBtnClickL {
            normalDialog.dismiss()
            listener?.onBtnClick()
        })
        showDialogOnMain(context, normalDialog)
    }

    fun dialogStateShow(context: Context, msg: String, listener: DialogInterface.OnDismissListener?, stateStyle: Int, delayTime: Long) {
        val stateDialog = StateDialog(context)
                .stateStyle(stateStyle)
                .content(CommonUtil.getSafeString(msg))

        stateDialog.setCanceledOnTouchOutside(false)
        showDialogOnMain(context, stateDialog)
        dialogDismiss(context, delayTime, stateDialog, listener)
    }

    fun dialogWarningShow(context: Context, msg: String, cancelStr: String, confirmStr: String, listener: OnBtnClickL?) {
        val normalDialog = WarningDialog(context)
                .contentGravity(Gravity.CENTER)
                .content(CommonUtil.getSafeString(msg))
                .btnNum(2)
                .btnText(cancelStr, confirmStr)
        normalDialog.setCanceledOnTouchOutside(false)

        normalDialog.setOnBtnClickL(null, OnBtnClickL {
            normalDialog.dismiss()
            listener?.onBtnClick()
        })
        showDialogOnMain(context, normalDialog)
    }

    fun showDialogOnMain(context: Context, dialog: Dialog) {
        Completable.complete().observeOn(AndroidSchedulers.mainThread()).subscribe(object : CompletableObserver {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {}
            override fun onComplete() {
                dialog.show()
            }
        })
    }

    fun dialogDismiss(context: Context, delay: Long = 0, dialog: Dialog? = mLoadingDialog, listener: DialogInterface.OnDismissListener? = null) {
        Completable.complete().delay(delay, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {
                        addCall(context, d)
                    }

                    override fun onComplete() {
                        dialog?.dismiss()
                        listener?.onDismiss(dialog)
                    }

                    override fun onError(e: Throwable) {}
                })
    }
}
