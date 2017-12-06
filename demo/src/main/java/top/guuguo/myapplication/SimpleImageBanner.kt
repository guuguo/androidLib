package top.guuguo.myapplication;

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import com.devbrackets.android.exomedia.ui.widget.VideoView
import com.guuguo.android.lib.extension.log
import com.guuguo.android.lib.widget.banner.widget.Banner.BaseIndicatorBanner
import com.guuguo.android.lib.widget.banner.widget.LoopViewPager.FixedSpeedScroller
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class SimpleImageBanner @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : BaseIndicatorBanner<VideoBean, SimpleImageBanner>(context, attrs, defStyle) {

    override fun onTitleSelect(tv: TextView?, position: Int) {}

    override fun onCreateItemView(position: Int): View? {
//        val inflate = View.inflate(context, R.layout.item_simple_image, null)
//        val imageView = inflate.findViewById<ImageView>(R.id.iv)
//
////        val videoView = inflate.findViewById<VideoView>(R.id.video_player)
//        val data = mDatas[position]
//        if (mDatas.size == 1 && data.url.isNotEmpty()) {
//            Glide.with(context).load(R.drawable.bg_zueet_ad_default).into(imageView)
//        } else {
//            if (data.isVideo) {
//                val videoView: VideoView = (inflate.findViewById<ViewStub>(R.id.stub_import)).inflate() as VideoView;
//                videoView.setVideoPath(data.url)
//                Glide.with(context).load(data.url).into(videoView.previewImageView)
//                if (position == 0)
//                    videoView.setOnPreparedListener {
//                        videoView.start()
//                        currentVideoView = videoView
//                    }
//                videoViewMap.put(data.url,videoView)
//            } else {
//                Glide.with(context).load(data.url).into(imageView)
//            }
//        }
        return null
    }

    var currentVideoView: VideoView? = null
    var videoViewMap: HashMap<String,View> = hashMapOf()

    init {
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                state.toString().log("Banner")
                if (state != 0) {
                    currentVideoView?.stopPlayback()
                } else {
                     Completable.complete().delay(200, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                currentVideoView?.start()
                            }
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
//                if (videoViewList.size > 0)
//                    currentVideoView = videoViewList[position].findViewById(R.id.video_view)
                "onPageSelected:$position".log("Banner")
//                Completable.complete().delay(100, TimeUnit.MILLISECONDS)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe {
//                            val bean = mDatas[position]
//                            currentVideoView = videoViewMap[bean.url]
//                            if()
//                        }
            }
        })
    }


    /**
     * 开始滚动
     */
    override fun startScroll() {
//        setTransformerClass(AlphaPageTransformer::class.java)
        super.startScroll()
//        setSpeed(1000)
//        mViewPager.adapter.notifyDataSetChanged()
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

//    internal var changeTransformDispose: Disposable? = null
//    private val alphaPageTransForm = AlphaPageTransformer()
//    private val defaultPageTransformer = DefaultPageTransformer()

    override fun goOnScroll() {
        super.goOnScroll()
//        changeTransformDispose = Completable.complete().delay(1, java.util.concurrent.TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
//            mViewPager.setPageTransformer(true, alphaPageTransForm)
//            setSpeed(1000)
//        }
    }

    public override fun isValid(): Boolean {
        return super.isValid()
    }

    override fun pauseScroll() {
//        if (changeTransformDispose != null && !changeTransformDispose!!.isDisposed) {
//            changeTransformDispose!!.dispose()
//        }
//        setSpeed(dragSpeed)
//        mViewPager.setPageTransformer(true, defaultPageTransformer)
        super.pauseScroll()
    }

    override fun onDetachedFromWindow() {
        pauseScroll()
        super.onDetachedFromWindow()
    }

}