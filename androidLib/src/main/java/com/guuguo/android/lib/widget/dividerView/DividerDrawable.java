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

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DividerDrawable extends Drawable {
    public static final int DEFAULT_DIVIDER_WIDTH = 1;
    public static final int DEFAULT_COLOR = 0xFFCCCCCC;

    public static final int dv_NO = 0;
    public static final int dv_TL = 1;
    public static final int dv_TC = 2;
    public static final int dv_TR = 3;
    public static final int dv_BL = 4;
    public static final int dv_BC = 5;
    public static final int dv_BR = 6;
    public static final int dv_LT = 7;
    public static final int dv_LC = 8;
    public static final int dv_LB = 9;
    public static final int dv_RT = 10;
    public static final int dv_RC = 11;
    public static final int dv_RB = 12;


    @IntDef({dv_NO, dv_TL, dv_TC, dv_TR, dv_BL, dv_BC, dv_BR, dv_LT, dv_LC, dv_LB, dv_RT, dv_RC, dv_RB})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AlignType {
    }

    @AlignType
    private int alignType = dv_NO;
    private int dividerWidth = DEFAULT_DIVIDER_WIDTH;
    private int dividerLength = 0;
    private int startMargin = 0;
    private int endMargin = 0;
    private int margin = -1;

    @ColorInt
    private int dividerColor = DEFAULT_COLOR;

    public int getAlignType() {
        return alignType;
    }

    public void setDividerLength(int dividerLength) {
        this.dividerLength = dividerLength;
    }

    public void setStartMargin(int startMargin) {
        this.startMargin = startMargin;
    }

    public void setEndMargin(int endMargin) {
        this.endMargin = endMargin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public void setAlignType(int alignType) {
        this.alignType = alignType;
    }

    public void setDividerWidth(int dividerWidth) {
        this.dividerWidth = dividerWidth;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        paint.setColor(dividerColor);
    }

    private final Paint paint;

    public DividerDrawable() {
        paint = new Paint();
    }

 
    @Override
    public void draw(@NonNull Canvas canvas) {
        final int w = canvas.getWidth();
        final int h = canvas.getHeight();
        int[] layouted = layout(w, h);
        canvas.drawRect(layouted[0], layouted[1], layouted[2], layouted[3], paint);
    }

    /**
     * @return int[] {startX, startY, stopX, stopY}
     */
    public int[] layout(int width, int height) {
        int marginStart = getMarginStart();
        int marginEnd = getMarginEnd();
        int[] rect = new int[4];
        int realDividerLength = dividerLength;
        if (realDividerLength == 0)
            realDividerLength = width;
        int hCenterWidth = ((width - marginStart - marginEnd) - realDividerLength) / 2;
        if (hCenterWidth < 0)
            hCenterWidth = 0;

        int vCenterWidth = ((height - marginStart - marginEnd) - realDividerLength) / 2;
        if (vCenterWidth < 0)
            vCenterWidth = 0;

        switch (alignType) {
            case dv_TL:
                rect[0] = marginStart;
                rect[1] = 0;
                rect[2] = Math.min(width - marginEnd, realDividerLength + marginStart);
                rect[3] = dividerWidth;
                break;
            case dv_TC:
                rect[0] = marginStart + hCenterWidth;
                rect[1] = 0;
                rect[2] = Math.min(width - marginEnd - hCenterWidth, realDividerLength + marginStart);
                rect[3] = dividerWidth;
                break;
            case dv_TR:
                rect[0] = Math.max(marginStart, width - realDividerLength - marginEnd);
                rect[1] = 0;
                rect[2] = width - marginEnd;
                rect[3] = dividerWidth;
                break;
            case dv_BL:
                rect[0] = marginStart;
                rect[1] = height - dividerWidth;
                rect[2] = Math.min(width - marginEnd, realDividerLength + marginStart);
                rect[3] = height;
                break;
            case dv_BC:
                rect[0] = marginStart + hCenterWidth;
                rect[1] = height - dividerWidth;
                rect[2] = Math.min(width - marginEnd - hCenterWidth, realDividerLength + marginStart);
                rect[3] = height;
                break;
            case dv_BR:
                rect[0] = Math.max(marginStart, width - realDividerLength - marginEnd);
                rect[1] = height - dividerWidth;
                rect[2] = width - marginEnd;
                rect[3] = height;
                break;
            case dv_LT:
                rect[0] = 0;
                rect[1] = marginStart;
                rect[2] = dividerWidth;
                rect[3] = Math.min(height - marginEnd, realDividerLength + marginStart);
                break;
            case dv_LC:
                rect[0] = 0;
                rect[1] = marginStart + vCenterWidth;
                rect[2] = dividerWidth;
                rect[3] = Math.min(height - marginEnd - vCenterWidth, realDividerLength + marginStart);
                break;
            case dv_LB:
                rect[0] = 0;
                rect[1] = Math.max(marginStart, height - realDividerLength - marginEnd);
                rect[2] = dividerWidth;
                rect[3] = height - marginEnd;
                break;
            case dv_RT:
                rect[0] = width - dividerWidth;
                rect[1] = marginStart;
                rect[2] = width;
                rect[3] = Math.min(height - marginEnd, realDividerLength + marginStart);
                break;
            case dv_RC:
                rect[0] = width - dividerWidth;
                rect[1] = marginStart + vCenterWidth;
                rect[2] = width;
                rect[3] = Math.min(height - marginEnd - vCenterWidth, realDividerLength + marginStart);
                break;
            case dv_RB:
                rect[0] = width - dividerWidth;
                rect[1] = Math.max(marginStart, height - realDividerLength - marginEnd);
                rect[2] = width;
                rect[3] = height - marginEnd;
                break;
        }
        return rect;
    }

    private int getMarginStart() {
        if (margin == -1)
            return startMargin;
        else return margin;
    }

    private int getMarginEnd() {
        if (margin == -1)
            return endMargin;
        else return margin;
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        paint.setAlpha(alpha);
    }


    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
