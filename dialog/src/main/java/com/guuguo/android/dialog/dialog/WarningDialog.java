package com.guuguo.android.dialog.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by mimi on 2016-11-11.
 */
@SuppressWarnings("deprecation")
public class WarningDialog extends MyDialog<WarningDialog> {

    public WarningDialog(Context context) {
        super(context);
        isTitleShow(false);
        setMContentGravity(Gravity.CENTER);
    }


    @Override
    protected View createCustomContent() {
        getMTvContent().setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return getMTvContent();
    }

    @Override
    protected void initCustomContent() {
        /** content */
        getMTvContent().setPadding(dp2px(15), dp2px(30), dp2px(15), dp2px(30));
        getMTvContent().setMinHeight(dp2px(120));
        getMTvContent().setGravity(getMContentGravity());
    }

}
