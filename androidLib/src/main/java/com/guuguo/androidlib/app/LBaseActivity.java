package com.guuguo.androidlib.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.flyco.systembar.SystemBarHelper;
import com.guuguo.androidlib.BaseApplication;
import com.guuguo.androidlib.R;
import com.guuguo.androidlib.eventBus.EventModel;
import com.guuguo.androidlib.helper.DrawerHelper;
import com.guuguo.androidlib.helper.ToolBarHelper;
import com.guuguo.androidlib.view.StateDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by guodeqing on 16/5/31.
 */
public abstract class LBaseActivity extends AppCompatActivity {


    protected final String TAG = this.getClass().getSimpleName();

    public final static String SIMPLE_ACTIVITY_INFO = "SIMPLE_ACTIVITY_INFO";
    public final static String SIMPLE_ACTIVITY_TOOLBAR = "SIMPLE_ACTIVITY_TOOLBAR";
    protected ArrayList<LBaseFragment> mFragments = new ArrayList<>();
    private ToolBarHelper mToolBarHelper;
    private DrawerHelper mDrawerHelper;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private int defaultToolBarView = R.layout.base_toolbar_common;
    private int forceToolBarView = 0;

    private BaseApplication myApplication = BaseApplication.getInstance();
    protected LBaseActivity activity;
    private boolean mIsHidden = false;
    private SweetAlertDialog mLoadingDialog;
    private LBaseFragment mFragment;

    public LBaseActivity() {
        activity = this;
    }

    private List<Subscription> mApiCalls = new ArrayList<>();

    /**
     * 管控异步网络请求.避免横竖屏切换出错
     *
     * @param call
     */
    protected void addApiCall(Subscription call) {
        if (call != null)
            mApiCalls.add(call);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.currentActivity = activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFromIntent(getIntent());
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
        setContentView(getLayoutResId());
        init();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        for (Subscription call : mApiCalls) {
            if (call != null && !call.isUnsubscribed())
                call.unsubscribe();
        }
        mApiCalls.clear();
        mLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mLoadingDialog = null;
        super.onPause();
    }


