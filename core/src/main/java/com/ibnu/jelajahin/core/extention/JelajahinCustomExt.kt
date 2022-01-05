package com.ibnu.jelajahin.core.extention

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.ibnu.jelajahin.core.R
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import kotlin.system.exitProcess

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

fun Activity.showExitJelajahInDialog(){
    AlertDialog.Builder(this).apply {
        setTitle("Keluar Aplikasi")
        setMessage("Apakah Anda yakin untuk keluar dari aplikasi JelajahIn?")
        setNegativeButton("Tidak") { p0, _ ->
            p0.dismiss()
        }
        setPositiveButton("IYA") { _, _ ->
            finish()
            exitProcess(0)
        }
    }.create().show()
}

