package com.guuguo.android.dialog.dialog

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.View
import com.guuguo.android.dialog.R
import com.guuguo.android.dialog.databinding.DialogDefaultWarningBinding
import com.guuguo.android.dialog.dialog.base.IWarningDialog
import com.guuguo.android.dialog.utils.CornerUtils


class DefaultWarningDialog : IWarningDialog {
    constructor(mContext: Context) : super(mContext) {
        this.mContext = mContext
    }

    lateinit var binding: DialogDefaultWarningBinding
    override fun onCreateView(): View {
        val view = layoutInflater.inflate(R.layout.dialog_default_warning, null)
        binding = DataBindingUtil.bind(view)!!
        widthRatio(0f)
        heightRatio(0f)
        dimEnabled(true)

        val lp = window.attributes
        lp.dimAmount = 0.5f
        window!!.attributes = lp

        binding.llContent.background = CornerUtils.cornerDrawable(Color.WHITE, radius)
        return view
    }
    val radius=dp2px(4f).toFloat()

    override fun setUiBeforShow() {
        binding.warningDialog = this
        binding.btn1.setOnClickListener {
            btnClick1?.invoke(this)
        }
        binding.btn2.setOnClickListener {
            btnClick2?.invoke(this)
        }

        val colorWhite = Color.WHITE
        val colorWhitePress = getColor(R.color.black20)

        val colorPrimary = getColor(R.color.dialogColorPrimary)
        val colorPrimaryPress = getColor(R.color.dialogColorPrimaryDark)


        if (btnNum.get() == 1) {
            binding.btn1.background = CornerUtils.btnSelector(radius, colorWhite, colorWhitePress, -1)
        } else if (btnNum.get() == 2) {

            binding.btn1.background = CornerUtils.btnSelector(radius,
                    if (btnPosition.get() == 1) colorPrimary else colorWhite, if (btnPosition.get() == 1) colorPrimaryPress else colorWhitePress, 0)
            binding.btn2.background = CornerUtils.btnSelector(radius,
                    if (btnPosition.get() == 2) colorPrimary else colorWhite, if (btnPosition.get() == 2) colorPrimaryPress else colorWhitePress, 1)
        }
    }

    @ColorInt
    fun getColor(@ColorRes id: Int): Int {
        return if (Build.VERSION.SDK_INT >= 23) {
            mContext.getColor(id)
        } else {
            mContext.resources.getColor(id)
        }
    }

    init {

    }

    var title = ObservableField("")
        private set
    var message = ObservableField("")
        private set
    var btnPosition = ObservableField(0)
        private set
    var btnNum = ObservableField(1)
        private set
    var btnText1 = ObservableField("")
        private set
    var btnText2 = ObservableField("")
        private set
    var btnClick1: ((v: DefaultWarningDialog) -> Unit)? = null
    var btnClick2: ((v: DefaultWarningDialog) -> Unit)? = null

    override fun setTitle(title: String) = this.also { it.title.set(title) }
    override fun setMessage(message: String) = this.also { it.message.set(message) }
    override fun setBtnNum(btnNum: Int) = this.also { it.btnNum.set(if (btnNum < 0) 0 else if (btnNum > 2) 2 else btnNum) }
    override fun setBtnText(vararg text: String) = this.also { text.getOrNull(0)?.apply { it.btnText1.set(this) }; text.getOrNull(1)?.apply { it.btnText2.set(this) } }
    override fun setBtnClick(vararg clicks: ((v: IWarningDialog) -> Unit)?) = this.also { clicks.getOrNull(0)?.apply { it.btnClick1 = this }; clicks.getOrNull(1)?.apply { it.btnClick2 = this } }
    override fun setPositiveBtnPosition(btnPosition: Int) = this.also { it.btnPosition.set(btnPosition) }

}//Fast Function
