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
//    @Override
//    public void setUiBeforShow() {
//        super.setUiBeforShow();
//
//        /** title */
//        if (mStyle == STYLE_ONE) {
//            mTvTitle.setMinHeight(dp2px(48));
//            mTvTitle.setGravity(Gravity.CENTER_VERTICAL);
//            mTvTitle.setPadding(dp2px(15), dp2px(5), dp2px(0), dp2px(5));
//            mTvTitle.setVisibility(mIsTitleShow ? View.VISIBLE : View.GONE);
//        } else if (mStyle == STYLE_TWO) {
//            mTvTitle.setGravity(Gravity.CENTER);
//            mTvTitle.setPadding(dp2px(0), dp2px(15), dp2px(0), dp2px(0));
//        }
//
//        /** title underline */
//        mVLineTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                dp2px(mTitleLineHeight)));
//        mVLineTitle.setBackgroundColor(mTitleLineColor);
//        mVLineTitle.setVisibility(mIsTitleShow && mStyle == STYLE_ONE ? View.VISIBLE : View.GONE);
//
//        /** content */
//        if (mStyle == STYLE_ONE) {
//            mTvContent.setPadding(dp2px(15), dp2px(30), dp2px(15), dp2px(30));
//            mTvContent.setMinHeight(dp2px(120));
//            mTvContent.setGravity(mContentGravity);
//        } else if (mStyle == STYLE_TWO) {
//            mTvContent.setPadding(dp2px(15), dp2px(30), dp2px(15), dp2px(30));
//            mTvContent.setMinHeight(dp2px(120));
//            mTvContent.setGravity(Gravity.CENTER);
//        }
//
//        /** btns */
//        mVLineHorizontal.setBackgroundColor(mDividerColor);
//        mVLineVertical.setBackgroundColor(mDividerColor);
//        mVLineVertical2.setBackgroundColor(mDividerColor);
//
//        if (mBtnNum == 1) {
//            mTvBtnLeft.setVisibility(View.GONE);
//            mTvBtnRight.setVisibility(View.GONE);
//            mVLineVertical.setVisibility(View.GONE);
//            mVLineVertical2.setVisibility(View.GONE);
//        } else if (mBtnNum == 2) {
//            mTvBtnMiddle.setVisibility(View.GONE);
//            mVLineVertical.setVisibility(View.GONE);
//        }
//
//        /**set background color and corner radius */
//        float radius = dp2px(mCornerRadius);
//        mLlContainer.setBackgroundDrawable(CornerUtils.cornerDrawable(mBgColor, radius));
//        mTvBtnLeft.setBackgroundDrawable(CornerUtils.btnSelector(radius, mBgColor, mBtnPressColor, 0));
//        mTvBtnRight.setBackgroundDrawable(CornerUtils.btnSelector(radius, Color.parseColor("#f23131"), Color.parseColor("#e12020"), 1));
//        mTvBtnMiddle.setBackgroundDrawable(CornerUtils.btnSelector(mBtnNum == 1 ? radius : 0, Color.parseColor("#f23131"), Color.parseColor("#e12020"), -1));
//    }
//
//    // --->属性设置
//
//    /**
//     * set style(设置style)
//     */
//    public WarningDialog style(int style) {
//        this.mStyle = style;
//        return this;
//    }
//
//    /**
//     * set title underline color(设置标题下划线颜色)
//     */
//    public WarningDialog titleLineColor(int titleLineColor) {
//        this.mTitleLineColor = titleLineColor;
//        return this;
//    }
//
//    /**
//     * set title underline height(设置标题下划线高度)
//     */
//    public WarningDialog titleLineHeight(float titleLineHeight_DP) {
//        this.mTitleLineHeight = titleLineHeight_DP;
//        return this;
//    }
//
//    /**
//     * set divider color between btns(设置btn分割线的颜色)
//     */
//    public WarningDialog dividerColor(int dividerColor) {
//        this.mDividerColor = dividerColor;
//        return this;
//    }

}
