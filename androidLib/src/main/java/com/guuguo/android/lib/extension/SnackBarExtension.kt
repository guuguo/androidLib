package com.guuguo.android.lib.extension

import android.graphics.Color
import android.support.design.R
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.TextView

/**
 * Created by 大哥哥 on 2016/10/24 0024.
 */

fun showTipWithAction(view: View, tipText: String, actionText: String, listener: View.OnClickListener) {
    val snack = Snackbar.make(view, tipText, Snackbar.LENGTH_INDEFINITE).setAction(actionText, listener).setActionTextColor(Color.WHITE)
    showSnackBar(snack)

}

fun showTipWithAction(view: View, tipText: String, actionText: String, listener: View.OnClickListener, duration: Int) {
    val snack = Snackbar.make(view, tipText, duration).setAction(actionText, listener)
    showSnackBar(snack)

}

fun showSnackTip(view: View, tipText: String) {
    val snack = Snackbar.make(view, tipText, Snackbar.LENGTH_SHORT)
    showSnackBar(snack)
}

private fun showSnackBar(snack: Snackbar) {
    val text = snack.view.findViewById<TextView>(R.id.snackbar_text)
    text.setTextColor(Color.WHITE)
    snack.show()
}