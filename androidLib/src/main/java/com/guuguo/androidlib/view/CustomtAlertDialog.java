package com.guuguo.androidlib.view;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by guodeqing on 6/23/16.
 */
@SuppressWarnings("depreion")
public class CustomtAlertDialog extends MyDialog<CustomtAlertDialog> {


    private FrameLayout mContentLayout;
    protected View mContentView;

    public CustomtAlertDialog(Context context) {
        super(context);
    }


    @Override
    protected View createCustomContent() {
        /** content */
        mContentLayout = new FrameLayout(mContext);
        mContentLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return mContentLayout;
    }

    @Override
    protected void initCustomContent() {
        /** content */
        if (mContentView != null && mContentLayout.indexOfChild(mContentView) == -1)
            mContentLayout.addView(mContentView);
        if (mStyle == STYLE_ONE) {
            mContentLayout.setPadding(dp2px(15), dp2px(10), dp2px(15), dp2px(10));
            mTvContent.setMinHeight(dp2px(120));
        } else if (mStyle == STYLE_TWO) {
            mContentLayout.setPadding(dp2px(15), dp2px(7), dp2px(15), dp2px(20));
            mTvContent.setMinHeight(dp2px(120));
        }

    }


    // --->属性设置
    public CustomtAlertDialog contentView(View content) {
        this.mContentView = content;
        return this;
    }
}


