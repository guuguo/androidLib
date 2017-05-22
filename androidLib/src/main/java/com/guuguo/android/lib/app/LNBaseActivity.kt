package com.guuguo.android.lib.app

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.flyco.dialog.listener.OnBtnClickL
import com.flyco.dialog.widget.NormalListDialog
import com.flyco.systembar.SystemBarHelper
import com.guuguo.android.R
import com.guuguo.android.lib.BaseApplication
import com.guuguo.android.lib.utils.CommonUtil
import com.guuguo.android.lib.utils.FileUtil
import com.guuguo.android.lib.utils.MemoryLeakUtil
import com.guuguo.android.lib.utils.ScreenManager
import com.guuguo.android.lib.view.dialog.StateDialog
import com.guuguo.android.lib.view.dialog.WarningDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


/**
 * Created by guodeqing on 16/5/31.
 */
abstract class LNBaseActivity : AppCompatActivity() {

    /*setting*/
    open protected val backExit = false

    private val myApplication = BaseApplication.getInstance()
    private var mLoadingDialog: StateDialog? = null
    /*fragment*/

    private var mFragment: LNBaseFragment? = null

    private val mApiCalls = CompositeDisposable()

    /**
     * rxJava订阅管道管理
     * @param call
     */
    protected fun addApiCall(call: Disposable?) {
        call?.let {
            mApiCalls.add(call)
        }
    }


    /*onCreate*/

    open protected fun getLayoutResId() = R.layout.nbase_activity_simple_back
    val activity = this
    open protected val isFullScreen = false
    private fun fullScreen(): Boolean {
        return isFullScreen || mFragment != null && mFragment!!.isFullScreen
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //入栈到pushActivity      
        ScreenManager.pushActivity(this)
        initFromIntent(intent)
        if (!isTaskRoot
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && intent.action != null
                && intent.action == Intent.ACTION_MAIN) {
            finish()
            return
        }

        if (fullScreen()) {
            val flag = WindowManager.LayoutParams.FLAG_FULLSCREEN
            val window = this.window
            window.setFlags(flag, flag)
        }

        setLayoutResId(getLayoutResId())
        init(savedInstanceState)
    }

    open protected fun setLayoutResId(layoutResId: Int) {
        setContentView(layoutResId)
    }

    /*toolbar*/

    open protected fun getHeaderTitle() = ""
    open fun getToolBar(): Toolbar? = null
    open fun getAppBar(): AppBarLayout? = null
    open protected fun isNavigationBack() = true
    open protected fun isAppbarPaddingToStatusBar() = true
    open protected fun isStatusBarTextDark() = false

    protected fun initToolBar() {
        val toolBar = getToolBar()
        setSupportActionBar(toolBar)
        toolBar?.title = getHeaderTitle()
        if (isNavigationBack())
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (isNavigationBack())
            when (item.itemId) {
                android.R.id.home -> {
                    this.onBackPressed()
                    return true
                }
            }
//        if (mFragments.size > 0)
//            for (fra in mFragments)
//                if (fra.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    protected fun initStatusBar() {
        val appbar = getAppBar()
        if (appbar != null && isAppbarPaddingToStatusBar()) {
            SystemBarHelper.immersiveStatusBar(this, 0f)
            SystemBarHelper.setPadding(this, getAppBar())
        }
        if (isStatusBarTextDark()) {
            SystemBarHelper.setStatusBarDarkMode(activity)
        }
    }

    /*menu*/

