package com.guuguo.androidlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.guuguo.androidlib.BaseApplication;

public class IconButton extends Button {

    public IconButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(BaseApplication.getInstance().getTypeface(BaseApplication.MATERIAL_ICON));
    }

    public IconButton(Context context) {
        this(context, null);
    }

    public IconButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
}
