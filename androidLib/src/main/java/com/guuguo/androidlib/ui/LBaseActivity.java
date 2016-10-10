package com.guuguo.androidlib.ui;

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
import com.sdsmdg.tastytoast.TastyToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by guodeqing on 16/5/31.
 */
public abstract class LBaseActivity extends AppCompatActivity implements IBaseActivityInterface {
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

    private List<Call> mApiCalls = new ArrayList<>();

    /**
     * 管控异步网络请求.避免横竖屏切换出错
     *
     * @param call
     */
    protected void addApiCall(Call call) {
        if (call != null)
            mApiCalls.add(call);
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
        for (Call call : mApiCalls) {
            if (call != null && call.isExecuted())
                call.cancel();
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
        if (getHeaderTitle() != null)
            activity.getSupportActionBar().setTitle(getHeaderTitle());
    }

    protected String getHeaderTitle() {
        return null;
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
        initBar();
        if (!isFullScreen()) {
            SystemBarHelper.immersiveStatusBar(this, 0);
            if (getDarkMode()) {
                SystemBarHelper.setStatusBarDarkMode(this);
            }
        }
    }

    protected void initBar() {
        if (getRealToolBarResId() == 0)
            return;
        if (Build.VERSION.SDK_INT >= 21 && getIsAppbarElevation()) {
            getAppbar().setElevation(10.6f);
        }
        if (!isFullScreen()) {
            SystemBarHelper.setHeightAndPadding(this, getToolbar());
        }
        getToolbar().setContentInsetsRelative(0, 0);

        setSupportActionBar(getToolbar()); /*自定义的一些操作*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(getReturnBtnVisible());
    }

    /**
     * 判断是否 fragment activity
     *
     * @param data
     */
    private void initFromIntent(Intent data) {
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
//        if (mFragment != null && mFragment.onOptionsItemSelected(item)) return true;
//        else return super.onOptionsItemSelected(item);
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
        dialogWarningShow("确定要退出吗", "确定", sweetAlertDialog -> {
            finish();
            System.exit(0);

        });
    }


    public void dialogLoadingShow() {
        showSweetDialog(SweetAlertDialog.PROGRESS_TYPE, R.color.colorPrimary, "加载中", false);
        showLoadingDialogOnMain();
    }

    public void dialogLoadingShow(String msg) {
        showSweetDialog(SweetAlertDialog.PROGRESS_TYPE, R.color.colorPrimary, msg, false);
        showLoadingDialogOnMain();
    }

    private void showSweetDialog(int dialogType, int progressColorRes, String TitleText, boolean cancelAble) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(activity, dialogType);
        } else {
            mLoadingDialog.changeAlertType(dialogType);
        }
        mLoadingDialog.getProgressHelper().setBarColor(getResources().getColor(progressColorRes));
        mLoadingDialog.setTitleText(TitleText);
        mLoadingDialog.setOnCancelListener(dialog -> mLoadingDialog = null);
        mLoadingDialog.setCanceledOnTouchOutside(cancelAble);
        mLoadingDialog.setConfirmClickListener(sweetAlertDialog -> dialogDismiss());
        mLoadingDialog.setCancelClickListener(sweetAlertDialog -> dialogDismiss());
    }

    public void dialogCompleteShow(String msg) {
        showSweetDialog(SweetAlertDialog.SUCCESS_TYPE, R.color.colorPrimaryBlue, msg, true);
        mLoadingDialog.setConfirmText("知道了");
        mLoadingDialog.showCancelButton(false);
        showLoadingDialogOnMain();
    }

    public void dialogErrorShow(String msg, String confirmStr, SweetAlertDialog.OnSweetClickListener listener) {
        showSweetDialog(SweetAlertDialog.ERROR_TYPE, R.color.colorPrimaryRed, msg, true);
        mLoadingDialog.setConfirmText(confirmStr);
        mLoadingDialog.setConfirmClickListener(listener);
        mLoadingDialog.setCancelText("取消");
        showLoadingDialogOnMain();
    }

    public void dialogErrorShow(String msg) {
        showSweetDialog(SweetAlertDialog.ERROR_TYPE, R.color.colorPrimaryRed, msg, true);
        mLoadingDialog.setConfirmText("知道了");
        mLoadingDialog.showCancelButton(false);
        showLoadingDialogOnMain();
    }

    private void showLoadingDialogOnMain() {
        Observable.just(activity).observeOn(AndroidSchedulers.mainThread()).subscribe((a) -> {
            try{
                mLoadingDialog.show();}
            catch (WindowManager.BadTokenException e){
                Log.i("baseActivity",e.getMessage());
            }
        });
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

    public void dialogDismiss(DialogDismissListener listener, long delayTime) {
        Observable.just(activity).delay(delayTime, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(tempActivity -> {
                    if (mLoadingDialog != null) {
                        try {
                            mLoadingDialog.dismiss();
                            if (listener != null) listener.onDismiss();
                        }
                        catch (WindowManager.BadTokenException e){
                            Log.i("baseActivity",e.getMessage());
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

    public void dialogWarningShow(String msg, String confirmStr, SweetAlertDialog.OnSweetClickListener listener) {
        showSweetDialog(SweetAlertDialog.WARNING_TYPE, R.color.colorPrimaryRed, msg, true);
        mLoadingDialog.setConfirmText(confirmStr);
        mLoadingDialog.setCancelText("取消");
        mLoadingDialog.setConfirmClickListener(listener);
        showLoadingDialogOnMain();
    }

}

