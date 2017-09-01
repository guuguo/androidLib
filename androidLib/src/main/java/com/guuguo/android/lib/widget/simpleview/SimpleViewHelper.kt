package com.guuguo.android.lib.widget.simpleview

import android.view.View
import android.view.View.OnClickListener
import com.guuguo.android.R


/**
 * 自定义要切换的布局，通过IVaryViewHelper实现真正的切换<br></br>
 * 使用者可以根据自己的需求，使用自己定义的布局样式

 * @author guuguo
 */
class SimpleViewHelper(private val helper: VaryViewHelper) {

    var simpleView: SimpleView? = null
        private set
    var isWrapContent: Boolean = true

    constructor(view: View) : this(VaryViewHelper(view))

    constructor(view: View, isWrapContent: Boolean = true) : this(VaryViewHelper(view)) {
        this.isWrapContent = isWrapContent
    }

    @JvmOverloads fun showError(errorText: String, buttonText: String, onClickListener: OnClickListener? = null) {
        showImg(R.drawable.state_error, errorText,  buttonText, onClickListener)
    }

    @JvmOverloads fun showLoading(text: String) {
        simpleView = SimpleView(helper.context)
        simpleView!!.setWrapContent(isWrapContent)
        simpleView!!.text(text)
                .style(SimpleView.STYLE.loading)
        requestSimpleView()
    }

    fun requestSimpleView() {
        simpleView!!.view.invalidate()
        helper.showLayout(simpleView?.view)
    }

    fun showImg(iconRes: Int, text: String, buttonText: String, onClickListener: OnClickListener?) {
        simpleView = SimpleView(helper.context)
        simpleView!!.setWrapContent(isWrapContent)
        simpleView!!.text(text)
                .icon(iconRes)
                .btnText(buttonText)
                .btnListener(onClickListener)

        requestSimpleView()
    }

    fun showEmpty(text: String,  buttonText: String, onClickListener: OnClickListener?) {
        showImg(R.drawable.empty_cute_girl_box, text,  buttonText, onClickListener)
    }

    fun showWarning(text: String) {
        showImg(R.drawable.state_warning, text, "", null)
    }

    fun showEmpty(text: String) {
        showEmpty( text, "", null)
    }

    fun showEmpty(isIconShow: Boolean, text: String,  buttonText: String, onClickListener: OnClickListener?) {

        simpleView = SimpleView(helper.context)
        simpleView!!.setWrapContent(isWrapContent)
        simpleView!!.text(text)
                .setIconShow(isIconShow)
                .btnText(buttonText)
                .btnListener(onClickListener)

        requestSimpleView()
    }

    fun showView(view: View) {
        helper.showLayout(view)
    }

    fun restore() {
        helper.restoreView()
    }

}
