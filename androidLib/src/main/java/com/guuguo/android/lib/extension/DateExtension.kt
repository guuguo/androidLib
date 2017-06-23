package com.guuguo.android.lib.extension

import com.guuguo.android.lib.utils.DateUtil.fromDate
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by 大哥哥 on 2016/10/21 0021.
 */
fun Date.getDateSimply(): String {
    var format = SimpleDateFormat("yyyy-MM-dd").format(this)
    when {
        format.equals(Date().date()) ->
            return "今天"
        format.equals(Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000).date()) ->
            return "昨天"
        else ->
            return format
    }
}

/**
 * 获取该时间离现在多久

 * @param format
 * *
 * @param dateTime
 * *
 * @return
 */
fun Date.getTimeSpan(): String {
    var timeSpan = System.currentTimeMillis() - time
    timeSpan = timeSpan / 1000
    if (timeSpan < 60)
        return timeSpan.toString() + "秒前"
    timeSpan /= 60
    if (timeSpan < 60) {
        return timeSpan.toString() + "分钟前"
    }
    timeSpan /= 60
    if (timeSpan < 24) {
        return timeSpan.toString() + "小时前"
    }
    timeSpan /= 24
    if (timeSpan < 30) {
        return timeSpan.toString() + "天前"
    }
    timeSpan = (timeSpan / 30.42).toLong()
    if (timeSpan < 12) {
        return timeSpan.toString() + "月前"
    }
    timeSpan /= 12
    return timeSpan.toString() + "年前"
}

/**
 * 获取毫秒为单位时间戳的离现在多久

 * @param format
 * *
 * @param dateTime
 * *
 * @return
 */
fun Date.getTimeSpanUntilDay(): String {
    val timeSpan = getTimeSpan()
    if (timeSpan.contains("月") || timeSpan.contains("年"))
        return date('-')
    else return timeSpan
}

fun Date.date(char: Char = '/'): String {
    return SimpleDateFormat("yyyy${char}MM${char}dd").format(this)
}

fun Date.year(): String {
    return SimpleDateFormat("yyyy").format(this)
}

fun Date.month(): String {
    return SimpleDateFormat("MM").format(this)
}

fun Date.day(): String {
    return SimpleDateFormat("dd").format(this)
}
