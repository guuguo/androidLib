package com.guuguo.android.lib.extension

import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Created by 大哥哥 on 2016/10/24 0024.
 */

fun Number.toStringWith2Decimal():String {
    val df = DecimalFormat("0.00")
    df.roundingMode = RoundingMode.HALF_UP
    return df.format(this)
}