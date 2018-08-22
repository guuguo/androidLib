package com.guuguo.android.dialog.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import org.jetbrains.annotations.Nullable;

/**
 * Created by 大哥哥 on 2016/8/26 0026.
 */

public class DisplayUtil {

    private DisplayUtil() {
    }

    private static float density = -1F;
    private static int screenWidthPixels = -1;
    private static int screenHeightPixels = -1;
    private static int screenRealHeightPixels = -1;


    public static int getScreenWidth() {
        if (screenWidthPixels <= 0) {
            screenWidthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        }
        return screenWidthPixels;
    }

    public static int getScreenRealHeight(Context context) {
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
            float height;
            if (point.x < point.y) {
                height = point.y;
            } else {
                height = point.x;
            }
            screenRealHeightPixels = (int) height;
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

    public static boolean isAllScreenDevice(Context context) {
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
            float  height;
            if (point.x < point.y) {
                height = point.y;
            } else {
                height = point.x;
            }
            if (height != getNormalScreenHeight()) {
                mIsAllScreenDevice = true;
            }
        }
        return mIsAllScreenDevice;
    }
}
