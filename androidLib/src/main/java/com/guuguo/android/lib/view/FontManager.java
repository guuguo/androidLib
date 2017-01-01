package com.guuguo.android.lib.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by 大哥哥 on 2016/9/14 0014.
 */

public class FontManager {
    public static final String MATERIAL_ICON = "MaterialIcons-Regular.ttf";
    public static final String FONTAWESOME_ICON = "fontawesome-webfont.ttf";
    private static final String TAG = FontManager.class.getName();
    
    public static Typeface getTypeface(Context context, String fontName)  {
        if (typefaceHashMap.containsKey(fontName))
            return typefaceHashMap.get(fontName);
        else {
            try {
            Typeface typeface = Typeface.createFromAsset(context.getResources().getAssets(), fontName);
            typefaceHashMap.put(fontName, typeface);
            return typeface;
            } catch (RuntimeException e) {
                Log.e(TAG, "getFont: Can't create font from asset.", e);
                return Typeface.DEFAULT;
            }
        }
    }

    private static HashMap<String, Typeface> typefaceHashMap = new HashMap<>();

}