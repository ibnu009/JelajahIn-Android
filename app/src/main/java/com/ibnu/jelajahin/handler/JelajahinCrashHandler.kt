package com.ibnu.jelajahin.handler

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.StringBuilder
import kotlin.system.exitProcess

class JelajahinCrashHandler (private val context: Context) : Thread.UncaughtExceptionHandler {

    private val lineSeparator = "\n"

    override fun uncaughtException(p0: Thread, p1: Throwable) {
        val stackTrace = StringWriter()
        p1.printStackTrace(PrintWriter(stackTrace))
        val errorReport = StringBuilder()
        errorReport.append("************ CAUSE OF ERROR ************\n\n")
        errorReport.append(stackTrace.toString())

        val deviceInformation = StringBuilder()
        deviceInformation.append("\n************ DEVICE INFORMATION ***********\n")
        deviceInformation.append("Brand: ")
        deviceInformation.append(Build.BRAND)
        deviceInformation.append(lineSeparator)
        deviceInformation.append("Device: ")
        deviceInformation.append(Build.DEVICE)
        deviceInformation.append(lineSeparator)
        deviceInformation.append("Model: ")
        deviceInformation.append(Build.MODEL)
        deviceInformation.append(lineSeparator)
        deviceInformation.append("Id: ")
        deviceInformation.append(Build.ID)
        deviceInformation.append(lineSeparator)
        deviceInformation.append("Product: ")
        deviceInformation.append(Build.PRODUCT)
        deviceInformation.append(lineSeparator)

        val firmwareInformation = StringBuilder()
        firmwareInformation.append("\n************ FIRMWARE ************\n")
        firmwareInformation.append("Android Version / SDK: ")
        firmwareInformation.append(Build.VERSION.SDK)
        firmwareInformation.append(lineSeparator)
        firmwareInformation.append("Release: ")
        firmwareInformation.append(Build.VERSION.RELEASE)
        firmwareInformation.append(lineSeparator)
        firmwareInformation.append("Incremental: ")
        firmwareInformation.append(Build.VERSION.INCREMENTAL)
        firmwareInformation.append(lineSeparator)

        val intent = Intent(context, CrashHandlerActivity::class.java)
        intent.putExtra("error", errorReport.toString())
        intent.putExtra("device", deviceInformation.toString())
        intent.putExtra("firmware", firmwareInformation.toString())
        intent.putExtra("activityName", context.javaClass.simpleName)
        context.startActivity(intent)

        Process.killProcess(Process.myPid())
        exitProcess(10)
    }
}