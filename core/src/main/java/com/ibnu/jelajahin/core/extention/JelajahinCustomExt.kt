package com.ibnu.jelajahin.core.extention

fun Double.toJelajahinAccreditation(): String {
    return when {
        this == 5.0 -> "Sempurna"
        this > 4.0 && this <= 4.9 -> "Luar Biasa"
        this > 3.0 && this <= 3.9 -> "Biasa"
        this > 2.0 && this <= 2.9 -> "Buruk"
        this > 0.1 && this <= 1.9 -> "Sangat Buruk"
        else -> "Not Rated"
    }
}

