/*
Copyright 2015 shizhefei（LuckyJayce）
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
   http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.guuguo.android.lib.view.simpleview

import android.view.View
import android.view.View.OnClickListener
import com.guuguo.android.R
import com.guuguo.android.lib.view.simpleview.vary.IVaryViewHelper
import com.guuguo.android.lib.view.simpleview.vary.VaryViewHelper


/**
 * 自定义要切换的布局，通过IVaryViewHelper实现真正的切换<br></br>
 * 使用者可以根据自己的需求，使用自己定义的布局样式

 * @author guuguo
 */
class SimpleViewHelper(private val helper: IVaryViewHelper) {

    var simpleView: SimpleView? = null
        private set
    var isWrapContent: Boolean = true

    constructor(view: View) : this(VaryViewHelper(view))

    constructor(view: View, isWrapContent: Boolean = true) : this(VaryViewHelper(view)) {
        this.isWrapContent = isWrapContent
    }

    @JvmOverloads fun showError(errorText: String, buttonText: String, onClickListener: OnClickListener? = null) {
        showImg(R.drawable.state_error, errorText, "", buttonText, onClickListener)
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

    fun showImg(iconRes: Int, text: String, hint: String, buttonText: String, onClickListener: OnClickListener?) {
        simpleView = SimpleView(helper.context)
        simpleView!!.setWrapContent(isWrapContent)
        simpleView!!.text(text)
                .icon(iconRes)
                .hint(hint)
                .btnText(buttonText)
                .btnListener(onClickListener)

        requestSimpleView()
    }

    fun showEmpty(text: String, hint: String, buttonText: String, onClickListener: OnClickListener?) {
        showImg(R.drawable.empty_cute_girl_box, text, hint, buttonText, onClickListener)
    }

    fun showWarning(text: String) {
        showImg(R.drawable.state_warning, "", text, "", null)
    }

    fun showEmpty(text: String) {
        showEmpty("", text, "", null)
    }

    fun showEmpty(isIconShow: Boolean, text: String, hint: String, buttonText: String, onClickListener: OnClickListener?) {

        simpleView = SimpleView(helper.context)
        simpleView!!.setWrapContent(isWrapContent)
        simpleView!!.text(text)
                .setIconShow(isIconShow)
                .hint(hint)
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
