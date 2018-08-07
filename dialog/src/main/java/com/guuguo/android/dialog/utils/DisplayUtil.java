package com.guuguo.android.dialog.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import org.jetbrains.annotations.Nullable;

/**
 * Created by 大哥哥 on 2016/8/26 0026.
 */

public class DisplayUtil {

//    public static void setPadding(final View view, float left, float top, float right, float bottom) {
//        view.setPadding(designedDP2px(left), dip2px(top), designedDP2px(right), dip2px(bottom));
//    }

    private static float density = -1F;
    private static int screenWidthPixels = -1;
    private static int screenHeightPixels = -1;
    private static int screenRealHeightPixels = -1;

//
//    public static float getDensity() {
//        if (density <= 0F) {
//            density = Resources.getSystem().getDisplayMetrics().density;
//        }
//        return density;
//    }
//
//    public static int designedDP2px(float designedDp) {
//        if (screenWidthPixels < 0) {
//            getScreenWidth();
//        }
//        if (px2dip(screenWidthPixels) != 320) {
//            designedDp = designedDp * px2dip(screenWidthPixels) / 320f;
//        }
//        return dip2px(designedDp);
//    }
//
//    public static int dip2px(float dpValue) {
//        return (int) (dpValue * getDensity() + 0.5F);
//    }
//
//    public static int px2dip(float pxValue) {
//        return (int) (pxValue / getDensity() + 0.5F);
//    }

    public static int getScreenWidth() {
        if (screenWidthPixels <= 0) {
            screenWidthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        }
        return screenWidthPixels;
    }
       public static int getScreenRealHeight( Context context) {
        if (!isAllScreenDevice(context)) {
            return getNormalScreenHeight();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return getNormalScreenHeight();
        }

        if (screenRealHeightPixels <= 0) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager == null) {
                return getNormalScreenHeight();
            }
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            float width, height;
            if (point.x < point.y) {
                width = point.x;
                height = point.y;
            } else {
                width = point.y;
                height = point.x;
            }
            screenRealHeightPixels= (int) height;
        }
        return screenRealHeightPixels;
    }

    public static int getNormalScreenHeight() {
        if (screenHeightPixels <= 0) {
            screenHeightPixels = Resources.getSystem().getDisplayMetrics().heightPixels;
        }
        return screenHeightPixels;
    }

    private volatile static boolean mHasCheckAllScreen;
    private volatile static boolean mIsAllScreenDevice;

    public static boolean isAllScreenDevice(@Nullable Context context) {
        if (mHasCheckAllScreen) {
            return mIsAllScreenDevice;
        }
        mHasCheckAllScreen = true;
        mIsAllScreenDevice = false;
        // 低于 API 21的，都不会是全面屏。。。
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            float width, height;
            if (point.x < point.y) {
                width = point.x;
                height = point.y;
            } else {
                width = point.y;
                height = point.x;
            }
            if (height != getNormalScreenHeight() ) {
                mIsAllScreenDevice = true;
            }
        }
        return mIsAllScreenDevice;
    }
}