    protected void init() {
        myApplication.currentContainer = getContainer();
        if (mFragment != null) {
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.content, mFragment);
            trans.commitAllowingStateLoss();
            mFragments.add(mFragment);
        }
        initView();
        initVariable();
        loadData();
    }

    protected void loadData() {
    }

    protected void initVariable() {
    }

    protected void initView() {
    }

    protected String getHeaderTitle() {
        return "";
    }

    protected int getMyTheme() {
        return 0;
    }

    protected int getLayoutResId() {
        return R.layout.base_activity_simple_back;
    }

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
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mFragments.size() > 0)
            for (Fragment fra : mFragments)
                fra.onCreateOptionsMenu(menu, getMenuInflater());
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void setContentView(int layoutResID) {
        View contentView = null;
        if (getRealToolBarResId() == 0) {
            mToolBarHelper = new ToolBarHelper(this, layoutResID);
        } else {
            mToolBarHelper = new ToolBarHelper(this, layoutResID, getRealToolBarResId());
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
            if (!isFullScreen()) {
                SystemBarHelper.tintStatusBarForDrawer(this, drawerLayout, color);
            }
        } else {
            super.setContentView(contentView);
        }
        if (getRealToolBarResId() != 0)
            initBar();
        if (!isFullScreen()) {
            SystemBarHelper.immersiveStatusBar(this, 0);
            if (getDarkMode()) {
                SystemBarHelper.setStatusBarDarkMode(this);
            }
        }
    }

    protected void initBar() {
        if (Build.VERSION.SDK_INT >= 21 && getIsAppbarElevation()) {
            getAppbar().setElevation(10.6f);
        }

        if (!isFullScreen()) {
            SystemBarHelper.setHeightAndPadding(this, toolbar.getVisibility() == View.GONE ? getMyToolBar() : toolbar);
        }
        getToolbar().setContentInsetsRelative(0, 0);
        setSupportActionBar(getToolbar()); /*自定义的一些操作*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(getReturnBtnVisible());
        activity.getSupportActionBar().setTitle(getHeaderTitle());
    }

    protected View getMyToolBar() {
        return getAppbar();
    }

    /**
     * 判断是否 fragment activity
     *
     * @param data
     */
    private void initFromIntent(Intent data) {
        try {
            Class<?> clz = (Class<?>) getIntent().getSerializableExtra(SIMPLE_ACTIVITY_INFO);
            if (data == null || clz == null) {
                return;
            }
            forceToolBarView = getIntent().getIntExtra(SIMPLE_ACTIVITY_TOOLBAR, 0);
            try {
                mFragment = (LBaseFragment) clz.newInstance();
                Bundle args = data.getExtras();

                if (args != null) {
                    mFragment.setArguments(args);
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException("generate fragment error. by value:" + clz.toString());
            }
        } catch (Exception e) {

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        if (mFragments.size() > 0)
            for (Fragment fra : mFragments)
                if (fra.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    protected boolean isFullScreen() {
        return false;
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
    public void onEvent(EventModel event) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (LBaseFragment fragment : mFragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        if (getBackExit()) {
            exitDialog();
        } else {
            for (LBaseFragment fragment : mFragments) {
                if (fragment.getUserVisibleHint() && fragment.onBackPressed()) {
                    return;
                }
            }
            super.onBackPressed();
        }
    }

    public void exitDialog() {
        dialogWarningShow("确定要退出吗", "确定", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                finish();
                System.exit(0);
            }
        });
    }

    public void dialogLoadingShow(String msg) {
        if (TextUtils.isEmpty(msg))
            msg = "加载中";
        showSweetDialog(SweetAlertDialog.PROGRESS_TYPE, R.color.colorPrimary, msg, false);
        showLoadingDialogOnMain();
    }

    public void dialogLoadingShowCanTouchDismiss(String msg) {
        if (TextUtils.isEmpty(msg))
            msg = "加载中";
        showSweetDialog(SweetAlertDialog.PROGRESS_TYPE, R.color.colorPrimary, msg, true);
        showLoadingDialogOnMain();
    }

    public void dialogErrorShow(String msg, DialogDismissListener listener) {
        StateDialog stateDialog = new StateDialog(activity)
                .stateStyle(StateDialog.STATE_STYLE.error)
                .autoDismissDelay(500)
                .autoDismiss(true)
                .content(msg);
        stateDialog.show();
//        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
        if (mLoadingDialog != null)
            dialogDismiss(listener, 500);
    }

    public void dialogCompleteShow(String msg, DialogDismissListener listener) {
//        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

        StateDialog stateDialog = new StateDialog(activity)
                .stateStyle(StateDialog.STATE_STYLE.success)
                .autoDismissDelay(500)
                .autoDismiss(true)
                .content(msg);
        stateDialog.show();

        if (mLoadingDialog != null)
            dialogDismiss(listener, 500);
    }

    public void dialogWarningShow(String msg, String confirmStr, SweetAlertDialog.OnSweetClickListener listener) {
        showSweetDialog(SweetAlertDialog.WARNING_TYPE, R.color.colorPrimaryRed, msg, true);
        mLoadingDialog.setConfirmText(confirmStr);
        mLoadingDialog.setCancelText("取消");
        mLoadingDialog.setConfirmClickListener(listener);
        showLoadingDialogOnMain();
    }

    public void dialogDismiss(final DialogDismissListener listener, long delayTime) {
        Observable observable = Observable.just(activity);

        if (delayTime != 0) observable = observable.delay(delayTime, TimeUnit.MILLISECONDS);
        observable.observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1() {
                    @Override
                    public void call(Object o) {
                        try {
                            if (mLoadingDialog != null) {
                                mLoadingDialog.dismiss();
                            }
                            if (listener != null) listener.onDismiss();
                        } catch (WindowManager.BadTokenException e) {
                            Log.i("baseActivity", e.getMessage());
                        }
                    }
                });
    }

    public void dialogDismiss(DialogDismissListener listener) {
        dialogDismiss(listener, 0);
    }

    public void dialogDismiss() {
        dialogDismiss(null, 0);
    }

    private void showSweetDialog(int dialogType, int progressColorRes, String TitleText, boolean cancelAble) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(activity, dialogType);
        } else {
            mLoadingDialog.changeAlertType(dialogType);
        }
        mLoadingDialog.getProgressHelper().setBarColor(getResources().getColor(progressColorRes));
        mLoadingDialog.setTitleText(TitleText);
        mLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mLoadingDialog = null;
            }
        });
        mLoadingDialog.setCanceledOnTouchOutside(cancelAble);
        mLoadingDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialogDismiss();
            }
        });
        mLoadingDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialogDismiss();
            }
        });
    }

    private void showLoadingDialogOnMain() {
        Observable.just(activity).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<LBaseActivity>() {
            @Override
            public void call(LBaseActivity lBaseActivity) {
                try {
                    if (mLoadingDialog != null)
                        mLoadingDialog.show();
                } catch (WindowManager.BadTokenException e) {
                    Log.i("baseActivity", e.getMessage());
                }
            }
        });
    }

    public int getRealToolBarResId() {
        if (forceToolBarView == 0)
            return getToolBarResId();
        else
            return forceToolBarView;
    }

    public interface DialogDismissListener {
        void onDismiss();

    }

    public boolean getIsAppbarElevation() {
        return true;
    }


}

