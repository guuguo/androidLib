package com.guuguo.android.lib;

import android.app.Application;
import android.support.design.widget.CoordinatorLayout;
import android.widget.Toast;

import com.guuguo.android.lib.app.LBaseActivity;
import com.sdsmdg.tastytoast.TastyToast;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by guodeqing on 16/3/7.
 */
public abstract class BaseApplication extends Application {

    public CoordinatorLayout currentContainer;
    protected static BaseApplication INSTANCE;

    public static BaseApplication getInstance() {
        return INSTANCE;
    }

    public LBaseActivity currentActivity = null;

    @Override
    public void onCreate() {
        INSTANCE = this;
        super.onCreate();
        init();
    }

    protected abstract void init();


    public void toast(final String msg) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                TastyToast.makeText(BaseApplication.this, msg, Toast.LENGTH_SHORT, TastyToast.DEFAULT);
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public void toastLong(final String msg) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                TastyToast.makeText(BaseApplication.this, msg, Toast.LENGTH_LONG, TastyToast.DEFAULT);
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }


}
