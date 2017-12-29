package top.guuguo.myapplication.view

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.guuguo.android.lib.extension.log
import  top.guuguo.myapplication.R

/**
 * Created by mimi on 2017/12/7.
 */
class SimpleVideoView : FrameLayout {
    var previewImage: ImageView? = null
    var simpleExoVideoView: SimpleExoPlayerView? = null
    var onStateChangeListener: OnStateChangeListener? = null


    var player: SimpleExoPlayer? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (isInEditMode) {
            return
        }
        val playerLayoutId = R.layout.simple_player_view
        LayoutInflater.from(context).inflate(playerLayoutId, this)

        simpleExoVideoView = findViewById(R.id.player_view)
        previewImage = findViewById(R.id.preview_image)
    }


    fun init(context: Context, contentUrl: String) {
        // Create a default track selector.
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        // Create a player instance.
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
        // Bind the player to the view.
        simpleExoVideoView?.player = player

        player!!.addListener(object : Player.EventListener {
            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
                "onTimelineChanged:${timeline.toString()}".log()
            }

            override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
                "onTracksChanged:${trackGroups.toString()},${trackSelections.toString()}".log()
            }

            override fun onLoadingChanged(isLoading: Boolean) {
                "onLoadingChanged  isLoading:${isLoading.toString()}".log()
                if (isLoading) {
                    onStateChangeListener?.onVideoLoading()
                    previewImage?.visibility = View.VISIBLE
                } else {
                    onStateChangeListener?.onVideoPrepare()
                    previewImage?.visibility = View.GONE
                }
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                "onPlayerStateChanged  playWhenReady:${playWhenReady.toString()} playbackState:${playbackState.toString()}".log()
                when (playbackState) {
                    Player.STATE_ENDED -> onStateChangeListener?.onVideoComplete()
                    Player.STATE_READY -> if (playWhenReady) onStateChangeListener?.onVideoStart() else onStateChangeListener?.onVideoPause()
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                "onRepeatModeChanged  repeatMode:${repeatMode.toString()}".log()

            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                "onShuffleModeEnabledChanged  shuffleModeEnabled:${shuffleModeEnabled.toString()}".log()

            }

            override fun onPlayerError(error: ExoPlaybackException) {
                "onPlayerError  error:${error.message}".log()
            }

            override fun onPositionDiscontinuity(reason: Int) {
                "onPositionDiscontinuity  reason:${reason.toString()}".log()
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                "onPlaybackParametersChanged  playbackParameters:${playbackParameters.toString()}".log()
            }

            override fun onSeekProcessed() {
                "onSeekProcessed".log()
            }
        }
        )
        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "Exo IMA Demo"))

        // Produces Extractor instances for parsing the content media (i.e. not the ad).
        val extractorsFactory = DefaultExtractorsFactory()

        // This is the MediaSource representing the content media (i.e. not the ad).
        val contentMediaSource = ExtractorMediaSource(
                Uri.parse(contentUrl), dataSourceFactory, extractorsFactory, null, null)

        // Compose the content media source into a new AdsMediaSource with both ads and content.
        //    MediaSource mediaSourceWithAds = new AdsMediaSource(contentMediaSource, dataSourceFactory,
        //        adsLoader, simpleExoPlayerView.getOverlayFrameLayout());
        player!!.prepare(contentMediaSource)
        player!!.playWhenReady = true
    }

    fun reset() {
        if (player != null) {
            player!!.release()
            player=null
        }
    }

    fun pause() {
        player?.playWhenReady = false
    }

    fun resume() {
        player?.playWhenReady = true
    }

    fun seekTo(position: Long) {
        player?.seekTo(position)
    }

    fun release() {
        player?.release()
        player = null
    }

    interface OnStateChangeListener {
        fun onVideoComplete()
        fun onVideoStart()
        fun onVideoPause()
        fun onVideoLoading()
        fun onVideoPrepare()
    }
}