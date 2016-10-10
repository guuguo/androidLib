package com.guuguo.androidlib.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.guuguo.androidlib.ui.LBaseSimpleFragmentActivity;

/**
 * Created by guodeqing on 16/6/6.
 */
public abstract class BaseActivityHelper {
    public static void startSimpleBackActivity(Activity activity,  Class<?> clz, int requestCode) {
        startSimpleBackActivity(activity, clz,0, requestCode);
    }

    public static void startSimpleBackActivity(Activity activity,  Class<?> clz,int toolbarRes, int requestCode) {
        startSimpleBackActivity(activity, clz,toolbarRes, requestCode, null);
    }

    public static void startSimpleBackActivity(Activity activity, Class<?> clz,int toolbarRes, int requestCode, Bundle bundle) {
        startOneFragmentActivity(LBaseSimpleFragmentActivity.class, activity, clz,toolbarRes, requestCode, bundle);
    }
    public static void startSimpleBackActivity(Activity activity, Class<?> clz, int requestCode, Bundle bundle) {
        startOneFragmentActivity(LBaseSimpleFragmentActivity.class, activity, clz,0, requestCode, bundle);
    }
    public static void startOneFragmentActivity(Class toActivityClass, Activity activity, Class<?> clz,  int requestCode, Bundle bundle) {
        startOneFragmentActivity(toActivityClass, activity, clz,0, requestCode, bundle);

    }

    public static void startOneFragmentActivity(Class toActivityClass, Activity activity, Class<?> clz,int toolbarRes,  int requestCode, Bundle bundle) {
        Intent intent = new Intent(activity, toActivityClass);
        intent.putExtra(LBaseSimpleFragmentActivity.SIMPLE_ACTIVITY_INFO, clz);
        intent.putExtra(LBaseSimpleFragmentActivity.SIMPLE_ACTIVITY_TOOLBAR, toolbarRes);
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

}

