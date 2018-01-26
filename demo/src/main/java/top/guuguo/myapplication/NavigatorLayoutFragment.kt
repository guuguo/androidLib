package top.guuguo.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.github.florent37.viewanimator.ViewAnimator
import com.guuguo.android.lib.app.LBaseActivitySupport
import com.guuguo.android.lib.app.LBaseFragmentSupport
import com.guuguo.android.lib.extension.safe
import com.guuguo.android.lib.widget.SwipeNavigationLayout
import kotlinx.android.synthetic.main.fragment_banner2.*

class NavigatorLayoutFragment : LBaseFragmentSupport() {

    override fun getLayoutResId() = R.layout.fragment_banner2

    override fun getHeaderTitle(): String? = "NavigatorLayout"

    companion object {
        fun intentTo(activity: Activity) {
            val intent = Intent(activity, BaseTitleActivity::class.java)
            intent.putExtra(LBaseActivitySupport.SIMPLE_ACTIVITY_INFO, NavigatorLayoutFragment::class.java)
            val bundle = Bundle()
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }

    private var lastList: List<String>? = null
    private var currentVideoIndex = 0

    override fun loadData() {
        super.loadData()
        val mList = mutableListOf(
                "https://cdn.dribbble.com/users/458522/screenshots/3995732/toy_illustrations.jpg",
                "http://p0.ifengimg.com/pmop/2017/0626/696AF538F51450AFAD8C2862A8910A4A5F08031E_size29_w800_h494.jpeg",
                "https://cdn.dribbble.com/users/956577/screenshots/3936987/comp_1_2.gif"
                )
        lastList = mList
        initBanner(lastList!![currentVideoIndex])
    }

    override fun initView() {
        super.initView()
        navigation.setNavigationListener(object : SwipeNavigationLayout.NavigationListener {
            override fun navigationNext() {
                lastList?.let {
                    currentVideoIndex++
                    if (currentVideoIndex >= it.size.safe())
                        currentVideoIndex = 0
                    initBanner(lastList!![currentVideoIndex])
                }
            }

            override fun navigationBack() {
                lastList?.let {
                    currentVideoIndex--
                    if (currentVideoIndex < 0)
                        currentVideoIndex = it.size - 1
                    initBanner(lastList!![currentVideoIndex])
                }
            }
        })
    }

    val glideRequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.load_img)
            .priority(Priority.HIGH)


    /**
     * 开始加载广告图片
     * @param newslist
     */
    private fun initBanner(bean: String) {
        ViewAnimator.animate(image_view).alpha(1f, 0f).duration(200).onStop {
            Glide.with(image_view).load(bean).apply(glideRequestOptions).into(image_view)
            ViewAnimator.animate(image_view).alpha(0f, 1f).duration(500).start()
        }.start()

    }


}