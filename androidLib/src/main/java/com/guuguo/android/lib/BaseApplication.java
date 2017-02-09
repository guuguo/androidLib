package com.guuguo.android.lib;

import android.app.Application;

import com.guuguo.android.lib.utils.Toastor;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by guodeqing on 16/3/7.
 */
public abstract class BaseApplication extends Application {

    protected static BaseApplication INSTANCE;
    public Toastor toastor;
    public boolean isMaterial = false;

    public static BaseApplication getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        toastor = new Toastor(this);
        INSTANCE = this;
        super.onCreate();
        init();
    }

    protected abstract void init();

    public void toast(final String msg) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                toastor.getToast(msg).show();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public void toastLong(final String msg) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                toastor.getLongToast(msg).show();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }


}
