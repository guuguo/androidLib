package com.guuguo.androidlib.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guuguo.androidlib.BaseApplication;
import com.guuguo.androidlib.eventBus.SettingChangeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by guodeqing on 16/5/31.
 */
public abstract class LBaseFragment extends Fragment implements IBaseActivityInterface {
    protected LBaseActivity activity;
    private BaseApplication myApplication=BaseApplication.getInstance();
    private boolean isPrepare = false;
    protected boolean isFirstLazyLoad = true;
    protected View contentView;

    protected void init(View view) {
        activity = (LBaseActivity) getActivity();
        initView();
        initVariable();
        loadData();
        //如果可见 懒加载
        isPrepare = true;
        if (getUserVisibleHint()) {
            lazyLoad();
            isFirstLazyLoad = false;
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView == null) {
            contentView = inflater.inflate(getLayoutResId(), container, false);
        }
        EventBus.getDefault().register(this);
        init(contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }


    //如果准备好并可见,而且没有懒加载过 懒加载
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isPrepare) {
            lazyLoad();
            isFirstLazyLoad = false;
        }
    }

    public Toolbar getToolbar() {
        return activity.getToolbar();
    }

    public AppBarLayout getAppbar() {return activity.getAppbar();}

    /**
     * 返回动作,如果返回true,捕捉了返回动作
     * @return
     */
    public boolean onBackPressed() {return false;}

    public void lazyLoad() {}

    @Subscribe
    public void onEvent(SettingChangeEvent event) {
    }

    protected abstract int getLayoutResId() ;
}