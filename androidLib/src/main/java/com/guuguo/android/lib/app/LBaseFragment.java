package com.guuguo.android.lib.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by guodeqing on 16/5/31.
 */
public abstract class LBaseFragment extends Fragment {

    protected final String TAG = this.getClass().getSimpleName();
    protected LBaseActivity activity;

    private boolean isPrepare = false;
    protected boolean mFirstLazyLoad = true;
    protected View contentView;
    private List<Subscription> mApiCalls = new ArrayList<>();

    protected void addApiCall(Subscription call) {
        if (call != null)
            mApiCalls.add(call);
    }

    protected void init(View view) {
        activity = (LBaseActivity) getActivity();
        initView();
        initVariable();
        loadData();
        if (getHeaderTitle() != null && activity.getRealToolBarResId() != 0)
            activity.getSupportActionBar().setTitle(getHeaderTitle());
        //如果可见 懒加载
        isPrepare = true;
        if (getUserVisibleHint()) {
            lazyLoad();
            mFirstLazyLoad = false;
        }
    }

    protected void loadData() {
    }

    protected void initVariable() {
    }

    protected void initView() {
    }

    protected String getHeaderTitle() {
        return null;
    }

    protected int getMenuResId() {
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView == null) {
            contentView = inflater.inflate(getLayoutResId(), container, false);
        }
        return contentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getMenuResId() != 0)
            inflater.inflate(getMenuResId(), menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public void onDestroyView() {
        for (Subscription call : mApiCalls) {
            if (call != null && !call.isUnsubscribed())
                call.unsubscribe();
        }
        mApiCalls.clear();
        super.onDestroyView();
    }


    //如果准备好并可见,而且没有懒加载过 懒加载
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isPrepare) {
            lazyLoad();
            mFirstLazyLoad = false;
        }
    }

    public Toolbar getToolbar() {
        return activity.getToolbar();
    }

    public AppBarLayout getAppbar() {
        return activity.getAppbar();
    }

    /**
     * 返回动作,如果返回true,捕捉了返回动作
     *
     * @return
     */
    public boolean onBackPressed() {
        return false;
    }

    public void lazyLoad() {
    }

    protected abstract int getLayoutResId();

    public boolean isFullScreen() {
        return false;
    }

    public boolean isToolBarOverlay() {
        return false;
    }
}