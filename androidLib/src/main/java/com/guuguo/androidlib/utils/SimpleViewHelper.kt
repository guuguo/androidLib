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
package com.guuguo.androidlib.utils

import android.view.View
import android.view.View.OnClickListener
import com.guuguo.androidlib.R
import com.guuguo.androidlib.utils.vary.IVaryViewHelper
import com.guuguo.androidlib.utils.vary.VaryViewHelper


/**
 * 自定义要切换的布局，通过IVaryViewHelper实现真正的切换<br></br>
 * 使用者可以根据自己的需求，使用自己定义的布局样式

 * @author guuguo
 */
class SimpleViewHelper(private val helper: IVaryViewHelper) {

    var simpleView: SimpleView? = null
        private set

    constructor(view: View) : this(VaryViewHelper(view)) {
    }

    fun setmIconSrc(mIconSrc: Int) {
        this.mIconSrc = mIconSrc
    }

    private var mIconSrc = -1

    @JvmOverloads fun showError(errorText: String, buttonText: String, onClickListener: OnClickListener? = null) {
        simpleView = SimpleView(helper.context)
        simpleView!!.text(errorText)
                .icon(R.drawable.state_error)
                .btnText(buttonText)
                .btnListener(onClickListener)
        requestSimpleView()

    }

    @JvmOverloads fun showLoading(text: String, indicator: String = "", color: Int = -1) {
        simpleView = SimpleView(helper.context)
        simpleView!!.setWrapContent(true)
        simpleView!!.text(text)
                .style(SimpleView.STYLE.loading)
                .loadingIndicator(indicator)
                .loadingColor(color)
        requestSimpleView()
    }

    fun requestSimpleView() {
        if (mIconSrc != -1)
            simpleView!!.icon(mIconSrc)
        simpleView!!.view.invalidate()
        helper.showLayout(simpleView?.view)
    }


    @JvmOverloads fun showEmpty(text: String, hint: String, buttonText: String, onClickListener: OnClickListener?) {
        simpleView = SimpleView(helper.context)
        simpleView!!.setWrapContent(true)
        simpleView!!.text(text)
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
