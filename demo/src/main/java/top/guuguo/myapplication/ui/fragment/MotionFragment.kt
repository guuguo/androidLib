package top.guuguo.myapplication.ui.fragment

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guuguo.android.lib.app.BaseCupertinoTitleActivity
import com.guuguo.android.lib.app.LBaseActivity
import com.guuguo.android.lib.app.LBaseFragment
import com.guuguo.android.lib.extension.dpToPx
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import top.guuguo.myapplication.R

class MotionFragment : LBaseFragment() {
    override fun getLayoutResId() = R.layout.fragment_motion
    override fun getHeaderTitle() = "MotionLayout"
    override fun initView() {
        super.initView()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    companion object {
        fun intentTo(activity: Activity) {
            LBaseActivity.intentTo(activity, MotionFragment::class.java, BaseCupertinoTitleActivity::class.java)
        }
    }
}