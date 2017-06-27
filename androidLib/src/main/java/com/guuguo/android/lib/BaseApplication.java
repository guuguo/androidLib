package com.guuguo.android.lib;

import android.app.Application;

import com.guuguo.android.lib.utils.ToastUtil;
import com.guuguo.android.lib.utils.Utils;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by guodeqing on 16/3/7.
 */
public abstract class BaseApplication extends Application {

    protected static BaseApplication INSTANCE;

    public static BaseApplication get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        Utils.init(this);
        INSTANCE = this;
        super.onCreate();
        init();
    }

    protected abstract void init();

    public void toast(final String msg) {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                ToastUtil.showSingletonToast(msg);
                e.onComplete();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public void toastLong(final String msg) {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                ToastUtil.getSingleLongToast(msg).show();
                e.onComplete();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }

}
