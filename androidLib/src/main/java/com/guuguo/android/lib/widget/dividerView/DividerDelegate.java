/*
 * Copyright 2017. nekocode (nekocode.cn@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.guuguo.android.lib.widget.dividerView;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.guuguo.android.R;

import static com.guuguo.android.lib.widget.dividerView.DividerDrawable.DEFAULT_COLOR;
import static com.guuguo.android.lib.widget.dividerView.DividerDrawable.DEFAULT_DIVIDER_WIDTH;
import static com.guuguo.android.lib.widget.dividerView.DividerDrawable.dv_NO;

public class DividerDelegate {
    /**
     * 根据 AttributeSet
     *
     * @param context
     * @param attrs
     * @return
     */
    public static Drawable fromAttributeSet(View view, Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DividerTextView);

        Drawable drawable = getBgSelector(view, typedArray);
        typedArray.recycle();
        return drawable;
    }

    @Nullable
    private static DividerDrawable getDividerDrawable(TypedArray typedArray, int dividerColor) {
        int alignType = typedArray.getInt(R.styleable.DividerTextView_dv_align, dv_NO);
        if (alignType == dv_NO)
            return null;
        DividerDrawable bg = new DividerDrawable();
        bg.setDividerColor(dividerColor);
        bg.setDividerWidth(typedArray.getDimensionPixelSize(R.styleable.DividerTextView_dv_dividerWidth, DEFAULT_DIVIDER_WIDTH));
        bg.setDividerLength(typedArray.getDimensionPixelSize(R.styleable.DividerTextView_dv_dividerLength, 0));
        bg.setMargin(typedArray.getDimensionPixelSize(R.styleable.DividerTextView_dv_margin, -1));
        bg.setEndMargin(typedArray.getDimensionPixelSize(R.styleable.DividerTextView_dv_marginEnd, 0));
        bg.setStartMargin(typedArray.getDimensionPixelSize(R.styleable.DividerTextView_dv_marginStart, 0));
        bg.setAlignType(alignType);
        return bg;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static ColorStateList getPressedColorSelector(int normalColor, int pressedColor) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_activated},
                        new int[]{}
                },
                new int[]{
                        pressedColor,
                        pressedColor,
                        pressedColor,
                        normalColor
                }
        );
    }

    public static Drawable getBgSelector(View view, TypedArray typedArray) {
        int dividerColor = typedArray.getColor(R.styleable.DividerTextView_dv_dividerColor, DEFAULT_COLOR);
        int dividerColorFocus = typedArray.getColor(R.styleable.DividerTextView_dv_dividerColorFocus, Integer.MAX_VALUE);
        int backgroundColor = typedArray.getColor(R.styleable.DividerTextView_dv_backgroundColor, DEFAULT_COLOR);
        int backgroundColorPress = typedArray.getColor(R.styleable.DividerTextView_dv_backgroundColorPress, Integer.MAX_VALUE);
        int textColorPress = typedArray.getColor(R.styleable.DividerTextView_dv_textColorPress, Integer.MAX_VALUE);
        boolean isRipple = typedArray.getBoolean(R.styleable.DividerTextView_dv_isRipple, false);

        backgroundColorPress = backgroundColorPress == Integer.MAX_VALUE ? backgroundColor : backgroundColorPress;
        dividerColorFocus = dividerColorFocus == Integer.MAX_VALUE ? dividerColor : dividerColorFocus;
        
        Drawable resultDrawable;
        DividerDrawable drawableNormal = getDividerDrawable(typedArray, dividerColor);
        DividerDrawable drawableFocus = getDividerDrawable(typedArray, dividerColorFocus);
        drawableNormal.setColor(backgroundColor);
        drawableFocus.setColor(backgroundColorPress);
        
        getDividerDrawable(typedArray, dividerColorFocus);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isRipple) {
            RippleDrawable rippleDrawable = new RippleDrawable(getPressedColorSelector(backgroundColor, backgroundColorPress), drawableNormal, null);
            resultDrawable = rippleDrawable;
        } else {
            StateListDrawable bg = new StateListDrawable();
            bg.addState(new int[]{-android.R.attr.state_pressed, -android.R.attr.state_focused}, drawableNormal);
            bg.addState(new int[]{android.R.attr.state_pressed}, drawableFocus);
            bg.addState(new int[]{android.R.attr.state_focused}, drawableFocus);
            resultDrawable = bg;
        }
        if (view instanceof TextView && textColorPress != Integer.MAX_VALUE)
        {
            ColorStateList textColors = ((TextView) view).getTextColors();
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{new int[]{-android.R.attr.state_pressed}, new int[]{android.R.attr.state_pressed}},
                    new int[]{textColors.getDefaultColor(), textColorPress});
            ((TextView) view).setTextColor(colorStateList);
        }
        return resultDrawable;
    }

    public static Drawable getLayerDrawable(Drawable d1, DividerDrawable d2) {
        if (d2 == null)
            return d1;
        return new LayerDrawable(new Drawable[]{d1, d2});
    }

}
