package top.guuguo.myapplication.ui.activity

import android.view.View
import com.guuguo.android.lib.app.LBaseActivitySupport
import com.guuguo.android.lib.extension.doAvoidDouble
import kotlinx.android.synthetic.main.activity_main.*
import top.guuguo.myapplication.R
import top.guuguo.myapplication.ui.fragment.*

class MainActivity : LBaseActivitySupport() {
    override fun getLayoutResId() = R.layout.activity_main
    override fun initView() {
        super.initView()
        v_divider.setOnClickListener {
            it.doAvoidDouble {
                DividerViewFragment.intentTo(activity)
            }
        }
        v_dialog.setOnClickListener {
            it.doAvoidDouble {
                DialogFragment.intentTo(activity)
            }
        }
        v_divider.setOnClickListener {
            it.doAvoidDouble {
                DividerViewFragment.intentTo(activity)
            }
        }
        v_progress.setOnClickListener {
            it.doAvoidDouble {
                ProgressActivity.intentTo(activity)
            }
        }
        v_swip.setOnClickListener {
            it.doAvoidDouble {
                NavigatorLayoutFragment.intentTo(activity)
            }
        }
        v_flowlayout.setOnClickListener {
            it.doAvoidDouble {
                FlowLayoutFragment.intentTo(activity)
            }
        }
        v_wave.setOnClickListener {
            it.doAvoidDouble {
                WaveViewFragment.intentTo(activity)
            }
        }
        v_test.setOnClickListener {
            it.doAvoidDouble {
                TestFragment.intentTo(activity)
            }
        }
    }
}
