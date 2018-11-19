package top.guuguo.myapplication

import android.content.Context
import android.view.View
import com.guuguo.android.dialog.base.BaseDialog
import com.guuguo.android.dialog.dialog.base.IWarningDialog
import top.guuguo.myapplication.databinding.DialogCustomWarningBinding


class MyDialog(context: Context) : BaseDialog<MyDialog>(context) {
    override fun onCreateView(): View {
      val  rootView = layoutInflater.inflate(R.layout.fragment_guild, null)
        return rootView
    }

    override fun setUiBeforShow() {
        widthRatio(0.91f)
        heightRatio(0.999f)
    }

}//Fast Function