    open protected fun getMenuResId() = 0
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val res = getMenuResId()
        if (res != 0)
            menuInflater.inflate(res, menu)

//        if (mFragments.size > 0)
//            for (fra in mFragments)
//                fra.onCreateOptionsMenu(menu, menuInflater)
        return super.onCreateOptionsMenu(menu)
    }

    /*init*/

    open protected fun initVariable(savedInstanceState: Bundle?) {}
    open protected fun initView() {}
    open protected fun loadData() {}
    protected fun init(savedInstanceState: Bundle?) {
        mFragment?.let {
            val trans = supportFragmentManager.beginTransaction()
            trans.replace(R.id.content, mFragment)
            trans.commitAllowingStateLoss()
//            mFragments.add(mFragment!!)
        }
        initVariable(savedInstanceState)
        initToolBar()
        initStatusBar()
        initView()
        loadData()
    }

    override fun onDestroy() {
        clearApiCall()
        mLoadingDialog = null
        ScreenManager.popActivity(this)
        MemoryLeakUtil.fixInputMethodManagerLeak(activity)
        super.onDestroy()
    }

    protected fun clearApiCall() {
        mApiCalls.clear()
    }

    /**
     * 判断是否 fragment activity

     * @param data
     */
    private fun initFromIntent(data: Intent?) {
        try {
            val clz = intent.getSerializableExtra(SIMPLE_ACTIVITY_INFO) as Class<*>?
            if (data == null || clz == null) {
                return
            }
            try {
                mFragment = clz.newInstance() as LNBaseFragment
                val args = data.extras

                if (args != null) {
                    mFragment!!.arguments = args
                }

            } catch (e: Exception) {
                e.printStackTrace()
                throw IllegalArgumentException("generate fragment error. by value:" + clz.toString())
            }

        } catch (e: Exception) {

        }

    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        for (fragment in mFragments) {
//            fragment.onActivityResult(requestCode, resultCode, data)
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }


    override fun onBackPressed() {
        if (backExit) {
            exitDialog()
        } else {
//            for (fragment in mFragments) {
//                if (fragment.userVisibleHint && fragment.onBackPressed()) {
//                    return
//                }
//            }
            super.onBackPressed()
        }
    }

    fun exitDialog() {
        dialogWarningShow("确定退出软件？", "取消", "确定", object : OnBtnClickL {
            override fun onBtnClick() {
                finish()
                //                android.os.Process.killProcess(android.os.Process.myPid());
                //                Intent home = new Intent(Intent.ACTION_MAIN);
                //                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //                home.addCategory(Intent.CATEGORY_HOME);
                //                startActivity(home);
                //                System.exit(0);
                ScreenManager.popAllActivityExceptOne(this.javaClass)
                System.exit(0)
            }
        })

    }

    @JvmOverloads fun dialogLoadingShow(msg: String, canTouchCancel: Boolean = false, maxDelay: Long = 0, listener: DialogInterface.OnDismissListener? = null) {
        var msg = msg
        if (TextUtils.isEmpty(msg))
            msg = "加载中"
        if (mLoadingDialog == null)
            mLoadingDialog = StateDialog(activity)
        mLoadingDialog!!.stateStyle(StateDialog.STATE_STYLE.loading)
                .content(msg)

        if (maxDelay > 0)
            dialogDismiss(maxDelay, mLoadingDialog, listener)
        mLoadingDialog!!.setCanceledOnTouchOutside(canTouchCancel)
        showDialogOnMain(mLoadingDialog!!)

    }

    fun dialogErrorShow(msg: String, listener: DialogInterface.OnDismissListener?) {
        dialogStateShow(msg, listener, StateDialog.STATE_STYLE.error, 1500)
    }

    fun dialogErrorShow(msg: String, listener: DialogInterface.OnDismissListener?, delayTime: Int) {
        dialogStateShow(msg, listener, StateDialog.STATE_STYLE.error, delayTime.toLong())
    }

    fun dialogCompleteShow(msg: String, listener: DialogInterface.OnDismissListener?, delayTime: Int = 800) {
        dialogStateShow(msg, listener, StateDialog.STATE_STYLE.success, delayTime.toLong())
    }

    fun dialogMsgShow(msg: String, btnText: String, listener: OnBtnClickL?) {
        val normalDialog = WarningDialog(activity)
                .contentGravity(Gravity.CENTER)
                .content(CommonUtil.getSafeString(msg))
                .btnNum(1)
                .btnText(btnText)
        normalDialog.setOnBtnClickL(OnBtnClickL {
            normalDialog.dismiss()
            listener?.onBtnClick()
        })
        showDialogOnMain(normalDialog)
    }

    private fun dialogStateShow(msg: String, listener: DialogInterface.OnDismissListener?, stateStyle: Int, delayTime: Long) {
        val stateDialog = StateDialog(activity)
                .stateStyle(stateStyle)
                .content(CommonUtil.getSafeString(msg))

        stateDialog.setCanceledOnTouchOutside(false)
        showDialogOnMain(stateDialog)
        dialogDismiss(delayTime, stateDialog, listener)
    }

    fun dialogWarningShow(msg: String, cancelStr: String, confirmStr: String, listener: OnBtnClickL?) {
        val normalDialog = WarningDialog(activity)
                .contentGravity(Gravity.CENTER)
                .content(CommonUtil.getSafeString(msg))
                .btnNum(2)
                .btnText(cancelStr, confirmStr)
        normalDialog.setCanceledOnTouchOutside(false)

        normalDialog.setOnBtnClickL(null, OnBtnClickL {
            normalDialog.dismiss()
            listener?.onBtnClick()
        })
        showDialogOnMain(normalDialog)
    }

    fun showDialogOnMain(dialog: Dialog) {
        Completable.complete().observeOn(AndroidSchedulers.mainThread()).subscribe(object : CompletableObserver {
            override fun onSubscribe(d: Disposable) {
                addApiCall(d)
            }

            override fun onComplete() {
                try {
                    dialog.show()
                } catch (e: Exception) {
                }

            }

            override fun onError(e: Throwable) {}
        })
    }

    @JvmOverloads fun dialogDismiss(delay: Long = 0, dialog: Dialog? = mLoadingDialog, listener: DialogInterface.OnDismissListener? = null) {
        Completable.complete().delay(delay, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {
                        addApiCall(d)
                    }

                    override fun onComplete() {
                        if (isValidContext(activity) && dialog != null) {
                            dialog.dismiss()
                        }
                        if (listener != null)
                            try {
                                listener.onDismiss(dialog)
                            } catch (e: Exception) {
                            }

                    }

                    override fun onError(e: Throwable) {}
                }
                )
    }

    fun dialogTakePhotoShow(takePhotoListener: DialogInterface.OnClickListener, pickPhotoListener: DialogInterface.OnClickListener) {
        if (FileUtil.isExternalStorageMounted()) {
            val rxPermissions = RxPermissions(this)
            rxPermissions.request(Manifest.permission.CAMERA)
                    .subscribe { granted ->
                        if (granted!!) { // Always true pre-M
                            val strings = arrayOf("拍照", "从相册中选取")
                            val listDialog = NormalListDialog(activity, strings).title("请选择")
                            listDialog.layoutAnimation(null)
                            listDialog.setOnOperItemClickL { parent, view, position, id ->
                                if (position == 0)
                                    takePhotoListener.onClick(listDialog, position)
                                else if (position == 1)
                                    pickPhotoListener.onClick(listDialog, position)
                                listDialog.dismiss()
                            }
                            showDialogOnMain(listDialog)
                        } else {
                            myApplication.toast("拍照权限被拒绝")
                        }
                    }
        } else {
            myApplication.toast("未检测到外部sd卡")
        }
    }

    private fun isValidContext(c: Context): Boolean {

        val a = c as Activity

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (a.isDestroyed || a.isFinishing) {
                Log.i("YXH", "Activity is invalid." + " isDestoryed-->" + a.isDestroyed + " isFinishing-->" + a.isFinishing)
                return false
            } else {
                return true
            }
        }
        return false
    }

    companion object {

        val SIMPLE_ACTIVITY_INFO = "SIMPLE_ACTIVITY_INFO"
        val SIMPLE_ACTIVITY_TOOLBAR = "SIMPLE_ACTIVITY_TOOLBAR"
    }
}
