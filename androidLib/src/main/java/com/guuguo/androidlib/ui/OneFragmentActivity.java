package com.guuguo.androidlib.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.guuguo.androidlib.R;
import com.guuguo.androidlib.info.BaseSimpleBackActivityInfo;

/**
 * Created by guodeqing on 6/21/16.
 */
public class OneFragmentActivity extends LBaseActivity {
    public final static String SIMPLE_ACTIVITY_INFO = "SIMPLE_ACTIVITY_INFO";

    protected LBaseFragment mFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.base_activity_simple_back;
    }

    @Override
    protected int getToolBarResId() {
        return 0;
    }

    @Override
    protected boolean getDarkMode() {
        return true;
    }

    @Override
    public void initVariable() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void initView() {
        BaseSimpleBackActivityInfo mPage = getIntent().getParcelableExtra(SIMPLE_ACTIVITY_INFO);
        initFromIntent(mPage, getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mFragment != null) mFragment.onCreateOptionsMenu(menu, getMenuInflater());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mFragment != null && mFragment.onOptionsItemSelected(item)) return true;
        else return super.onOptionsItemSelected(item);
    }

    private void initFromIntent(BaseSimpleBackActivityInfo page, Intent data) {
        if (data == null) {
            throw new RuntimeException("you must provide a page info to display");
        }
        if (page == null) {
            throw new IllegalArgumentException("can not find page");
        }
        if (page.getTitle() != 0 && getToolbar() != null)
            getSupportActionBar().setTitle(page.getTitle());

        try {
            mFragment = (LBaseFragment) page.getClz().newInstance();
            Bundle args = data.getExtras();

            if (args != null) {
                mFragment.setArguments(args);
            }
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.content, mFragment);
            trans.commitAllowingStateLoss();

            mFragments.add(mFragment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("generate fragment error. by value:" + page.toString());
        }
    }


}
