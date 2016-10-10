package com.guuguo.androidlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class IconButton extends Button {

    public IconButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(FontManager.getTypeface(getContext(),FontManager.MATERIAL_ICON));
    }

    public IconButton(Context context) {
        this(context, null);
    }

    public IconButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
}
