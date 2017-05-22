package com.guuguo.android.lib.app

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * Created by guodeqing on 16/5/31.
 */
abstract class LNBaseFragment : Fragment() {

    protected val TAG = this.javaClass.simpleName
    protected lateinit var activity: LNBaseActivity
    protected abstract fun getLayoutResId(): Int

    private var isPrepare = false
    var mFirstLazyLoad = true
    protected var contentView: View? = null
    private val mApiCalls = CompositeDisposable()

    protected fun addApiCall(call: Disposable?) {
        call?.let {
            mApiCalls.add(call)
        }
    }

    open protected fun setLayoutResId(inflater: LayoutInflater?, resId: Int, container: ViewGroup?) {
        contentView = inflater!!.inflate(resId, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.activity = context as LNBaseActivity
    }

    protected fun init(view: View) {
        activity = getActivity() as LNBaseActivity
        initView()
        loadData()
        activity.supportActionBar?.setTitle(getHeaderTitle())
        //如果准备好 懒加载
        isPrepare = true
        if (userVisibleHint) {
            lazyLoad()
            mFirstLazyLoad = false
        }
    }
    /*toolbar*/

    open fun getToolBar() = activity.getToolBar()
    open fun getAppBar() = activity.getAppBar()
    open protected fun getHeaderTitle() = ""
    open protected fun isNavigationBack() = true
    open protected fun isAppbarPaddingToStatusBar() = true
    open protected fun isStatusBarTextDark() = false

    /*init*/
    protected fun loadData() {}

    protected fun initVariable() {}
    protected fun initView() {}

    open protected val menuResId = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = super.onCreateView(inflater, container, savedInstanceState)
        initVariable()
        if (contentView == null) {
            setLayoutResId(inflater, getLayoutResId(), container)
        }
        return contentView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (menuResId != 0)
            inflater!!.inflate(menuResId, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view!!)
    }

    override fun onDestroyView() {
        clearApiCall()
        super.onDestroyView()
    }

    protected fun clearApiCall() {
        mApiCalls.clear()
    }


    //如果准备好并可见,而且没有懒加载过 懒加载
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isPrepare) {
            lazyLoad()
            mFirstLazyLoad = false
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && isPrepare) {
            lazyLoad()
            mFirstLazyLoad = false
        }
    }
//

    /**
     * 返回动作,如果返回true,捕捉了返回动作

     * @return
     */
    fun onBackPressed(): Boolean {
        return false
    }

    fun lazyLoad() {}


    val isFullScreen: Boolean
        get() = false

    val isToolBarOverlay: Boolean
        get() = false
}