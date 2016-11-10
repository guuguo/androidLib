package com.guuguo.androidlib.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guuguo.androidlib.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by Barry on 16/5/28.
 */
public class SimpleView extends FrameLayout {

    private final LinearLayout mLLLayout;
    private String text = "找不到数据   ";
    private String hint;
    private OnClickListener mOnClickListener;
    private String mBtnText;
    private final AVLoadingIndicatorView mAVIloading;

    public SimpleView setButtonShow(boolean buttonShow) {
        isButtonShow = buttonShow;
        return this;
    }

    private boolean isButtonShow = false;

    private int mIconSrc = -1;

    private ImageView mImg;
    private TextView mTvText;
    private TextView mTvHint;
    private Button mBtn;


    public static final int TYPE_TEXT_ONLY = 0;
    public static final int TYPE_HINT_ONLY = TYPE_TEXT_ONLY + 1;
    public static final int TYPE_IMAGE_ONLY = TYPE_HINT_ONLY + 1;
    public static final int TYPE_IMAGE_HINT = TYPE_IMAGE_ONLY + 1;
    public static final int TYPE_IMAGE_TEXT = TYPE_IMAGE_HINT + 1;
    public static final int TYPE_IMAGE_TEXT_HINT = TYPE_IMAGE_TEXT + 1;
    public static final int TYPE_LOADING = TYPE_IMAGE_TEXT_HINT + 1;
    public static final int TYPE_LOADING_HINT = TYPE_LOADING + 1;


    private int type = TYPE_IMAGE_HINT;
    private int imageStyle;
    private int gravity = Gravity.CENTER_VERTICAL;


    public int getVerticalGravity() {
        return gravity;
    }

    public SimpleView setVerticalGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }


    public SimpleView(Context context) {
        this(context, null);
    }

    public SimpleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.simple_empty_view, this);
        mAVIloading = (AVLoadingIndicatorView) findViewById(R.id.avi_loading);
        mLLLayout = (LinearLayout) findViewById(R.id.layoutEmpty);
        mTvText = (TextView) findViewById(R.id.tv_text);
        mTvHint = (TextView) findViewById(R.id.tv_hint);
        mImg = (ImageView) findViewById(R.id.img);
        mBtn = (Button) findViewById(R.id.btn_empty);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public int getType() {
        return type;
    }

    public SimpleView setType(int type) {
        this.type = type;
        return this;
    }

    private void hideAllView() {
        mTvText.setVisibility(View.GONE);
        mTvHint.setVisibility(View.GONE);
        mImg.setVisibility(View.GONE);
    }

    private void showAllView() {
        mTvText.setVisibility(View.VISIBLE);
        mTvHint.setVisibility(View.VISIBLE);
        mImg.setVisibility(View.VISIBLE);
    }


    public String getText() {
        return text;
    }

    public SimpleView setText(String text) {
        this.text = text;
        return this;
    }

    public String getHint() {
        return hint;
    }

    public SimpleView setHint(String hint) {
        this.hint = hint;
        return this;
    }

    public SimpleView setButton(String text, OnClickListener listener) {
        this.mBtnText = text;
        this.mOnClickListener = listener;
        return this;
    }

    public SimpleView setButtonText(String text) {
        this.mBtnText = text;
        return this;

    }

    @Override
    public void onAttachedToWindow() {
        setUIBeforeShow();
        super.onAttachedToWindow();
    }

    private void setUIBeforeShow() {
        LayoutParams params = new LayoutParams(mLLLayout.getLayoutParams());
        params.gravity = gravity | Gravity.CENTER_HORIZONTAL;
        mLLLayout.setLayoutParams(params);

        if (!isButtonShow)
            mBtn.setVisibility(View.GONE);
        else {
            mBtn.setVisibility(View.VISIBLE);
            mBtn.setText(getSafeString(mBtnText));
            mBtn.setOnClickListener(mOnClickListener);
        }


        mTvHint.setText(TextUtils.isEmpty(hint) ? getSafeString(text) : hint);
        mTvText.setText(getSafeString(text));
        if (mIconSrc != -1)
            mImg.setImageResource(mIconSrc);
        mAVIloading.setVisibility(View.GONE);
        switch (type) {
            case TYPE_TEXT_ONLY:
                hideAllView();
                mTvText.setVisibility(View.VISIBLE);
                break;
            case TYPE_HINT_ONLY:
                hideAllView();
                mTvHint.setVisibility(View.VISIBLE);
                break;
            case TYPE_IMAGE_ONLY:
                hideAllView();
                mImg.setVisibility(View.VISIBLE);
                break;
            case TYPE_IMAGE_HINT:
                showAllView();
                mTvText.setVisibility(View.GONE);
                break;
            case TYPE_IMAGE_TEXT:
                showAllView();
                mTvHint.setVisibility(View.GONE);
                break;
            case TYPE_IMAGE_TEXT_HINT:
                showAllView();
                break;
            case TYPE_LOADING:
                hideAllView();
                mAVIloading.setVisibility(View.VISIBLE);
                break;
            case TYPE_LOADING_HINT:
                hideAllView();
                mAVIloading.setVisibility(View.VISIBLE);
                mTvHint.setText(text);
                break;
        }
    }

    public SimpleView setLoadingIndicator(String str) {
        mAVIloading.setIndicator(str);
        return this;
    }

    public SimpleView setLoadingIndicator(String str, int color) {
        mAVIloading.setIndicator(str);
        if (color >= 0)
            mAVIloading.setIndicatorColor(color);
        return this;
    }

    public int getmIconSrc() {
        return mIconSrc;
    }

    public SimpleView setmIconSrc(int mIconSrc) {
        this.mIconSrc = mIconSrc;
        return this;
    }

    public boolean isLoading() {
        if (type == TYPE_LOADING || type == TYPE_LOADING_HINT)
            return true;
        return false;
    }

    public boolean isButtonShow() {
        return isButtonShow;
    }

    //但如果字符串为空，返回“”
    private String getSafeString(String str) {
        if (TextUtils.isEmpty(str))
            return "";
        else
            return str;
    }


   

}
