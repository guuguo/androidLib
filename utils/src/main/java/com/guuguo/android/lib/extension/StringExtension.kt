package com.guuguo.android.lib.extension

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.guuguo.android.lib.utils.ToastUtil
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Pattern

/**
 * Created by mimi on 2016-11-17.
 */

fun String?.safe(default: String = ""): String {
    if (this.isNullOrEmpty())
        return default
    else return this!!;
}

fun String?.toast(isShortToast: Boolean = true): String? {
    if (isShortToast)
        ToastUtil.showSingletonToast(this.safe("(空字符串)"))
    else
        ToastUtil.showSingleLongToast(this.safe("(空字符串)"))
    return this
}



fun String.sha1() = encrypt(this, "SHA-1")
fun String.md5() = encrypt(this, "MD5")

fun String?.isEmail(): Boolean {
    val str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
    val p = Pattern.compile(str)
    val m = p.matcher(this.safe())
    return m.matches()
}

fun String?.isUrl(): Boolean {
    val str = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]"
    val p = Pattern.compile(str)
    val m = p.matcher(this.safe())
    return m.matches()
}

fun String.isPhone(): Boolean {
    val p = "^1([34578])\\d{9}\$".toRegex()
    return matches(p)
}

fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}

/** 网络地址后添加参数 可防止地址后面已经拼接参数地址拼接问题 [map] Map<String, String> 参数集合  */
fun String.addUrlParams(map: Map<String, Any>?): String {
    map ?: return this
    val url = StringBuffer(this)
    if (!url.contains('?')) //判断之前有无参数
        url.append("?")
    else if (url.last() != '?' && url.last() != '&') {
        url.append("&")
    }
    map.forEach { (k, v) ->
        url.append("$k=$v&")
    }
    return url.trimEnd { it == '&' }.toString()
}

/**文字颜色设置 [tints] 文字和颜色对 first 变色文字，second 颜色*/
fun String.tintText(vararg tints: Pair<String, Int>): SpannableString {
    val spannableString = SpannableString(this)
    tints.forEach {
        if (it.first.isEmpty())
            return spannableString
        var i = 0
        val colorFSpan = ForegroundColorSpan(it.second)
        while (i < length - it.first.length) {
            val resIndex = indexOf(it.first, i)
            if (resIndex == -1)
                break
            else {
                spannableString.setSpan(colorFSpan, resIndex, resIndex + it.first.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                i = resIndex + it.first.length
            }
        }
    }
    return spannableString
}

/**文字颜色设置 [tints] 文字和颜色对 first 位置Pair Start to End，second 颜色*/
fun String.tintTextByPosition(vararg tints: Pair<Pair<Int, Int>, Int>): SpannableString {
    val spannableString = SpannableString(this)
    tints.forEach {
        if (it.first.first > it.first.second)
            return spannableString
        val colorFSpan = ForegroundColorSpan(it.second)
        spannableString.setSpan(colorFSpan, it.first.first, it.first.second, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return spannableString
}

/**
 * Extension method to get encrypted string.
 */
private fun encrypt(string: String?, type: String): String {
    if (string.isNullOrEmpty()) {
        return ""
    }
    val md5: MessageDigest
    return try {
        md5 = MessageDigest.getInstance(type)
        val bytes = md5.digest(string!!.toByteArray())
        bytes2Hex(bytes)
    } catch (e: NoSuchAlgorithmException) {
        ""
    }
}

private fun bytes2Hex(bts: ByteArray): String {
    var des = ""
    var tmp: String
    for (i in bts.indices) {
        tmp = Integer.toHexString(bts[i].toInt() and 0xFF)
        if (tmp.length == 1) {
            des += "0"
        }
        des += tmp
    }
    return des
}
