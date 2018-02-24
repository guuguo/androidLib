package top.guuguo.myapplication;

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import com.bumptech.glide.Glide
import com.guuguo.android.lib.extension.log
import com.guuguo.android.lib.widget.banner.widget.Banner.BaseIndicatorBanner
import com.guuguo.android.lib.widget.banner.widget.LoopViewPager.FixedSpeedScroller
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import top.guuguo.myapplication.animation.AlphaPageTransformer
import top.guuguo.myapplication.animation.DefaultPageTransformer
import top.guuguo.myapplication.view.SimpleVideoView
class SimpleImageBanner @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : BaseIndicatorBanner<VideoBean, SimpleImageBanner>(context, attrs, defStyle) {

    override fun onTitleSelect(tv: TextView?, position: Int) {}

    override fun onCreateItemView(position: Int): View? {
        val inflate = View.inflate(context, R.layout.item_simple_image2, null)

        val bean = mDatas[position]
        val videoView: SimpleVideoView = inflate.findViewById<SimpleVideoView>(R.id.image_view)

        if (mDatas.size == 1 && bean.url.isNotEmpty()) {
            Glide.with(context).load(R.drawable.bg_zueet_ad_default).into(videoView.previewImage)
        } else {
            if (bean.isVideo) {
                Glide.with(context).load(bean.url).into(videoView.previewImage)
                if (bean.isVideo) {
                    videoView.onStateChangeListener = object : SimpleVideoView.OnStateChangeListener {
                        override fun onVideoPrepare() {
                            if (position == 0)
                                videoView.resume()
                        }

                        override fun onVideoComplete() {
                            videoView.seekTo(0L)
                            videoView.pause()
                        }

                        override fun onVideoLoading() {}
                        override fun onVideoStart() {}
                        override fun onVideoPause() {}
                    }
                    videoView.init(videoView.context, bean.url)

                    videoViewMap.put(bean.url, videoView)

                }
            } else {
                Glide.with(context).load(bean.url).into(videoView.previewImage)
            }
        }
        return inflate
    }

    var currentPlayer: SimpleVideoView? = null
    var videoViewMap: HashMap<String, SimpleVideoView> = hashMapOf()

    init {
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                state.toString().log("Banner")
               
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
//                if (videoViewList.size > 0)
//                    currentVideoView = videoViewList[position].findViewById(R.id.video_view)
                "onPageSelected:$position".log("Banner")
                val bean = mDatas[position]
                currentPlayer?.pause()
                currentPlayer = videoViewMap[bean.url]
                currentPlayer?.resume()
            }
        })
    }


    /**
     * 开始滚动
     */
    override fun startScroll() {
        setTransformerClass(AlphaPageTransformer::class.java)
        super.startScroll()
        setSpeed(1000)
        mViewPager.adapter?.notifyDataSetChanged()
    }

    internal var dragSpeed = 450

    private fun setSpeed(speed: Int) {
        try {
            val e = ViewPager::class.java.getDeclaredField("mScroller")
            e.isAccessible = true
            val interpolator = AccelerateDecelerateInterpolator()
            val myScroller = FixedSpeedScroller(this.mContext, interpolator, speed)
            e.set(this.mViewPager, myScroller)
        } catch (var4: Exception) {
            var4.printStackTrace()
        }

    }

    internal var changeTransformDispose: Disposable? = null
    private val alphaPageTransForm = AlphaPageTransformer()
    private val defaultPageTransformer = DefaultPageTransformer()

    override fun goOnScroll() {
        super.goOnScroll()
        changeTransformDispose = Completable.complete().delay(1, java.util.concurrent.TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            mViewPager.setPageTransformer(true, alphaPageTransForm)
            setSpeed(1000)
        }
    }

    public override fun isValid(): Boolean {
        return super.isValid()
    }

    override fun pauseScroll() {
        if (changeTransformDispose != null && !changeTransformDispose!!.isDisposed) {
            changeTransformDispose!!.dispose()
        }
        setSpeed(dragSpeed)
        mViewPager.setPageTransformer(true, defaultPageTransformer)
        super.pauseScroll()
    }

    override fun onDetachedFromWindow() {
        pauseScroll()
        super.onDetachedFromWindow()
        videoViewMap.forEach{it.value.release()}
    }

}