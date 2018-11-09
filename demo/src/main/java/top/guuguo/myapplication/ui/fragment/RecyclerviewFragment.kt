package top.guuguo.myapplication.ui.fragment

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guuguo.android.lib.app.BaseCupertinoTitleActivity
import com.guuguo.android.lib.app.LBaseActivity
import com.guuguo.android.lib.app.LBaseFragment
import com.guuguo.android.lib.extension.dpToPx
import com.guuguo.android.lib.widget.CustomMultiWaveView
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import kotlinx.android.synthetic.main.fragment_waveview.*
import top.guuguo.myapplication.R
import top.guuguo.myapplication.ui.guide.HomeGuideDialog

class RecyclerviewFragment : LBaseFragment() {
    override fun getLayoutResId() = R.layout.fragment_recyclerview
    override fun getHeaderTitle() = "waveView"
    override fun initView() {
        super.initView()
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return ViewHolder(TextView(activity).apply { height = 50.dpToPx();width = 200.dpToPx();text = "可怕" })
            }

            override fun getItemCount(): Int {
                return 20
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            }

        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    companion object {
        fun intentTo(activity: Activity) {
            LBaseActivity.intentTo(activity, RecyclerviewFragment::class.java, BaseCupertinoTitleActivity::class.java)
        }
    }
}