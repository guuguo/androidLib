package com.guuguo.android.lib.app

import android.content.Context
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import io.reactivex.disposables.Disposable


/**
 * Created by guodeqing on 16/5/31.
 */
abstract class LNBaseFragment : Fragment() {

    protected val TAG = this.javaClass.simpleName
    protected lateinit var activity: LNBaseActivity

    private var isPrepare = false
    var mFirstLazyLoad = true
    protected var contentView: View? = null
    private val mApiCalls = ArrayList<Disposable>()

    protected fun addApiCall(call: Disposable?) {
        if (call != null)
            mApiCalls.add(call)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.activity = context as LNBaseActivity
    }

    protected fun init(view: View) {
        activity = getActivity() as LNBaseActivity
        initView()
        loadData()
        activity.supportActionBar?.setTitle(headerTitle)
        //如果可见 懒加载
        isPrepare = true
        if (userVisibleHint) {
            lazyLoad()
            mFirstLazyLoad = false
        }
    }

    protected fun loadData() {}

    protected fun initVariable() {}

    protected fun initView() {}

    protected val headerTitle: String?
        get() = null

    protected val menuResId: Int
        get() = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = super.onCreateView(inflater, container, savedInstanceState)
        initVariable()
        if (contentView == null) {
            contentView = inflater!!.inflate(layoutResId, container, false)
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
        for (call in mApiCalls) {
            if (call != null && !call.isDisposed)
                call.dispose()
        }
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

    val toolbar: Toolbar
        get() = activity.getToolBar()

    val appbar: AppBarLayout
        get() = activity.getAppBar()

    /**
     * 返回动作,如果返回true,捕捉了返回动作

     * @return
     */
    fun onBackPressed(): Boolean {
        return false
    }

    fun lazyLoad() {}

    protected abstract val layoutResId: Int

    val isFullScreen: Boolean
        get() = false

    val isToolBarOverlay: Boolean
        get() = false
}