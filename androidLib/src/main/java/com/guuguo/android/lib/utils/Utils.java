package com.guuguo.android.lib.utils;

import android.content.Context;

import com.guuguo.android.lib.BaseApplication;

/**
 * mimi 创造于 2017-06-14.
 * 项目 order
 */

public class Utils {
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        if (mContext != null)
            return mContext;
        return BaseApplication.get();
    }
}
