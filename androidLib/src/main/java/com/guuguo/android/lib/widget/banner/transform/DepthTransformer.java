package com.guuguo.android.lib.widget.banner.transform;

import android.view.View;
import android.support.v4.view.ViewPager;


public class DepthTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE_DEPTH = 0.75f;

    @Override
    public void transformPage(View page, float position) {
        float alpha;
        float scale;
        float translationX;
        if (position > 0 && position < 1) {
            // moving to the right
            alpha = (1 - position);
            scale = MIN_SCALE_DEPTH + (1 - MIN_SCALE_DEPTH) * (1 - Math.abs(position));
            translationX = (page.getWidth() * -position);
        } else {
            // use default for all other cases
            alpha = 1;
            scale = 1;
            translationX = 0;
        }

        page.setAlpha(alpha);
        page.setTranslationX(translationX);
        page.setScaleX(scale);
        page.setScaleY(scale);
    }
}
