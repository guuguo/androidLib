package com.guuguo.android.lib.widget.dividerView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


/**
 * 用于需要圆角矩形框背景的TextView的情况,减少直接使用TextView时引入的shape资源文件
 */
public class DividerTextView extends AppCompatTextView {
    public DividerTextView(Context context) {
        super(context);
        init(context, null);
    }

    public DividerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DividerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final Drawable bg = DividerDelegate.fromAttributeSet(DividerTextView.this, context, attrs);
        setBackgroundDrawable(bg);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
