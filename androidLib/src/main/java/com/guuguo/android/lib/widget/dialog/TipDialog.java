package com.guuguo.android.lib.widget.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;
import com.guuguo.android.R;
import com.guuguo.android.lib.widget.drawable.CircularDrawable;
import com.guuguo.android.lib.widget.drawable.DoubleCircleDrawable;

/**
 * Created by mimi on 2016-11-10.
 */

public class TipDialog extends BaseDialog<TipDialog> {
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

    public void setLoadDrawable(Drawable loadDrawable) {
        this.loadDrawable = loadDrawable;
    }

    private Drawable loadDrawable;

    public static class STATE_STYLE {
        public static final int noIcon = 4;
        public static final int loading = 0;
        public static final int success = 1;
        public static final int error = 2;
        public static final int info = 3;
    }

    protected AppCompatImageView mIvState;
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
    public TipDialog(Context context) {
        super(context, true);
        /** init */
        mCornerRadius = 6;
        mContentTextSize = 16;
        mContentTextColor = Color.parseColor("#ffffff");
        mBgColor = Color.parseColor("#AA000000");

        widthScale(1f);
        dimEnabled(false);
        mLlContainer = new LinearLayout(context);
        mLlContainer.setOrientation(LinearLayout.VERTICAL);

        /** stateImage **/
        mIvState = new AppCompatImageView(context);

        /** content */
        mTvContent = new TextView(context);
    }

    @Override
    public View onCreateView() {
        mLlControlHeight.setGravity(Gravity.CENTER);

        /** llcontainer **/
        mLlContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mLlContainer.setPadding(dp2px(10), dp2px(20), dp2px(10), dp2px(20));
        mLlContainer.setMinimumWidth(dp2px(150));
        mLlContainer.setGravity(Gravity.CENTER);

        /** stateImage **/
        mIvState.setLayoutParams(new ViewGroup.LayoutParams(dp2px(44), dp2px(44)));
        mIvState.setColorFilter(mContentTextColor);
        mLlContainer.addView(mIvState);

        /** loadingView **/
        if(loadDrawable==null)
            loadDrawable = new CircularDrawable();

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
                    if (loadDrawable instanceof Animatable)
                        ((Animatable) loadDrawable).start();
                    break;
                case STATE_STYLE.error:
                    mIvState.setImageResource(R.drawable.qmui_icon_notify_error);
                    break;
                case STATE_STYLE.info:
                    mIvState.setImageResource(R.drawable.qmui_icon_notify_info);
                    break;
                case STATE_STYLE.success:
                    mIvState.setImageResource(R.drawable.qmui_icon_notify_done);
                    break;
                case STATE_STYLE.noIcon:
                    mIvState.setVisibility(View.GONE);
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
        if (mCancel) {
            dismiss();
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * set content text(设置正文内容)
     */

    public TipDialog content(String content) {
        mContent = content;
        return this;
    }


    /**
     * set content textcolor(设置正文字体颜色)
     */
    public TipDialog contentTextColor(int contentTextColor) {
        mContentTextColor = contentTextColor;
        return this;
    }

    /**
     * set content textsize(设置正文字体大小,单位sp)
     */
    public TipDialog contentTextSize(float contentTextSize_SP) {
        mContentTextSize = contentTextSize_SP;
        return this;
    }

    /**
     * set state style(设置图片类别-)
     */
    public TipDialog stateStyle(int stateStyle) {
        mStateStyle = stateStyle;
        return this;
    }

    /**
     * set state style(设置图片类别-)
     */
    public TipDialog customStateRes(int imgRes) {
        mCustomStateRes = imgRes;
        return this;
    }

    /**
     * set corner radius (设置圆角程度)
     */
    public TipDialog cornerRadius(float cornerRadius_DP) {
        mCornerRadius = cornerRadius_DP;
        return this;
    }

    /**
     * set backgroud color(设置背景色)
     */
    public TipDialog bgColor(int bgColor) {
        mBgColor = bgColor;
        return this;
    }

}
