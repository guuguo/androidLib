package com.guuguo.android.lib.app

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.WindowManager
import com.flyco.dialog.listener.OnBtnClickL
import com.flyco.dialog.widget.NormalListDialog
import com.flyco.systembar.SystemBarHelper
import com.guuguo.android.R
import com.guuguo.android.lib.BaseApplication
import com.guuguo.android.lib.extension.initNav
import com.guuguo.android.lib.extension.toast
import com.guuguo.android.lib.utils.FileUtil
import com.guuguo.android.lib.utils.MemoryLeakUtil
import com.guuguo.android.lib.widget.dialog.DialogHelper
import com.guuguo.android.lib.widget.dialog.TipDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * Created by guodeqing on 16/5/31.
 */
@Deprecated("用LBaseActivitySupport吧")
abstract class LNBaseActivity : RxAppCompatActivity() {

    private val myApplication = BaseApplication.get()
    private var mLoadingDialog: TipDialog? = null
    /*fragment*/

    var mFragment: LNBaseFragment? = null

    private val mApiCalls = CompositeDisposable()

    /**
     * rxJava订阅管道管理
     * @param call
     */
    fun addApiCall(call: Disposable?) {
        call?.let {
            mApiCalls.add(call)
        }
    }


    /*onCreate*/
    val BACK_DEFAULT = 0
    val BACK_DIALOG_CONFIRM = 1
    val BACK_WAIT_TIME = 2


    open protected fun getLayoutResId() = R.layout.nbase_activity_simple_back
    var activity = this
    open protected val isFullScreen = false
    open protected val backExitStyle = BACK_DEFAULT
    open protected val backWaitTime = 2000L
    private var TOUCH_TIME: Long = 0

    open fun finishActivitySupport(): Boolean {
        return false
    }

    private fun fullScreen(): Boolean {
        return isFullScreen || mFragment != null && mFragment!!.isFullScreen
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //入栈到pushActivity    
        activity = this
        ActivityManager.pushActivity(this)
        initFromIntent(intent)
        if (!isTaskRoot
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && intent.action != null
                && intent.action == Intent.ACTION_MAIN) {
            finish()
            return
        }

        setFullScreen(fullScreen())
        setLayoutResId(getLayoutResId())
        init(savedInstanceState)
    }

    fun setFullScreen(boolean: Boolean) {
        if (boolean) {
            val params = window.attributes;
            params.flags = params.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.attributes = params;
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            val params = window.attributes;
            params.flags = params.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
            window.attributes = params;
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    open protected fun setLayoutResId(layoutResId: Int) {
        setContentView(layoutResId)
    }

    /*toolbar*/

    open fun getToolBar(): Toolbar? = null
    open fun getAppBar(): ViewGroup? = null
    open protected fun isNavigationBack() = true
    open protected fun isAppbarPaddingToStatusBar() = true
    open protected fun isStatusBarTextDark() = false
    open protected fun initToolBar() {
        val toolBar = getToolBar()
        setSupportActionBar(toolBar)
        if (isNavigationBack())
            toolBar?.initNav(activity)
        title = getHeaderTitle()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mFragment?.let {
            if (mFragment?.onOptionsItemSelected(item)!!)
                return true
        }
        return super.onOptionsItemSelected(item)
    }

    open protected fun initStatusBar() {
        if (!fullScreen()) {
            SystemBarHelper.tintStatusBar(activity, ContextCompat.getColor(activity, R.color.colorPrimary), 0f)
            if (isStatusBarTextDark()) {
                SystemBarHelper.setStatusBarDarkMode(activity)
            }
        }
    }

    /*menu and title*/

    open protected fun getHeaderTitle() = ""
    override fun setTitle(title: CharSequence?) {
        supportActionBar?.title = title
    }

    open protected fun getMenuResId() = 0
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val res = getMenuResId()
        if (res != 0)
            menuInflater.inflate(res, menu)
        mFragment?.onCreateOptionsMenu(menu, menuInflater)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mFragment?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
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
        ActivityManager.popActivity(activity)
        MemoryLeakUtil.fixInputMethodManagerLeak(activity)
        DialogHelper.clearCalls(activity)
        super.onDestroy()
    }

    fun clearApiCall() {
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

    override fun onBackPressed() {
        when (backExitStyle) {
            BACK_DIALOG_CONFIRM ->
                exitDialog()
            BACK_WAIT_TIME -> {
                if (System.currentTimeMillis() - TOUCH_TIME < backWaitTime) {
                    exit()
                } else {
                    TOUCH_TIME = System.currentTimeMillis()
                    getString(R.string.press_again_exit).toast()
                }
            }
            BACK_DEFAULT -> {
                if (mFragment != null && mFragment!!.onBackPressed())
                else {
                    super.onBackPressed()
                }
            }
        }   
    }
    fun exitDialog() {
        dialogWarningShow("确定退出软件？", "取消", "确定", OnBtnClickL { exit() })
    }

    fun exit() {
        ActivityManager.popAllActivity()
    }

    fun dialogLoadingShow(msg: String, canTouchCancel: Boolean = false, maxDelay: Long = 0, listener: DialogInterface.OnDismissListener? = null) {
        DialogHelper.dialogLoadingShow(activity, msg, canTouchCancel, maxDelay, listener)
    }

    fun dialogErrorShow(msg: String, listener: DialogInterface.OnDismissListener? = null, delayTime: Int = 1500) {
        DialogHelper.dialogStateShow(activity, msg, listener, TipDialog.STATE_STYLE.error, delayTime.toLong())
    }

    fun dialogCompleteShow(msg: String, listener: DialogInterface.OnDismissListener? = null, delayTime: Int = 800) {
        DialogHelper.dialogStateShow(activity, msg, listener, TipDialog.STATE_STYLE.success, delayTime.toLong())
    }

    fun dialogMsgShow(msg: String, btnText: String, listener: OnBtnClickL?) {
        DialogHelper.dialogMsgShow(activity, msg, btnText, listener)
    }

    fun dialogWarningShow(msg: String, cancelStr: String, confirmStr: String, listener: OnBtnClickL?) {
        DialogHelper.dialogWarningShow(activity, msg, cancelStr, confirmStr, listener)
    }
    fun dialogWarningShow(msg: String, cancelStr: String, confirmStr: String, listener: OnBtnClickL?,cancelListener: OnBtnClickL?) {
        DialogHelper.dialogWarningShow(activity, msg, cancelStr, confirmStr, listener,cancelListener)
    }
    fun showDialogOnMain(dialog: Dialog) {
        DialogHelper.showDialogOnMain(activity, dialog)
    }

    fun dialogDismiss() {
        DialogHelper.dialogDismiss(activity)
    }

    fun dialogTakePhotoShow(takePhotoListener: DialogInterface.OnClickListener, pickPhotoListener: DialogInterface.OnClickListener) {
        if (FileUtil.isExternalStorageMounted()) {
            val rxPermissions = RxPermissions(this)
            rxPermissions.request(Manifest.permission.CAMERA)
                    .subscribe { granted ->
                        if (granted) { // Always true pre-M
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

