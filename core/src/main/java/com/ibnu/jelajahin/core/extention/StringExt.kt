package com.ibnu.jelajahin.core.extention

import android.text.TextUtils
import java.security.MessageDigest

fun String.isEmailValid(): Boolean  {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.sha256(): String {
    return MessageDigest
        .getInstance("SHA-256")
        .digest(this.toByteArray())
        .fold("", { str, it -> str + "%02x".format(it) })
}

fun Int.getUserLevelProgressInPercent(): String{
    val currentProgress = this % 1000 //untuk selalu mendapatkan nilai antara 0 sampai 1000
    val percent = (currentProgress.toDouble() / 1000) * 100
    return "$percent%"
}

fun Int.getUserLevelProgressInPercentAsInt(): Int{
    val currentProgress = this % 1000 //untuk selalu mendapatkan nilai antara 0 sampai 1000
    val percent = (currentProgress.toDouble() / 1000) * 100
    return percent.toInt()
}

fun Int.getUserLevel(): String{
    val level = this.toDouble() / 1000
    return "${level.toInt()}"
}