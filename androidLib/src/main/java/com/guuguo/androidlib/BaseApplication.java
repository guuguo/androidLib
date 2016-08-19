package com.guuguo.androidlib;

import android.app.Application;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by guodeqing on 16/3/7.
 */
public abstract class BaseApplication extends Application {

    public CoordinatorLayout currentContainer;
    private static BaseApplication INSTANCE;

    public static BaseApplication getInstance() {
        return INSTANCE;
    }

    public abstract int getAppTheme();

    @Override
    public void onCreate() {
        INSTANCE = this;
        super.onCreate();
        init();
    }

    protected abstract void init();

    public static final String MATERIAL_ICON = "MaterialIcons-Regular.ttf";
    public static final String FONTAWESOME_ICON = "fontawesome-webfont.ttf";

    public Typeface getTypeface(String fontName) {
        if (typefaceHashMap.containsKey(fontName))
            return typefaceHashMap.get(fontName);
        else {
            Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), fontName);
            typefaceHashMap.put(fontName, typeface);
            return typeface;
        }
    }

    private HashMap<String, Typeface> typefaceHashMap = new HashMap<>();

    public void toast(String msg) {
        Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            TastyToast.makeText(this, msg, Toast.LENGTH_SHORT, TastyToast.DEFAULT);
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public void toastLong(String msg) {
        Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            TastyToast.makeText(this, msg, Toast.LENGTH_LONG, TastyToast.DEFAULT);
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }

}
