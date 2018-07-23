package com.guuguo.android.lib.widget;

import com.guuguo.android.lib.widget.roundview.com.guuguo.android.utils.widget.roundview.RoundTextView;

public class Iconcom.guuguo.android.utils.widget.roundview.RoundTextView extends com.guuguo.android.utils.widget.roundview.RoundTextView {
    private int type = 0;

    public Iconcom.guuguo.android.utils.widget.roundview.RoundTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttr(context, attrs);
    }


    public Iconcom.guuguo.android.utils.widget.roundview.RoundTextView(Context context) {
        this(context, null);
    }

    public Iconcom.guuguo.android.utils.widget.roundview.RoundTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconTextView);
        type = typedArray.getInt(R.styleable.IconTextView_typeface, 0);
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


