package com.guuguo.android.lib.app

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import com.guuguo.android.R
import com.guuguo.android.lib.extension.initNav
import com.guuguo.android.lib.extension.safe
import com.trello.rxlifecycle2.components.support.RxFragment


/**
 * Created by guodeqing on 16/5/31.
 */
abstract class LBaseFragment : RxFragment() {

    protected val TAG = this.javaClass.simpleName
    lateinit var activity: AppCompatActivity
    protected abstract fun getLayoutResId(): Int

    private var isPrepare = false
    var mFirstLazyLoad = true
    protected var contentView: View? = null

    open protected fun setLayoutResId(inflater: LayoutInflater?, resId: Int, container: ViewGroup?): View {
        return inflater!!.inflate(resId, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context as AppCompatActivity
    }

    protected fun init(view: View) {
        activity = getActivity() as AppCompatActivity
        initToolbar()
        initView()
        loadData()
        loadData(false)

        //如果准备好 懒加载
        isPrepare = true
        if (userVisibleHint && !isHidden) {
            lazyLoad()
            mFirstLazyLoad = false
        }
    }

    open fun onBackPressed():Boolean=false

    /*toolbar*/
    open fun getBackIconRes(): Int = 0

    open fun getToolBar(): Toolbar? = null //fragment有自己的toolbar就重写该方法。fragment修改toolbar用activity.getSupportActionBar
    open protected fun initToolbar() {
        if (isNavigationBack()) {
            getToolBar()?.initNav(activity, getBackIconRes().safe(R.drawable.ic_arrow_back_white_24dp))
        }
        val str = getHeaderTitle()
        if (str != null)
            setTitle(str)
    }

    /*init*/
    @Deprecated("用带参数的方法吧",replaceWith = ReplaceWith("loadData(false)"), level = DeprecationLevel.WARNING)
    open fun loadData() {}
    open fun loadData(isRefresh:Boolean) {}

    protected open fun initVariable(savedInstanceState: Bundle?) {}
    protected open fun initView() {}
    protected open fun getMenuResId() = 0
    open fun isNavigationBack() = true

    open fun overridePendingTransition() = Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = super.onCreateView(inflater, container, savedInstanceState)
        initVariable(savedInstanceState)
        if (contentView == null) {
            contentView = setLayoutResId(inflater, getLayoutResId(), container)
        }
        return contentView
    }
    /*menu and title*/

    open protected fun getHeaderTitle(): String? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (getMenuResId() != 0)
            inflater.inflate(getMenuResId(), menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    protected open fun setTitle(title: String) {
        activity.title = title
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
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
        childFragmentManager.fragments.forEach {
            if (hidden) it.userVisibleHint = false
            else if (it.isVisible) {
                it.userVisibleHint = true
            }

            if (it.isVisible) it.onHiddenChanged(hidden)
        }
    }

    open fun lazyLoad() {
        if (activity is LBaseActivity) {
            (activity as LBaseActivity).mFragment = this
        }
    }


    open val isFullScreen: Boolean
        get() = false
}