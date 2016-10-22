package com.guuguo.androidlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class IconTextView extends TextView {
    private  int type=0;

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
        TypedArray typedArray = context.obtainStyledAttributes(attrs, com.guuguo.androidlib.R.styleable.IconTextView);
        type = typedArray.getInt(com.guuguo.androidlib.R.styleable.IconTextView_typeface, 0);
        setType();
        typedArray.recycle();
    }

    private void setType() {
        switch (type) {
            case 0:
                this.setTypeface(FontManager.getTypeface(getContext(), FontManager.MATERIAL_ICON));
                break;
            case 1:
                this.setTypeface(FontManager.getTypeface(getContext(), FontManager.FONTAWESOME_ICON));

        }
    }

    public void setNormalText(CharSequence text) {
        setText(text);
        setTypeface(Typeface.DEFAULT);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        setType();
    }
}


