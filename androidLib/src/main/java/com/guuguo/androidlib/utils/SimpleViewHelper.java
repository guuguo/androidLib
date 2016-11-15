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

    public void setButtonShow(boolean buttonShow) {
        isButtonShow = buttonShow;
    }

    private boolean isButtonShow = false;

    public SimpleViewHelper(View view) {
        this(new VaryViewHelper(view));
    }

    public void setmIconSrc(int mIconSrc) {
        this.mIconSrc = mIconSrc;
    }

    private int mIconSrc = -1;
    private int mGravityVertical = -1;
    private int mSimpleViewType = -1;

    public void restoreSimpleViewType() {
        mSimpleViewType = -1;
    }

    public SimpleViewHelper(IVaryViewHelper helper) {
        super();
        this.helper = helper;
    }

    public void showError(String errorText) {
        showError(errorText, null, null);
    }

    public void showError(String errorText, String buttonText, OnClickListener onClickListener) {
        simpleView = new SimpleView(helper.getContext());
        simpleView.setType(SimpleView.TYPE_IMAGE_HINT);

        simpleView.setText(errorText);
        simpleView.setButton(buttonText, onClickListener);
        if (buttonText == null)
            simpleView.setButtonShow(false);
        simpleView.setmIconSrc(R.drawable.state_error);
        simpleView.setType(mSimpleViewType);

        requestSimpleView();

    }

    public void showEmpty(String text) {
        showEmpty(text, null);
    }

    public void showEmpty(String text, String hint) {
        showEmpty(text, hint, null, null);
    }

    public void showEmpty(String text, String buttonText, OnClickListener onClickListener) {
        showEmpty(text, null, buttonText, onClickListener);
    }

    public void showLoading(String text) {
        showLoading(text, null);
    }

    public void showLoading(String text, String indicator) {
        showLoading(text, indicator, -1);
    }

    public void showLoading(String text, String indicator, int color) {
        simpleView = new SimpleView(helper.getContext());
        if (text != null) {
            simpleView.setText(text);
            simpleView.setType(SimpleView.TYPE_LOADING);
        } else {
            simpleView.setType(SimpleView.TYPE_LOADING_HINT);
        }
        simpleView.setLoadingIndicator(indicator, color);
        requestSimpleView();
    }

    public void requestSimpleView() {

        if (!simpleView.isLoading()) {
            simpleView.setButtonShow(isButtonShow);
            simpleView.setType(SimpleView.TYPE_IMAGE_HINT);
            if (mSimpleViewType != -1) {
                simpleView.setType(mSimpleViewType);
            }
        } else {
            simpleView.setButtonShow(false);
        }

        simpleView.onAttachedToWindow();
        helper.showLayout(simpleView);
    }

    public void hideButton() {
        simpleView.setButtonText(null);
    }

    public void showLoading() {
        showLoading(null);
    }

    public void showEmpty(String text, String hint, String buttonText, OnClickListener onClickListener) {
        simpleView = new SimpleView(helper.getContext());
        simpleView.setText(text);
        if (hint != null) {
            simpleView.setType(SimpleView.TYPE_IMAGE_TEXT_HINT);
            simpleView.setHint(hint);
        }
        if (buttonText != null)
            simpleView.setButton(buttonText, onClickListener);
        if (mGravityVertical != -1) {
            simpleView.setVerticalGravity(mGravityVertical);
        }
        simpleView.setmIconSrc(mIconSrc);
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

    public void setmGravityVertical(int mGravityVertical) {
        this.mGravityVertical = mGravityVertical;
    }

    public void changeType(int Type) {
    }

    public void setmSimpleViewType(int mSimpleViewType) {
        this.mSimpleViewType = mSimpleViewType;
    }
}
