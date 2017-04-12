package com.guuguo.android.lib;

import android.app.Activity;
import android.app.Application;

import com.guuguo.android.lib.utils.Toastor;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;


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
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                toastor.getSingletonToast(msg).show();
                e.onComplete();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public void toastLong(final String msg) {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                toastor.getSingleLongToast(msg).show();
                e.onComplete();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }

}
