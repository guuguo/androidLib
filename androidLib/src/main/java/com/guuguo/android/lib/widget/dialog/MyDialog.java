package com.guuguo.android.lib.widget.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.internal.BaseAlertDialog;
import com.guuguo.android.R;

/**
 * Created by mimi on 2016-11-11.
 */
@SuppressWarnings("deprecation")
public abstract class MyDialog<T extends BaseAlertDialog<T>> extends BaseAlertDialog<T> {

    /**
     * title underline
     */
    protected View mVLineTitle;
    /**
     * vertical line between btns
     */
    protected View mVLineVertical;
    /**
     * vertical line between btns
     */
    protected View mVLineVertical2;
    /**
     * horizontal line above btns
     */
    protected View mVLineHorizontal;
    /**
     * title underline color(标题下划线颜色)
     */
    protected int mTitleLineColor = Color.parseColor("#61AEDC");
    /**
     * title underline height(标题下划线高度)
     */
    protected float mTitleLineHeight = 1f;
    /**
     * btn divider line color(对话框之间的分割线颜色(水平+垂直))
     */
    protected int mDividerColor = Color.parseColor("#DCDCDC");

    /**
     * btn mButtonType(按钮类型0是内部)
     */

    static final int BTN_STYLE_FLAT = 0;
    static final int BTN_STYLE_OUTLINE = 1;
    static final int BTN_STYLE_RAISED = 2;

    private int mButtonType = BTN_STYLE_FLAT;

    private int mPrimaryBtnColor;
    private int mPrimaryBtnPressColor;
    private boolean mIsDfaultWidth = false;

    public static class DialogSetting {
        int primaryBtnColor = 0;
        int primaryBtnPressColor = 0;
        int cornerRadius = 0;
        int isTitleShow = 0;
    }

    public MyDialog(Context context) {
        super(context);

        /** default value*/
        mTitleTextSize = 17f;
        mTitleTextColor = Color.parseColor("#000000");
        mTitleLineColor = Color.parseColor("#33000000");
        mWidthScale = 0.85f;
        mContentTextColor = Color.parseColor("#383838");
        mContentTextSize = 17f;
        mLeftBtnTextColor = Color.parseColor("#000000");
        mRightBtnTextColor = Color.parseColor("#ffffff");
        mMiddleBtnTextColor = Color.parseColor("#ffffff");

        mPrimaryBtnPressColor = Color.parseColor("#e12020");
        mPrimaryBtnColor = ContextCompat.getColor(context, R.color.colorPrimary); //Color.parseColor("#f23131");
        mCornerRadius = 5;
        /** default value*/
    }

    @Override
    public View onCreateView() {
        mLlControlHeight.setGravity(Gravity.CENTER);


        /** llcontainer **/
        if (!mIsDfaultWidth) {
            mLlContainer.setLayoutParams(new ViewGroup.LayoutParams(getContext().getResources().getDimensionPixelSize(R.dimen.dialog_width), ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        /** title */
        mTvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mTvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
//        mTvTitle.setForegroundGravity(Gravity.CENTER_HORIZONTAL);

        mLlContainer.addView(mTvTitle);

        /** title underline */
        mVLineTitle = new View(mContext);
        mLlContainer.addView(mVLineTitle);


        mLlContainer.addView(createCustomContent());

        mVLineHorizontal = new View(mContext);
        mVLineHorizontal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        mLlContainer.addView(mVLineHorizontal);

        /** btns */

        mTvBtnLeft.setLayoutParams(new LinearLayout.LayoutParams(0, dp2px(43), 1));
        mLlBtns.addView(mTvBtnLeft);

        mVLineVertical = new View(mContext);
        mVLineVertical.setLayoutParams(new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT));
        mLlBtns.addView(mVLineVertical);

        mTvBtnMiddle.setLayoutParams(new LinearLayout.LayoutParams(0, dp2px(43), 1));
        mLlBtns.addView(mTvBtnMiddle);

        mVLineVertical2 = new View(mContext);
        mVLineVertical2.setLayoutParams(new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT));
        mLlBtns.addView(mVLineVertical2);

        mTvBtnRight.setLayoutParams(new LinearLayout.LayoutParams(0, dp2px(43), 1));
        mLlBtns.addView(mTvBtnRight);


        mLlContainer.addView(mLlBtns);

        return mLlContainer;
    }

    protected abstract View createCustomContent();

    protected abstract void initCustomContent();

    @Override
    public void setUiBeforShow() {
        super.setUiBeforShow();


        /** title */
        mTvTitle.setMinHeight(dp2px(48));
        mTvTitle.setGravity(Gravity.CENTER_VERTICAL);
        mTvTitle.setPadding(dp2px(15), dp2px(5), dp2px(0), dp2px(5));
        mTvTitle.setVisibility(mIsTitleShow ? View.VISIBLE : View.GONE);


        /** title underline */
        mVLineTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                dp2px(mTitleLineHeight)));
        mVLineTitle.setBackgroundColor(mTitleLineColor);
        mVLineTitle.setVisibility(mIsTitleShow ? View.VISIBLE : View.GONE);

