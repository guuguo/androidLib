package top.guuguo.myapplication

import android.support.v7.widget.Toolbar

import com.guuguo.android.lib.app.LNBaseActivity
import top.guuguo.progress.view.ProgressRing

class BaseTitleActivity : LNBaseActivity() {
    override fun getLayoutResId(): Int {
        return R.layout.activity_base_title
    }

    override fun getToolBar(): Toolbar? {
        return findViewById(R.id.id_tool_bar) as Toolbar
    }

    override fun getHeaderTitle(): String {
        return "simple view"
    }
}
