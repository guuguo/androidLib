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
package com.guuguo.androidlib.helper;

import android.view.View;
import android.view.View.OnClickListener;

import com.guuguo.androidlib.helper.vary.IVaryViewHelper;
import com.guuguo.androidlib.helper.vary.VaryViewHelper;

/**
 * 自定义要切换的布局，通过IVaryViewHelper实现真正的切换<br>
 * 使用者可以根据自己的需求，使用自己定义的布局样式
 *
 * @author guuguo
 */
public abstract class BaseLoadViewHelper {

    private IVaryViewHelper helper;

    public BaseLoadViewHelper(View view) {
        this(new VaryViewHelper(view));
    }

    private BaseLoadViewHelper(IVaryViewHelper helper) {
        super();
        this.helper = helper;
    }

    public abstract void showError(String errorText, String buttonText, OnClickListener onClickListener);

    public abstract void showError(String errorText);

    public abstract void showEmpty(String errorText, String buttonText, OnClickListener onClickListener);

    public abstract void showEmpty(String errorText);

    public abstract void showLoading(String loadText);

    public void showView(View view) {
        helper.showLayout(view);
    }

    public void restore() {
        helper.restoreView();
    }
}
