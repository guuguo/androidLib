package com.guuguo.android.lib.app

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.WindowManager
import com.flyco.dialog.listener.OnBtnClickL
import com.flyco.dialog.widget.NormalListDialog
import com.guuguo.android.R
import com.guuguo.android.lib.BaseApplication
import com.guuguo.android.lib.extension.initNav
import com.guuguo.android.lib.extension.toast
import com.guuguo.android.lib.utils.FileUtil
import com.guuguo.android.lib.utils.MemoryLeakUtil
import com.guuguo.android.lib.utils.SystemBarHelper
import com.guuguo.android.lib.widget.dialog.DialogHelper
import com.guuguo.android.lib.widget.dialog.TipDialog
import com.guuguo.android.lib.widget.dialog.WarningDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.Serializable
import java.util.concurrent.TimeUnit


/**
 * Created by guodeqing on 16/5/31.
 */
abstract class LBaseActivitySupport : SupportActivity() {

    open fun getApp() = BaseApplication.get()
    private var mLoadingDialog: TipDialog? = null
    /*fragment*/

    var mFragment: LBaseFragmentSupport? = null

    /*onCreate*/
    val BACK_DEFAULT = 0
    val BACK_DIALOG_CONFIRM = 1
    val BACK_WAIT_TIME = 2

    open protected fun getLayoutResId() = R.layout.base_activity_simple_back
    var activity = this
    open protected val isFullScreen = false
    open protected val backExitStyle = BACK_DEFAULT
    open protected val backWaitTime = 2000L
    private var TOUCH_TIME: Long = 0

    private fun fullScreen(): Boolean =
            isFullScreen || mFragment != null && mFragment!!.isFullScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        initFromIntent(intent)
        if (!isTaskRoot
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && intent.action != null
                && intent.action == Intent.ACTION_MAIN) {
            finish()
            return
        }

        setFullScreen(fullScreen())
        initVariable(savedInstanceState)
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
        if (layoutResId != 0)
            setContentView(layoutResId)
    }

    /*toolbar*/
    open fun getToolBar(): Toolbar? = null

    open fun getAppBar(): ViewGroup? = null
    open protected fun isNavigationBack() = true
    open protected fun isStatusBarTextDark() = false
    open protected fun initToolBar() {
        val toolBar = getToolBar()
        setSupportActionBar(toolBar)
        if (isNavigationBack())
            toolBar?.initNav(activity)
        getHeaderTitle()?.let {
            title = it
        }

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

    open protected fun getHeaderTitle():String? = ""
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

    open protected fun initVariable(savedInstanceState: Bundle?) {}
    open protected fun initView() {}
    open protected fun loadData() {}
    @CallSuper
    protected fun init(savedInstanceState: Bundle?) {
        mFragment?.let {
            val trans = supportFragmentManager.beginTransaction()
            trans.replace(R.id.content, mFragment)
            trans.commitAllowingStateLoss()
        }
        initToolBar()
        initStatusBar()
        initView()
        loadData()
    }

    @CallSuper
    override fun onDestroy() {
        mLoadingDialog = null
        MemoryLeakUtil.fixInputMethodManagerLeak(activity)
        DialogHelper.clearCalls(activity)
        super.onDestroy()
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
                mFragment = clz.newInstance() as LBaseFragmentSupport
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

    override fun onBackPressedSupport() {
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
                if (mFragment != null && mFragment!!.onBackPressedSupport())
                else {
                    super.onBackPressedSupport()
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition()
    }

    open fun overridePendingTransition() {
        overridePendingTransition(R.anim.h_fragment_enter, R.anim.h_fragment_exit)
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        overridePendingTransition()
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        overridePendingTransition()
    }

    fun exitDialog() {
        dialogWarningShow("确定退出软件？", "取消", "确定", OnBtnClickL { exit() })
    }

    fun exit() {
        val home = Intent(Intent.ACTION_MAIN)
        home.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        home.addCategory(Intent.CATEGORY_HOME)
        startActivity(home)
        Completable.complete().delay(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            BaseApplication.get().mActivityLifecycle.clear()
        }
    }

    fun dialogLoadingShow(msg: String, canTouchCancel: Boolean = false, maxDelay: Long = 0, listener: DialogInterface.OnDismissListener? = null): TipDialog? {
        return DialogHelper.dialogLoadingShow(activity, msg, canTouchCancel, maxDelay, listener)
    }

    fun dialogErrorShow(msg: String, listener: DialogInterface.OnDismissListener? = null, delayTime: Int = 1500): TipDialog? {
        return DialogHelper.dialogStateShow(activity, msg, listener, TipDialog.STATE_STYLE.error, delayTime.toLong())
    }

    fun dialogCompleteShow(msg: String, listener: DialogInterface.OnDismissListener? = null, delayTime: Int = 800): TipDialog? {
        return DialogHelper.dialogStateShow(activity, msg, listener, TipDialog.STATE_STYLE.success, delayTime.toLong())
    }

    fun dialogMsgShow(msg: String, btnText: String, listener: OnBtnClickL?): WarningDialog? {
        return DialogHelper.dialogMsgShow(activity, msg, btnText, listener)
    }

    fun dialogWarningShow(msg: String, cancelStr: String, confirmStr: String, listener: OnBtnClickL?): WarningDialog? {
        return DialogHelper.dialogWarningShow(activity, msg, cancelStr, confirmStr, listener)
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
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe { granted ->
                        if (granted) { // Always true pre-M
                            val strings = arrayOf("拍照", "从相册中选取")
                            val listDialog = NormalListDialog(activity, strings).title("请选择")
                            listDialog.layoutAnimation(null)
                            listDialog.setOnOperItemClickL { _, _, position, _ ->
                                if (position == 0)
                                    takePhotoListener.onClick(listDialog, position)
                                else if (position == 1)
                                    pickPhotoListener.onClick(listDialog, position)
                                listDialog.dismiss()
                            }
                            showDialogOnMain(listDialog)
                        } else {
                            "拍照权限被拒绝".toast()
                        }
                    }
        } else {
            "未检测到外部sd卡".toast()
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

        fun <F : Fragment, A : Activity> intentTo(activity: Activity, targetFragment: Class<F>, targetActivity: Class<A>, map: HashMap<String, Any>? = null, targetCode: Int = 0) {
            val intent = Intent(activity, targetActivity)
            intent.putExtra(SIMPLE_ACTIVITY_INFO, targetFragment)

            val bundle = Bundle()
            map?.forEach {
                when (it.value) {
                    is String -> bundle.putString(it.key, it.value as String)
                    is Int -> bundle.putInt(it.key, it.value as Int)
                    is Float -> bundle.putFloat(it.key, it.value as Float)
                    is Parcelable -> bundle.putParcelable(it.key, it.value as Parcelable)
                    is Serializable -> bundle.putSerializable(it.key, it.value as Serializable)
                }
            }

            intent.putExtras(bundle)
            if (targetCode == 0)
                activity.startActivity(intent)
            else
                activity.startActivityForResult(intent, targetCode)
        }
    }
}

