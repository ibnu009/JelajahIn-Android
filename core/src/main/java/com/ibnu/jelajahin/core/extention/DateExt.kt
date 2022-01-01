package com.ibnu.jelajahin.core.extention

import java.text.SimpleDateFormat
import java.util.*

fun String.parseStringDateToMillis(): Long {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
    val date = parser.parse(this) ?: Date()
    return date.time
}

fun String.isThisDateIsTheSameMonthAs(dateString: String): Boolean{
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
    val thisDate = parser.parse(this) ?: Date()
    val comparedDate = parser.parse(dateString) ?: Date()

    val cal1 = Calendar.getInstance()
    val cal2 = Calendar.getInstance()

    cal1.time = thisDate
    cal2.time = comparedDate

    val result: Boolean = if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
        cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
    } else {
        false
    }

    return result
}

fun String.isThisDateIsTheSameDayAs(dateString: String): Boolean{
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
    val fmt = SimpleDateFormat("yyyyMMdd", Locale.ROOT)
    val date1 = fmt.format(parser.parse(this) ?: Date())
    val date2 = fmt.format(parser.parse(dateString) ?: Date())

    return date1.equals(date2)
}

fun String.parseDateDayAndMonth(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
    val formatter = SimpleDateFormat("dd MMMM", Locale.ROOT)
    return formatter.format(parser.parse(this) ?: Date())
}

fun String.parseDateMonthAndYear(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.ROOT)
    return formatter.format(parser.parse(this) ?: Date())
}

fun String.parseDateToDateOnly(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
    val formatter = SimpleDateFormat("dd", Locale.ROOT)
    return formatter.format(parser.parse(this) ?: Date())
}

fun String.parseDateToMonthOnly(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
    val formatter = SimpleDateFormat("MMMM", Locale.ROOT)
    return formatter.format(parser.parse(this) ?: Date())
}

fun String.parseDateToYearOnly(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
    val formatter = SimpleDateFormat("yyyy", Locale.ROOT)
    return formatter.format(parser.parse(this) ?: Date())
}

fun String.parseHour(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
    val formatter = SimpleDateFormat("HH:mm", Locale.ROOT)
    return formatter.format(parser.parse(this) ?: Date())
}

fun Date.parseToString(): String {
    val format = "dd MMMM yyyy"
    val formatter = SimpleDateFormat(format, Locale.ROOT)
    return formatter.format(this)
}