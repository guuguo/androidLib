package com.guuguo.android.lib.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;



/**
 * mimi 创造于 2017-08-01.
 * 项目 order
 */

public class SwipeNavigationLayout extends FrameLayout {
    private View navigationView;
    private NavigationListener navigationListener;

    public void setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }

    private final GestureDetector gestureDetector;

    public SwipeNavigationLayout( Context context) {
        this(context, null);
    }

    public SwipeNavigationLayout( Context context,  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeNavigationLayout( Context context, AttributeSet attrs,  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gestureDetector = new GestureDetector(context, new SwipeGesture());
    }

    private void alphaRemoveNavigation() {
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(navigationView, "alpha", 1F, 0F)
                .setDuration(100);
        animator.addListener(animatorStop);
        animator.start();
//        ViewAnimator.animate(navigationView).alpha(1, 0).duration(100).onStop(animatorStop).start();
    }

    private Animator.AnimatorListener animatorStop = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            SwipeNavigationLayout.this.removeView(navigationView);
            navigationView = null;
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (navigationView != null) {
                if (navigationView.getTranslationX() >= navigationView.getWidth()) {
                    toPrevious();
                } else if (navigationView.getTranslationX() <= -navigationView.getWidth()) {
                    toNext();
                } else {
                    ObjectAnimator animator = ObjectAnimator
                            .ofFloat(navigationView, "translationX", navigationView.getTranslationX(), 0F)
                            .setDuration(100);
                    animator.addListener(animatorStop);
                    animator.start();
//                    ViewAnimator.animate(navigationView).translationX(navigationView.getTranslationX(), 0).duration(100).onStop(animatorStop).start();
                }
            }
        }
        gestureDetector.onTouchEvent(ev);
        if (showType == 0 || showType == -1) {
            Log.i("onInterceptTouchEvent", "showType2：" + showType);
            return super.dispatchTouchEvent(ev);
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }
    public void toNext() {
        if (navigationListener != null) {
            navigationListener.navigationNext();
        }
        alphaRemoveNavigation();
    }

    public void toPrevious() {
        if (navigationListener != null) {
            navigationListener.navigationBack();
        }
        alphaRemoveNavigation();
    }


    int showType = -1; // -1导航出现为判断,0导航不需要出现，1左边出现，2右边出现,

    private class SwipeGesture extends GestureDetector.SimpleOnGestureListener {
        float startX = 0;

        @Override
        public boolean onDown(MotionEvent e) {
            startX = e.getX();
            showType = -1;
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (showType == -1 && navigationView == null && Math.abs(distanceX / distanceY) > 2) {
                if (distanceX < 0) {
                    navigationView = LayoutInflater.from(getContext()).inflate(R.layout.widget_include_navigation_left, SwipeNavigationLayout.this, false);
                    showType = 1;
                } else {
                    navigationView = LayoutInflater.from(getContext()).inflate(R.layout.widget_include_navigation_right, SwipeNavigationLayout.this, false);
                    showType = 2;
                }
                SwipeNavigationLayout.this.addView(navigationView);
            } else if (showType == -1) {
                showType = 0;
            }

            if (showType != 0) {
                float trans = (e2.getX() - startX) * 0.6f;
                if (showType == 1) {
                    if (trans > navigationView.getWidth()) {
                        trans = navigationView.getWidth();
                    }
                    navigationView.setTranslationX(trans);
                } else if (showType == 2) {
                    if (trans < -navigationView.getWidth()) {
                        trans = -navigationView.getWidth();
                    }
                    navigationView.setTranslationX(trans);
                }
                return true;
            }
            return false;
        }
    }

    public interface NavigationListener {
        void navigationBack();

        void navigationNext();
    }
}
