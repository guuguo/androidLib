package top.guuguo.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.github.florent37.viewanimator.ViewAnimator
import com.guuguo.android.lib.app.LBaseActivitySupport
import com.guuguo.android.lib.app.LBaseFragmentSupport
import com.guuguo.android.lib.extension.safe
import com.guuguo.android.lib.widget.SwipeNavigationLayout
import kotlinx.android.synthetic.main.fragment_banner2.*
import top.guuguo.myapplication.view.SimpleVideoView

class Banner2Fragment : LBaseFragmentSupport() {

    override fun getLayoutResId() = R.layout.fragment_banner2

    override fun getHeaderTitle(): String? = "Banner"

    companion object {
        fun intentTo(activity: Activity) {
            val intent = Intent(activity, BaseTitleActivity::class.java)
            intent.putExtra(LBaseActivitySupport.SIMPLE_ACTIVITY_INFO, Banner2Fragment::class.java)
            val bundle = Bundle()
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }

    private var lastList: List<VideoBean>? = null
    private var currentVideoIndex = 0

    override fun loadData() {
        super.loadData()
        val mList = mutableListOf(
                VideoBean("http://gslb.miaopai.com/stream/ojGMs5EBY15aBD8tGzXpVWeHaGoGrbtUETxH2g__.mp4?ssig=deb6bc54c8d0784541bfc158824251fc&time_stamp=1510563744592&cookie_id=&vend=1&os=3&partner=1&platform=2", true),
                        VideoBean("http://gslb.miaopai.com/stream/h1uI1dUIew8nFxIV~fQt5~wWk6pus7AMOLtXuw__.mp4?ssig=647627db1ea8de54685a22f197451b96&time_stamp=1510564112835&cookie_id=&vend=1&os=3&partner=1&platform=2", true),
                VideoBean("https://cdn.dribbble.com/users/458522/screenshots/3995732/toy_illustrations.jpg", false),
                VideoBean("https://cdn.dribbble.com/users/956577/screenshots/3956867/comp_1.gif", false),
                VideoBean("https://cdn.dribbble.com/users/956577/screenshots/3936987/comp_1_2.gif", false)
        )
        lastList = mList
        initBanner(lastList!![currentVideoIndex])
    }

    override fun initView() {
        super.initView()
        video_view.onStateChangeListener = object : SimpleVideoView.OnStateChangeListener {
            override fun onVideoPrepare() {
            }

            override fun onVideoComplete() {
                navigation.toNext()
            }

            override fun onVideoLoading() {}
            override fun onVideoStart() {}
            override fun onVideoPause() {}
        }
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
    private fun initBanner(bean: VideoBean) {
        ViewAnimator.animate(video_view).alpha(1f, 0f).duration(200).onStop {
            video_view.reset()
            video_view.previewImage?.visibility= View.VISIBLE
            if (bean.url.isEmpty()) {
                Glide.with(video_view).load(R.drawable.bg_zueet_ad_default).into(video_view.previewImage)
            } else {
                Glide.with(video_view).load(bean.url).into(video_view.previewImage)
                if (bean.isVideo) {
                    video_view.init(activity,bean.url)
                }
            }
            ViewAnimator.animate(video_view).alpha(0f, 1f).duration(500).start()
        }.start()
      
    }

    override fun onDestroyView() {
        super.onDestroyView()
        video_view.release()
    }

    override fun onPause() {
        super.onPause()
        video_view.pause()
    }

    override fun onResume() {
        super.onResume()
        video_view.resume()
    }


}