        /** content */

        mTvContent.setPadding(dp2px(15), dp2px(30), dp2px(15), dp2px(30));
        mTvContent.setMinHeight(dp2px(120));
        mTvContent.setGravity(mContentGravity);

        initCustomContent();
        /** btns */
        /**set background color and corner radius */
        float radius = dp2px(mCornerRadius);
        mLlContainer.setBackgroundDrawable(CornerUtils.cornerDrawable(mBgColor, radius));
        mTvBtnLeft.setBackgroundDrawable(CornerUtils.btnSelector(radius, mBgColor, mBtnPressColor, 0));
        mTvBtnRight.setBackgroundDrawable(CornerUtils.btnSelector(radius, mPrimaryBtnColor, mPrimaryBtnPressColor, 1));
        mTvBtnMiddle.setBackgroundDrawable(CornerUtils.btnSelector(mBtnNum == 1 ? radius : 0, mBgColor, mBtnPressColor, -1));
        mTvBtnMiddle.setTextColor(Color.BLACK);

        switch (mButtonType) {
            case BTN_STYLE_FLAT: {
                mVLineHorizontal.setBackgroundColor(mDividerColor);
                mVLineVertical.setBackgroundColor(mDividerColor);
                mVLineVertical2.setBackgroundColor(mDividerColor);

                if (mBtnNum == 1) {
                    mTvBtnLeft.setVisibility(View.GONE);
                    mTvBtnRight.setVisibility(View.GONE);
                    mVLineVertical.setVisibility(View.GONE);
                    mVLineVertical2.setVisibility(View.GONE);
                } else if (mBtnNum == 2) {
                    mTvBtnMiddle.setVisibility(View.GONE);
                    mVLineVertical.setVisibility(View.GONE);
                }
                break;
            }
            case BTN_STYLE_OUTLINE: {
                mVLineHorizontal.setBackgroundColor(Color.TRANSPARENT);
                mVLineVertical.setBackgroundColor(Color.TRANSPARENT);
                mVLineVertical2.setBackgroundColor(Color.TRANSPARENT);

                if (mBtnNum == 1) {
                    mTvBtnLeft.setVisibility(View.GONE);
                    mTvBtnRight.setVisibility(View.GONE);
                    mVLineVertical.setVisibility(View.GONE);
                    mVLineVertical2.setVisibility(View.GONE);
//                    ((LinearLayout.LayoutParams) mTvBtnMiddle.getLayoutParams()).setMargins(dp2px(8), dp2px(8), dp2px(8), dp2px(8));
//                    mTvBtnMiddle.setBackgroundDrawable(CornerUtils.btnSelector(dp2px(30), mBgColor, mBtnPressColor, -1));
                }
            }
        }


    }


    // --->属性设置
    public T btnType(int buttonType) {
        this.mButtonType = buttonType;
        return (T) this;
    }

    /**
     * set title underline color(设置标题下划线颜色)
     */
    public T titleLineColor(int titleLineColor) {
        this.mTitleLineColor = titleLineColor;
        return (T) this;
    }

    /**
     * set title underline height(设置标题下划线高度)
     */
    public T titleLineHeight(float titleLineHeight_DP) {
        this.mTitleLineHeight = titleLineHeight_DP;
        return (T) this;
    }

    /**
     * set divider color between btns(设置btn分割线的颜色)
     */
    public T dividerColor(int dividerColor) {
        this.mDividerColor = dividerColor;
        return (T) this;
    }

    /**
     * set divider color between btns(设置btn分割线的颜色)
     */
    public T setDefaultWidth(boolean bool) {
        this.mIsDfaultWidth = bool;
        return (T) this;
    }
}
