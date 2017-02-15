package com.guuguo.android.lib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Created by mimi on 2017-01-06.
 */

public class ResUtil {
    public static Drawable getDrawableWithName(Context context, String resourceName) {
        Resources res = context.getResources();
        int picid = res.getIdentifier(resourceName, "drawable",
                context.getPackageName());
        return res.getDrawable(picid);
    }
}
