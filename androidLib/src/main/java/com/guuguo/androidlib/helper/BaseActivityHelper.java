package com.guuguo.androidlib.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.guuguo.androidlib.info.BaseSimpleBackActivityInfo;
import com.guuguo.androidlib.ui.SimpleBackActivity;

/**
 * Created by guodeqing on 16/6/6.
 */
public abstract class BaseActivityHelper {
    /**
     * activity_编码
     */
    public static final int ACTIVITY_SIMPLE_BACK = 0;

    public static void startSimpleBackActivity(Activity activity, BaseSimpleBackActivityInfo backInfo, int requestCode) {
        startSimpleBackActivity(activity, backInfo, requestCode, null);
    }

    public static void startSimpleBackActivity(Activity activity, BaseSimpleBackActivityInfo backInfo, int requestCode, Bundle bundle) {
        startOneFragmentActivity(SimpleBackActivity.class,activity,backInfo,requestCode,bundle);
    }

    public static void startOneFragmentActivity(Class toActivityClass, Activity activity, BaseSimpleBackActivityInfo backInfo, int requestCode, Bundle bundle) {
        Intent intent = new Intent(activity, toActivityClass);
        intent.putExtra(SimpleBackActivity.SIMPLE_ACTIVITY_INFO, backInfo);
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }
}

