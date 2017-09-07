package top.guuguo.dividerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import static top.guuguo.dividerview.DividerDrawable.DEFAULT_COLOR;
import static top.guuguo.dividerview.DividerDrawable.DEFAULT_DIVIDER_WIDTH;
import static top.guuguo.dividerview.DividerDrawable.dv_NO;


public class DividerDelegate {
    private DividerDrawable drawableNormal;
    private DividerDrawable drawableFocus;

    /**
     * 根据 AttributeSet
     *
     * @param context
     * @param attrs
     * @return
     */
    public void resolveDrawable(View view, Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DividerTextView);
        Drawable drawable = getBgSelector(view, typedArray);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
            view.setBackground(drawable);
        } else {
            //noinspection deprecation
            view.setBackgroundDrawable(drawable);
        }
        typedArray.recycle();
    }

    public void addDivider(DividerDrawable.DividerLine divider) {
        if (drawableNormal != null) {
            drawableNormal.addDivider(divider);
        }
        if (drawableFocus != null) {
            drawableFocus.addDivider(divider);
        }
    }

    public void clearDivider() {
        if (drawableNormal != null) {
            drawableNormal.clearDivider();
        }
        if (drawableFocus != null) {
            drawableFocus.clearDivider();
        }
    }

    public void addDivider(@DividerDrawable.AlignType int alineType, int dividerWidth, int dividerLength, @ColorInt int dividerColor, int margin) {
        DividerDrawable.DividerLine divider = new DividerDrawable.DividerLine();
        divider.setAlignType(alineType);
        divider.setDividerWidth(dividerWidth);
        divider.setDividerColor(dividerColor);
        divider.setMargin(margin);
        divider.setDividerLength(dividerLength);
        if (drawableNormal != null) {
            drawableNormal.addDivider(divider);
            drawableNormal.invalidateSelf();
        }
        if (drawableFocus != null) {
            drawableFocus.addDivider(divider);
            drawableFocus.invalidateSelf();
        }
    }


    @Nullable
    private DividerDrawable getDividerDrawable(TypedArray typedArray, int dividerColor, int backGroundColor) {

        DividerDrawable bg = new DividerDrawable(backGroundColor);
        int alignType = typedArray.getInt(R.styleable.DividerTextView_dv_align, dv_NO);
        if (alignType != dv_NO) {
            DividerDrawable.DividerLine divider = new DividerDrawable.DividerLine();
            divider.setDividerColor(dividerColor);
            divider.setDividerWidth(typedArray.getDimensionPixelSize(R.styleable.DividerTextView_dv_dividerWidth, DEFAULT_DIVIDER_WIDTH));
            divider.setDividerLength(typedArray.getDimensionPixelSize(R.styleable.DividerTextView_dv_dividerLength, 0));
            divider.setMargin(typedArray.getDimensionPixelSize(R.styleable.DividerTextView_dv_margin, -1));
            divider.setEndMargin(typedArray.getDimensionPixelSize(R.styleable.DividerTextView_dv_marginEnd, 0));
            divider.setStartMargin(typedArray.getDimensionPixelSize(R.styleable.DividerTextView_dv_marginStart, 0));
            divider.setAlignType(alignType);
            bg.addDivider(divider);
        }
        return bg;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ColorStateList getPressedColorSelector(int normalColor, int pressedColor) {
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

    public Drawable getBgSelector(View view, TypedArray typedArray) {
        int dividerColor = typedArray.getColor(R.styleable.DividerTextView_dv_dividerColor, DEFAULT_COLOR);
        int dividerColorFocus = typedArray.getColor(R.styleable.DividerTextView_dv_dividerColorFocus, Integer.MAX_VALUE);
        int backgroundColor = typedArray.getColor(R.styleable.DividerTextView_dv_backgroundColor, Color.TRANSPARENT);
        int backgroundColorPress = typedArray.getColor(R.styleable.DividerTextView_dv_backgroundColorPress, Integer.MAX_VALUE);
        int textColorPress = typedArray.getColor(R.styleable.DividerTextView_dv_textColorPress, Integer.MAX_VALUE);
        boolean isRipple = typedArray.getBoolean(R.styleable.DividerTextView_dv_isRipple, false);

        backgroundColorPress = backgroundColorPress == Integer.MAX_VALUE ? backgroundColor : backgroundColorPress;
        dividerColorFocus = dividerColorFocus == Integer.MAX_VALUE ? dividerColor : dividerColorFocus;

        Drawable resultDrawable;
        drawableNormal = getDividerDrawable(typedArray, dividerColor, backgroundColor);
        drawableFocus = getDividerDrawable(typedArray, dividerColorFocus, backgroundColorPress);

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
        if (view instanceof TextView && textColorPress != Integer.MAX_VALUE) {
            ColorStateList textColors = ((TextView) view).getTextColors();
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{new int[]{-android.R.attr.state_pressed}, new int[]{android.R.attr.state_pressed}},
                    new int[]{textColors.getDefaultColor(), textColorPress});
            ((TextView) view).setTextColor(colorStateList);
        }
        return resultDrawable;
    }
}
