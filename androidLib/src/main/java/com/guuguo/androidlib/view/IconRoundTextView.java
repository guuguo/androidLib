package com.guuguo.androidlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.flyco.roundview.RoundTextView;

public class IconRoundTextView extends RoundTextView {

        public IconRoundTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            if (isInEditMode())
                return;
            initAttr(context, attrs);
        }


        public IconRoundTextView(Context context) {
            this(context, null);
        }

        public IconRoundTextView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        private void initAttr(Context context, AttributeSet attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, com.guuguo.androidlib.R.styleable.IconTextView);
            int type = typedArray.getInt(com.guuguo.androidlib.R.styleable.IconTextView_typeface, 0);
            switch (type) {
                case 0:

                    this.setTypeface(FontManager.getTypeface(getContext(),FontManager.MATERIAL_ICON));
                    break;
                case 1:
                    this.setTypeface(FontManager.getTypeface(getContext(),FontManager.FONTAWESOME_ICON));

            }
            typedArray.recycle();
        }
    }


