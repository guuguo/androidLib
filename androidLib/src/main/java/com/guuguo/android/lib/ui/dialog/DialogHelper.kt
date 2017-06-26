package com.guuguo.android.lib.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.Gravity
import com.flyco.dialog.listener.OnBtnClickL
import com.guuguo.android.lib.utils.CommonUtil
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleObserver
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
    //    private var mLoadingDialog: StateDialog? = null
    private var mLoadingDialogs = HashMap<Context, StateDialog>()
    private var mDialogs = HashMap<Context, ArrayList<Dialog>>()

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
        mLoadingDialogs[context]?.dismiss()
        mLoadingDialogs.remove(context)
        mDialogs[context]?.forEach { it.dismiss() }
        mDialogs.remove(context)
    }

    fun dialogLoadingShow(context: Context, msg: String, canTouchCancel: Boolean = false, maxDelay: Long = 0, listener: DialogInterface.OnDismissListener? = null) {
        var msg = msg
        if (TextUtils.isEmpty(msg))
            msg = "加载中"
        var loading = mLoadingDialogs[context]
        if (loading == null) {
            loading = StateDialog(context)
            mLoadingDialogs.put(context, loading)
        }
        loading.stateStyle(StateDialog.STATE_STYLE.loading)
                .content(msg)

        if (maxDelay > 0)
            dialogDismiss(context, maxDelay, loading, listener)
        loading.setCanceledOnTouchOutside(canTouchCancel)
        showDialogOnMain(context, loading)
    }

    fun dialogMsgShow(context: Context, msg: String, btnText: String, listener: OnBtnClickL?) {
        val normalDialog = WarningDialog(context)
                .contentGravity(Gravity.CENTER)
                .content(CommonUtil.getSafeString(msg))
                .btnNum(1)
                .btnText(btnText)
        normalDialog.setOnBtnClickL(OnBtnClickL {
            dialogDismiss(context, 0, normalDialog, DialogInterface.OnDismissListener { listener?.onBtnClick() })
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
        Single.just(dialog).observeOn(AndroidSchedulers.mainThread()).subscribe {
            d ->
            d.show()
            if (mDialogs[context] == null) {
                val list = arrayListOf(dialog)
                mDialogs.put(context, list)
            } else {
                mDialogs[context]?.add(dialog)
            }
        }
    }

    fun dialogDismiss() {
        Completable.complete().observeOn(AndroidSchedulers.mainThread()).subscribe { mLoadingDialogs.forEach { it.value.dismiss() } }
    }

    fun dialogDismiss(context: Context) {
        Completable.complete().observeOn(AndroidSchedulers.mainThread()).subscribe { mLoadingDialogs[context]?.dismiss() }
    }

    fun dialogDismiss(context: Context, delay: Long = 0, dialog: Dialog, listener: DialogInterface.OnDismissListener? = null) {
        Single.just(dialog).delay(delay, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver <Dialog> {
                    override fun onSuccess(d: Dialog) {
                        d.dismiss()
                        listener?.onDismiss(d)
                        mDialogs[context]?.remove(dialog)
                    }

                    override fun onSubscribe(d: Disposable) {
                        addCall(context, d)
                    }

                    override fun onError(e: Throwable) {}
                })
    }
}
