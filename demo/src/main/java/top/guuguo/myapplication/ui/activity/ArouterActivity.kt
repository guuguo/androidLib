package top.guuguo.myapplication.ui.activity

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import com.guuguo.android.lib.app.LBaseActivitySupport
import com.guuguo.android.lib.app.LBaseFragmentSupport
import top.guuguo.myapplication.R
import com.alibaba.android.arouter.launcher.ARouter


open class ArouterActivity : LBaseActivitySupport() {

    companion object {
        val SIMPLE_AROUTER_ACTIVITY_INFO = "SIMPLE_AROUTER_ACTIVITY_INFO"

        fun <A : Activity> intentToArouterPath(activity: Activity, targetFragmentPath: String, targetActivity: Class<A>, map: HashMap<String, *>? = null, targetCode: Int = 0) {
            val intent = Intent(activity, targetActivity)
            intent.putExtra(SIMPLE_AROUTER_ACTIVITY_INFO, targetFragmentPath)
            val bundle = LBaseActivitySupport.bundleData(map)
            intent.putExtras(bundle)

            if (targetCode == 0)
                activity.startActivity(intent)
            else
                activity.startActivityForResult(intent, targetCode)

        }
    }

    override fun getFragmentInstance(data: Intent?): LBaseFragmentSupport? {
        val path = data?.getStringExtra(SIMPLE_AROUTER_ACTIVITY_INFO) ?: return super.getFragmentInstance(data)
        return ARouter.getInstance().build(path).navigation() as LBaseFragmentSupport?
    }
}
