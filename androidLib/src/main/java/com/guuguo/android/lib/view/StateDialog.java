package com.guuguo.android.lib.view;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;
import com.guuguo.android.R;

import am.drawable.DoubleCircleDrawable;

/**
 * Created by mimi on 2016-11-10.
 */

public class StateDialog extends BaseDialog<StateDialog> {
    /**
     * container
     */
    protected LinearLayout mLlContainer;


//content
    /**
     * content
     */
    protected TextView mTvContent;
    /**
     * content text
     */
    protected String mContent;

    /**
     * content textcolor(正文字体颜色)
     */
    protected int mContentTextColor = Color.parseColor("#ffffff");
    /**
     * content textsize(正文字体大小)
     */
    protected float mContentTextSize;

//stateImage
    /**
     * stateStyle
     */
    protected int mStateStyle;
    private DoubleCircleDrawable loadDrawable;

    public static class STATE_STYLE {
        public static final int loading = 0;
        public static final int success = 1;
        public static final int error = 2;
    }

    protected ImageView mIvState;
    protected int mCustomStateRes = 0;


    /**
     * corner radius,dp(圆角程度,单位dp)
     */
    protected float mCornerRadius = 10;
    /**
     * background color(背景颜色)
     */
    protected int mBgColor;

    /**
     * method execute order:
     * show:constrouctor---show---oncreate---onStart---onAttachToWindow
     * dismiss:dismiss---onDetachedFromWindow---onStop
     *
     * @param context
     */
    public StateDialog(Context context) {
        super(context, true);
        /** init */
        mCornerRadius = 10;
        mContentTextSize = 16;
        mContentTextColor = Color.parseColor("#ffffff");
        mBgColor = Color.parseColor("#88000000");

        widthScale(1f);
        dimEnabled(false);
        mLlContainer = new LinearLayout(context);
        mLlContainer.setOrientation(LinearLayout.VERTICAL);

        /** stateImage **/
        mIvState = new ImageView(context);


        /** content */
        mTvContent = new TextView(context);
    }

    @Override
    public View onCreateView() {
        mLlControlHeight.setGravity(Gravity.CENTER);

        /** llcontainer **/
        mLlContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mLlContainer.setPadding(dp2px(15), dp2px(15), dp2px(15), dp2px(15));
        mLlContainer.setMinimumWidth(dp2px(150));
        mLlContainer.setGravity(Gravity.CENTER);

        /** stateImage **/
        mIvState.setLayoutParams(new ViewGroup.LayoutParams(dp2px(42), dp2px(42)));
        mIvState.setColorFilter(mContentTextColor);
        mLlContainer.addView(mIvState);

        /** loadingView **/
        loadDrawable = new DoubleCircleDrawable(getContext().getResources().getDisplayMetrics().density);

        /** content */
        mTvContent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTvContent.setPadding(0, dp2px(15), 0, 0);
        mLlContainer.addView(mTvContent);

        return mLlContainer;
    }

    @Override
    public void setUiBeforShow() {

        /** stateImage **/

        if (mCustomStateRes != 0) {
            mIvState.setImageResource(mCustomStateRes);
        } else {
            switch (mStateStyle) {
                case STATE_STYLE.loading:
                    mIvState.setImageDrawable(loadDrawable);
                    loadDrawable.start();
                    break;
                case STATE_STYLE.error:
                    mIvState.setImageResource(R.drawable.state_error);
                    break;
                case STATE_STYLE.success:
                    mIvState.setImageResource(R.drawable.state_success);
                    break;
            }
        }

        /** content */
        mTvContent.setGravity(Gravity.CENTER_HORIZONTAL);
        mTvContent.setText(mContent);
        mTvContent.setTextColor(mContentTextColor);
        mTvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, mContentTextSize);
        mTvContent.setLineSpacing(0, 1.3f);

        /**set background color and corner radius */
        float radius = dp2px(mCornerRadius);
        mLlContainer.setBackgroundDrawable(CornerUtils.cornerDrawable(mBgColor, radius));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mCancel)
            dismiss();
        return super.dispatchTouchEvent(ev);
    }

    /**
     * set content text(设置正文内容)
     */

    public StateDialog content(String content) {
        mContent = content;
        return (StateDialog) this;
    }


    /**
     * set content textcolor(设置正文字体颜色)
     */
    public StateDialog contentTextColor(int contentTextColor) {
        mContentTextColor = contentTextColor;
        return (StateDialog) this;
    }

    /**
     * set content textsize(设置正文字体大小,单位sp)
     */
    public StateDialog contentTextSize(float contentTextSize_SP) {
        mContentTextSize = contentTextSize_SP;
        return (StateDialog) this;
    }

    /**
     * set state style(设置图片类别-)
     */
    public StateDialog stateStyle(int stateStyle) {
        mStateStyle = stateStyle;
        return (StateDialog) this;
    }

    /**
     * set state style(设置图片类别-)
     */
    public StateDialog customStateRes(int imgRes) {
        mCustomStateRes = imgRes;
        return (StateDialog) this;
    }

    /**
     * set corner radius (设置圆角程度)
     */
    public StateDialog cornerRadius(float cornerRadius_DP) {
        mCornerRadius = cornerRadius_DP;
        return (StateDialog) this;
    }

    /**
     * set backgroud color(设置背景色)
     */
    public StateDialog bgColor(int bgColor) {
        mBgColor = bgColor;
        return (StateDialog) this;
    }

}
