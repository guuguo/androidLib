package com.guuguo.android.lib.extension

import android.graphics.Color
import com.guuguo.android.lib.BaseApplication
import com.guuguo.android.lib.utils.FileUtil

/**
 * Created by 大哥哥 on 2016/10/21 0021.
 */


fun Int.pxToDp(): Int {
    return (this / BaseApplication.get().resources.displayMetrics.density + 0.5f).toInt()
}

fun Int.dpToPx(): Int {
    return (this * BaseApplication.get().resources.displayMetrics.density + 0.5f).toInt()
}

fun Int?.safe(): Int {
    if (this == null)
        return 0
    else return this
}
fun Boolean?.safe(): Boolean {
    if (this == null)
        return false
    else return this
}
fun Long.getFitSize(byte2FitMemorySize: Int = 1): String = FileUtil.byte2FitMemorySize(this, byte2FitMemorySize)
//color
/**
 * 修改颜色透明度
 * @param color
 * *
 * @param alpha
 * *
 * @return
 */
fun Int.changeAlpha(alpha: Int): Int {
    val red = Color.red(this)
    val green = Color.green(this)
    val blue = Color.blue(this)

    return Color.argb(alpha, red, green, blue)
}

fun Number.formatDecimal( decimalLength: Int = 1): String {
    return String.format("%.${decimalLength}f", this.toDouble())
}