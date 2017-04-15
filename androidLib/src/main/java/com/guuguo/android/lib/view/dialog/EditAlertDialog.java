package com.guuguo.android.lib.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by guodeqing on 6/23/16.
 */
@SuppressWarnings("deprecation")
public class EditAlertDialog extends MyDialog<EditAlertDialog> {

    private FrameLayout mContentLayout;
    private EditText mEditTextView;
    private String mEditText;
    private int mInputType = -1;


    public EditAlertDialog(Context context) {
        super(context);
    }

    @Override
    protected View createCustomContent() {
        mContentLayout = new FrameLayout(mContext);
        mContentLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        /** EditText */
        mEditTextView = new EditText(mContext);
        mEditTextView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, dp2px(45)));
        mContentLayout.addView(mEditTextView);

        return mContentLayout;
    }

    @Override
    protected void initCustomContent() {
        /** content */

        mContentLayout.setPadding(dp2px(30), dp2px(20), dp2px(30), dp2px(20));
        mContentLayout.setMinimumHeight(dp2px(120));

        /**edit text */
        if (!TextUtils.isEmpty(mEditText)) {
            mEditTextView.setText(mEditText);
        }
        if (mInputType != -1) {
            mEditTextView.setInputType(mInputType);
        }

    }

    /**
     * set style(设置style)
     */
    public EditAlertDialog style(int style) {
        this.mStyle = style;
        return this;
    }

    public void setInputType(int inputType) {
        mInputType = inputType;
    }

    public void setEditText(String editText) {
        mEditText = editText;
        ;
    }

    public String getEditText() {
        if (mEditTextView != null)
            return mEditTextView.getText().toString();
        return null;
    }
}


