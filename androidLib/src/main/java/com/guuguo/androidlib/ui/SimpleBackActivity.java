package com.guuguo.androidlib.ui;

import com.guuguo.androidlib.R;

/**
 * Created by guodeqing on 6/17/16.
 */
public class SimpleBackActivity extends OneFragmentActivity {
    @Override
    public void initView() {
        super.initView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected boolean getDarkMode() {
        return false;
    }

    @Override
    protected int getToolBarResId() {
        return R.layout.base_toolbar_common;
    }
}
