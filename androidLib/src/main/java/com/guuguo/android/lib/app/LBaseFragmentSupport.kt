package com.guuguo.android.lib.app

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.*
import com.guuguo.android.lib.extension.initNav
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * Created by guodeqing on 16/5/31.
 */
abstract class LBaseFragmentSupport : SupportFragment() {

    protected val TAG = this.javaClass.simpleName
    lateinit var activity: LBaseActivitySupport
    protected abstract fun getLayoutResId(): Int

    private var isPrepare = false
    var mFirstLazyLoad = true
    protected var contentView: View? = null
    private val mApiCalls = CompositeDisposable()

    fun addApiCall(call: Disposable?) {
        call?.let {
            mApiCalls.add(call)
        }
    }

    open protected fun setLayoutResId(inflater: LayoutInflater?, resId: Int, container: ViewGroup?): View {
        return inflater!!.inflate(resId, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.activity = context as LBaseActivitySupport
    }

    protected fun init(view: View) {
        activity = getActivity() as LBaseActivitySupport

        initToolbar()
        initView()
        loadData()
        //如果准备好 懒加载
        isPrepare = true
        if (userVisibleHint && !isHidden) {
            lazyLoad()
            mFirstLazyLoad = false
        }
    }


    /*toolbar*/

    open fun getToolBar(): Toolbar? = null //fragment有自己的toolbar就重写该方法。fragment修改toolbar用activity.getSupportActionBar
    open protected fun initToolbar() {
        if (isNavigationBack()) {
            getToolBar()?.initNav(activity)
        }
        val str = getHeaderTitle()
        if (str != null)
            setTitle(str)
    }

    /*init*/

    open protected fun loadData() {}
    open protected fun initVariable(savedInstanceState: Bundle?) {}
    open protected fun initView() {}
    open protected fun getMenuResId() = 0
    open protected fun isNavigationBack() = true

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = super.onCreateView(inflater, container, savedInstanceState)
        initVariable(savedInstanceState)
        if (contentView == null) {
            contentView = setLayoutResId(inflater, getLayoutResId(), container)
        }
        return contentView
    }
    /*menu and title*/

    open protected fun getHeaderTitle(): String? = null
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (getMenuResId() != 0)
            inflater!!.inflate(getMenuResId(), menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    protected open fun setTitle(title: String) {
        activity.title = title
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
    open fun onBackPressed(): Boolean {
        return false
    }

    open fun lazyLoad() {
        activity.mFragment = this
    }


    open val isFullScreen: Boolean
        get() = false

    val isToolBarOverlay: Boolean
        get() = false
}