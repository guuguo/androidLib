package com.guuguo.androidlib.ui;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.flyco.systembar.SystemBarHelper;
import com.guuguo.androidlib.BaseApplication;
import com.guuguo.androidlib.R;
import com.guuguo.androidlib.eventBus.SettingChangeEvent;
import com.guuguo.androidlib.helper.DrawerHelper;
import com.guuguo.androidlib.helper.ToolBarHelper;
import com.sdsmdg.tastytoast.TastyToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by guodeqing on 16/5/31.
 */
public abstract class LBaseActivity extends AppCompatActivity implements IBaseActivityInterface {
    protected ArrayList<LBaseFragment> mFragments = new ArrayList<>();
    private ToolBarHelper mToolBarHelper;
    private DrawerHelper mDrawerHelper;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private int defaultToolBarView = R.layout.base_toolbar_common;

    private BaseApplication myApplication = BaseApplication.getInstance();
    protected LBaseActivity activity;
    private boolean mIsHidden = false;
    private SweetAlertDialog mLoadingDialog;

    public LBaseActivity() {
        activity = this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (isFullScreen()) {
            int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            Window window = LBaseActivity.this.getWindow();
            window.setFlags(flag, flag);
        }
        EventBus.getDefault().register(this);
        if (getMyTheme() > 0)
            setTheme(getMyTheme());
        else {
            setTheme(myApplication.getAppTheme());
        }
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        myApplication.currentContainer = getContainer();
        init();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    protected void init() {
        initView();
        initVariable();
        loadData();
    }

    protected int getMyTheme() {
        return 0;
    }

    protected abstract int getLayoutResId();

    protected boolean getDarkMode() {
        return false;
    }

    protected int getToolBarResId() {
        return defaultToolBarView;
    }

    protected int getDrawerResId() {
        return 0;
    }

    public boolean getReturnBtnVisible() {
        return false;
    }

    public void onCreateCustomToolBar(Toolbar toolbar) {
        toolbar.setContentInsetsRelative(0, 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(getReturnBtnVisible());
    }

    @Override
    public void setContentView(int layoutResID) {
        View contentView = null;
        if (getToolBarResId() == 0) {
            mToolBarHelper = new ToolBarHelper(this, layoutResID);
        } else {
            mToolBarHelper = new ToolBarHelper(this, layoutResID, getToolBarResId());
            toolbar = mToolBarHelper.getToolBar();
        }
        contentView = mToolBarHelper.getContentView();
        if (getDrawerResId() != 0) {
            mDrawerHelper = new DrawerHelper(this, contentView, getDrawerResId());
            drawerLayout = mDrawerHelper.getContentView();
            super.setContentView(drawerLayout);
            TypedArray array = getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimary,});
            int color = array.getColor(0, 0xFF00FF);
            array.recycle();
            SystemBarHelper.tintStatusBarForDrawer(this, drawerLayout, color);
        } else {
            super.setContentView(contentView);
            if (getToolbar() != null) {
                SystemBarHelper.immersiveStatusBar(this, 0);
                SystemBarHelper.setHeightAndPadding(this, getToolbar());
                setSupportActionBar(getToolbar()); /*自定义的一些操作*/
                onCreateCustomToolBar(getToolbar());
            } else {
                if (!isFullScreen()) {
                    SystemBarHelper.immersiveStatusBar(this, 0);
                }
            }
        }
        initBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean isFullScreen() {
        return false;
    }

    protected void initBar() {
        if (getToolBarResId() != 0 && Build.VERSION.SDK_INT >= 21 && getIsAppbarElevation()) {
            getAppbar().setElevation(10.6f);
        }
        if (getDarkMode()) {
            SystemBarHelper.setStatusBarDarkMode(this);
            SystemBarHelper.immersiveStatusBar(this, 0);
            if (getToolbar() != null) SystemBarHelper.setHeightAndPadding(this, getToolbar());
        }
    }

    protected boolean getBackExit() {
        return false;
    }

    public AppBarLayout getAppbar() {
        return mToolBarHelper.getmAppBarView();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public NavigationView getNavigationView() {
        return mDrawerHelper.getNavigationView();
    }

    public CoordinatorLayout getContainer() {
        return mToolBarHelper.getContentView();
    }

    @Subscribe
    public void onEvent(SettingChangeEvent event) {
        switch (event.getValue()) {
            case SettingChangeEvent.THEME_CHANGE:
                recreate();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (getBackExit()) {
            dialogWarningShow("确定要退出吗", "确定", sweetAlertDialog -> {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                System.exit(0);
            });
            ;
        } else {
            for (LBaseFragment fragment : mFragments) {
                if (fragment.getUserVisibleHint() && fragment.onBackPressed()) {
                    return;
                }
            }
            super.onBackPressed();
        } 
    }


    public void dialogLoadingShow() {
        showSweetDialog(SweetAlertDialog.PROGRESS_TYPE, R.color.colorPrimary, "加载中", false);
        mLoadingDialog.show();
    }

    public void dialogLoadingShow(String msg) {
        showSweetDialog(SweetAlertDialog.PROGRESS_TYPE, R.color.colorPrimary, msg, false);
        mLoadingDialog.show();
    }

    private void showSweetDialog(int dialogType, int progressColorRes, String TitleText, boolean cancelAble) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(this, dialogType);
        } else {
            mLoadingDialog.changeAlertType(dialogType);
        }
        mLoadingDialog.getProgressHelper().setBarColor(getResources().getColor(progressColorRes));
        mLoadingDialog.setTitleText(TitleText);
        mLoadingDialog.setOnCancelListener(dialog -> mLoadingDialog = null);
        mLoadingDialog.setCanceledOnTouchOutside(cancelAble);
        mLoadingDialog.setConfirmClickListener(sweetAlertDialog -> mLoadingDialog.dismiss());
        mLoadingDialog.setCancelClickListener(sweetAlertDialog -> mLoadingDialog.dismiss());
    }

    public void dialogCompleteShow(String msg) {
        showSweetDialog(SweetAlertDialog.SUCCESS_TYPE, R.color.colorPrimaryBlue, msg, true);
        mLoadingDialog.setConfirmText("知道了");
        mLoadingDialog.showCancelButton(false);
        mLoadingDialog.show();
    }

    public void dialogErrorShow(String msg, String confirmStr, SweetAlertDialog.OnSweetClickListener listener) {
        showSweetDialog(SweetAlertDialog.ERROR_TYPE, R.color.colorPrimaryRed, msg, true);
        mLoadingDialog.setConfirmText(confirmStr);
        mLoadingDialog.setConfirmClickListener(listener);
        mLoadingDialog.setCancelText("取消");
        mLoadingDialog.show();
    }

    public void dialogErrorShow(String msg) {
        showSweetDialog(SweetAlertDialog.ERROR_TYPE, R.color.colorPrimaryRed, msg, true);
        mLoadingDialog.setConfirmText("知道了");
        mLoadingDialog.showCancelButton(false);
        mLoadingDialog.show();
    }

    public void dialogErrorShow(String msg, long delayTime, DialogDismissListener listener) {
        dialogErrorShow(msg);
        dialogDismiss(listener, delayTime);

    }

    public void dialogErrorShow(String msg, int Length) {
        TastyToast.makeText(getApplicationContext(), msg, Length, TastyToast.ERROR);
        dialogDismiss();
    }

    public void dialogCompleteShow(String msg, int Length) {
        TastyToast.makeText(getApplicationContext(), msg, Length, TastyToast.SUCCESS);
        dialogDismiss();
    }

    public void dialogCompleteShow(String msg, long delayTime, DialogDismissListener listener) {
        dialogCompleteShow(msg);
        dialogDismiss(listener, delayTime);
    }

    public void dialogDismiss(DialogDismissListener listener, long... delayTime) {
        new Thread(() -> {
            try {
                if (delayTime.length != 0) Thread.sleep(delayTime[0]);
                else {
                    Thread.sleep(0);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            activity.runOnUiThread(() -> {
                if (mLoadingDialog != null) mLoadingDialog.dismiss();
                if (listener != null) listener.onDismiss();
            });
        }).start();
    }

    public void dialogDismiss() {
        activity.runOnUiThread(() -> {
            if (mLoadingDialog != null) mLoadingDialog.dismiss();
        });
    }

    public interface DialogDismissListener {
        void onDismiss();
    }

    public boolean getIsAppbarElevation() {
        return true;
    }

    public void dialogWarningShow(String msg, String confirmStr, SweetAlertDialog.OnSweetClickListener listener) {
        showSweetDialog(SweetAlertDialog.WARNING_TYPE, R.color.colorPrimaryRed, msg, true);
        mLoadingDialog.setConfirmText(confirmStr);
        mLoadingDialog.setCancelText("取消");
        mLoadingDialog.setConfirmClickListener(listener);
        mLoadingDialog.show();
    }

}

