package com.ibnu.jelajahin.core.extention

import java.text.SimpleDateFormat
import java.util.*

fun String.parseDateMonthAndYear(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.ROOT)
    return formatter.format(parser.parse(this) ?: Date())
}

fun String.parseHour(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
    val formatter = SimpleDateFormat("HH:mm", Locale.ROOT)
    return formatter.format(parser.parse(this) ?: Date())
}