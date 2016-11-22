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
package com.guuguo.androidlib.utils;

import android.view.View;
import android.view.View.OnClickListener;

import com.guuguo.androidlib.R;
import com.guuguo.androidlib.utils.vary.IVaryViewHelper;
import com.guuguo.androidlib.utils.vary.VaryViewHelper;


/**
 * 自定义要切换的布局，通过IVaryViewHelper实现真正的切换<br>
 * 使用者可以根据自己的需求，使用自己定义的布局样式
 *
 * @author guuguo
 */
public class SimpleViewHelper {

    private IVaryViewHelper helper;

    private SimpleView simpleView;


    public SimpleViewHelper(View view) {
        this(new VaryViewHelper(view));
    }

    public void setmIconSrc(int mIconSrc) {
        this.mIconSrc = mIconSrc;
    }

    private int mIconSrc = -1;

    public SimpleViewHelper(IVaryViewHelper helper) {
        super();
        this.helper = helper;
    }

    public void showError(String errorText) {
        showError(errorText, "", null);
    }

    public void showError(String errorText, String buttonText, OnClickListener onClickListener) {
        simpleView = new SimpleView(helper.getContext());
        simpleView.text(errorText)
                .icon(R.drawable.state_error)
                .btnText(buttonText)
                .btnListener(onClickListener);
        requestSimpleView();

    }

    public void showEmpty(String text) {
        showEmpty(text, "");
    }

    public void showEmpty(String text, String hint) {
        showEmpty(text, hint, "", null);
    }

    public void showEmpty(String text, String buttonText, OnClickListener onClickListener) {
        showEmpty(text, "", buttonText, onClickListener);
    }

    public void showLoading(String text) {
        showLoading(text, "");
    }

    public void showLoading(String text, String indicator) {
        showLoading(text, indicator, -1);
    }

    public void showLoading(String text, String indicator, int color) {
        simpleView = new SimpleView(helper.getContext());
        simpleView.setWrapContent(true);
        simpleView.text(text)
                .style(SimpleView.STYLE.INSTANCE.getLoading())
                .loadingIndicator(indicator)
                .loadingColor(color);
        requestSimpleView();
    }

    public void requestSimpleView() {
        if (mIconSrc != -1)
            simpleView.icon(mIconSrc);
        simpleView.onAttachedToWindow();
        helper.showLayout(simpleView);
    }


    public void showEmpty(String text, String hint, String buttonText, OnClickListener onClickListener) {
        simpleView = new SimpleView(helper.getContext());
        simpleView.setWrapContent(true);
        simpleView.text(text)
                .hint(hint)
                .btnText(buttonText)
                .btnListener(onClickListener);

        requestSimpleView();
    }


    public void showView(View view) {
        helper.showLayout(view);
    }

    public void restore() {
        helper.restoreView();
    }

    public SimpleView getSimpleView() {
        return simpleView;
    }

}
