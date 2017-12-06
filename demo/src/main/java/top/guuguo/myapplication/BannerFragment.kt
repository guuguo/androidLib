package top.guuguo.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.guuguo.android.lib.app.LBaseActivitySupport
import com.guuguo.android.lib.app.LBaseFragmentSupport
import kotlinx.android.synthetic.main.fragment_banner.*

class BannerFragment : LBaseFragmentSupport() {

    override fun getLayoutResId() = R.layout.fragment_banner

    override fun getHeaderTitle(): String? = "Banner"

    companion object {
        fun intentTo(activity: Activity) {
            val intent = Intent(activity, BaseTitleActivity::class.java)
            intent.putExtra(LBaseActivitySupport.SIMPLE_ACTIVITY_INFO, BannerFragment::class.java)
            val bundle = Bundle()
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }

    private var lastList: List<VideoBean>? = null
    override fun loadData() {
        super.loadData()
        val mList = mutableListOf(
                VideoBean("http://gslb.miaopai.com/stream/h1uI1dUIew8nFxIV~fQt5~wWk6pus7AMOLtXuw__.mp4?ssig=647627db1ea8de54685a22f197451b96&time_stamp=1510564112835&cookie_id=&vend=1&os=3&partner=1&platform=2", true),
                VideoBean("https://cdn.dribbble.com/users/458522/screenshots/3995732/toy_illustrations.jpg", false),
                VideoBean("https://cdn.dribbble.com/users/956577/screenshots/3956867/comp_1.gif", false),
                VideoBean("https://cdn.dribbble.com/users/956577/screenshots/3936987/comp_1_2.gif", false),
                VideoBean("http://gslb.miaopai.com/stream/ojGMs5EBY15aBD8tGzXpVWeHaGoGrbtUETxH2g__.mp4?ssig=deb6bc54c8d0784541bfc158824251fc&time_stamp=1510563744592&cookie_id=&vend=1&os=3&partner=1&platform=2", true)
        )
        initBanner(mList)
    }

    /**
     * 开始加载广告图片

     * @param newslist
     */
    private fun initBanner(newslist: MutableList<VideoBean>) {
        var isRefresh = false
        if (lastList == null || lastList?.size != newslist.size) {
            isRefresh = true
        } else {
            for (i in newslist.indices) {
                if (newslist[i] != lastList!![i]) {
                    isRefresh = true
                    break
                }
            }
        }
        convenientBanner.pauseScroll()
        if (isRefresh) {

            if (newslist.size == 1)
                convenientBanner.setAutoScrollEnable(false)
            else
                convenientBanner.setAutoScrollEnable(true)
            convenientBanner.offsetLeftAndRight(2)
            convenientBanner.setSource(newslist)
        }
        convenientBanner.setDelay(5)
                .startScroll()
        lastList = newslist
    }

    // 开始自动翻页
    override fun onResume() {
        super.onResume()

        //开始自动翻页
        if (convenientBanner.isValid)
            convenientBanner.startScroll()
    }

    // 停止自动翻页
    override fun onPause() {
        super.onPause()
        //停止翻页
        if (convenientBanner.isValid)
            convenientBanner.pauseScroll()
    }

}