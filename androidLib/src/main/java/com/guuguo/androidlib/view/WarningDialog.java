package com.guuguo.androidlib.view;

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
        mContentGravity = Gravity.CENTER;
    }


    @Override
    protected View createCustomContent() {
        mTvContent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return mTvContent;
    }

    @Override
    protected void initCustomContent() {
        /** content */
        mTvContent.setPadding(dp2px(15), dp2px(30), dp2px(15), dp2px(30));
        mTvContent.setMinHeight(dp2px(120));
        mTvContent.setGravity(mContentGravity);
    }

}
