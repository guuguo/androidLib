package com.guuguo.androidlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.guuguo.androidlib.BaseApplication;
import com.guuguo.androidlib.R;

public class IconTextView extends TextView {

    public IconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode())
            return;
        initAttr(context, attrs);
    }


    public IconTextView(Context context) {
        this(context, null);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconTextView);
        int type = typedArray.getInt(R.styleable.IconTextView_typeface, 0);
        switch (type) {
            case 0:
                this.setTypeface(BaseApplication.getInstance().getTypeface(BaseApplication.MATERIAL_ICON));
                break;
            case 1:
                this.setTypeface(BaseApplication.getInstance().getTypeface(BaseApplication.FONTAWESOME_ICON));

        }
    }
}
