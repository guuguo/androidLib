package top.guuguo.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.guuguo.android.lib.app.LBaseActivitySupport
import com.guuguo.android.lib.app.LBaseFragmentSupport
import com.guuguo.android.lib.extension.log
import kotlinx.android.synthetic.main.fragment_recycler_banner.*
import top.guuguo.myapplication.view.CardScaleHelper
import top.guuguo.myapplication.view.SimpleVideoView

class RecyclerBannerFragment : LBaseFragmentSupport() {

    override fun getLayoutResId() = R.layout.fragment_recycler_banner

    override fun getHeaderTitle(): String? = "Banner"

    companion object {
        fun intentTo(activity: Activity) {
            val intent = Intent(activity, BaseTitleActivity::class.java)
            intent.putExtra(LBaseActivitySupport.SIMPLE_ACTIVITY_INFO, RecyclerBannerFragment::class.java)
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

    override fun initView() {
        super.initView()
        CardScaleHelper().attachToRecyclerView(rv_banner.recyclerView)
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
        rv_banner.setRvAutoPlaying(true)
        rv_banner.setOnPageSelectListener { i ->
            "OnPageSelectListener:$i".log()
            val bean = newslist[i]
            currentPlayer?.pause()
            currentPlayer = videoViewMap[bean.url]
            currentPlayer?.resume()
        }
        rv_banner.setItemLayoutResId(R.layout.item_simple_image)
        rv_banner.setRvBannerData(newslist)
        rv_banner.setOnSwitchRvBannerListener { i, view ->
            val bean = newslist[i]
            val videoView = view.findViewById<SimpleVideoView>(R.id.image_view)
            if (bean.url.isEmpty()) {
                Glide.with(videoView).load(R.drawable.bg_zueet_ad_default).into(videoView.previewImage)
            } else {
                Glide.with(videoView).load(bean.url).into(videoView.previewImage)
                if (bean.isVideo) {
                    videoView.onStateChangeListener = object : SimpleVideoView.OnStateChangeListener {
                        override fun onVideoPrepare() {
                            if (i == 0) {
                                videoView.resume()
                                currentPlayer = videoView
                            }
                        }
                        override fun onVideoComplete() {
                            videoView.seekTo(0L)
                            videoView.pause()
                        }

                        override fun onVideoLoading() {}
                        override fun onVideoStart() {}
                        override fun onVideoPause() {}
                    }
                    videoView.init(activity, bean.url)

                    videoViewMap.put(bean.url, videoView)

                }
            }
        }
        lastList = newslist
    }

    override fun onDestroyView() {
        super.onDestroyView()
        videoViewMap.forEach { it.value.release() }
    }

    override fun onPause() {
        super.onPause()
        currentPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        currentPlayer?.resume()
    }

    var currentPlayer: SimpleVideoView? = null
    var videoViewMap: HashMap<String, SimpleVideoView> = hashMapOf()

}