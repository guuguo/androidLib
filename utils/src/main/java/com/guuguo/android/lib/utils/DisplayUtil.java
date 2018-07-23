package com.guuguo.android.lib.utils;

import android.content.res.Resources;
import android.view.View;

/**
 * Created by 大哥哥 on 2016/8/26 0026.
 */

public class DisplayUtil {

    public static void setPadding(final View view, float left, float top, float right, float bottom) {
        view.setPadding(designedDP2px(left), dip2px(top), designedDP2px(right), dip2px(bottom));
    }

    private static float density = -1F;
    private static int screenWidthPixels = -1;
    private static int screenHeightPixels = -1;


    public static float getDensity() {
        if (density <= 0F) {
            density = Resources.getSystem().getDisplayMetrics().density;
        }
        return density;
    }

    public static int designedDP2px(float designedDp) {
        if (screenWidthPixels < 0) {
            getScreenWidth();
        }
        if (px2dip(screenWidthPixels) != 320) {
            designedDp = designedDp * px2dip(screenWidthPixels) / 320f;
        }
        return dip2px(designedDp);
    }

    public static int dip2px(float dpValue) {
        return (int) (dpValue * getDensity() + 0.5F);
    }

    public static int px2dip(float pxValue) {
        return (int) (pxValue / getDensity() + 0.5F);
    }

    public static int getScreenWidth() {
        if (screenWidthPixels <= 0) {
            screenWidthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        }
        return screenWidthPixels;
    }


    public static int getScreenHeight() {
        if (screenHeightPixels <= 0) {
            screenHeightPixels = Resources.getSystem().getDisplayMetrics().heightPixels;
        }
        return screenHeightPixels;
    }
}
