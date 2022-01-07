package com.ibnu.jelajahin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.ibnu.jelajahin.handler.JelajahinCrashHandler
import dagger.hilt.android.HiltAndroidApp
import net.gotev.uploadservice.UploadServiceConfig
import net.gotev.uploadservice.logger.UploadServiceLogger
import net.gotev.uploadservice.okhttp.OkHttpStack
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltAndroidApp
open class BaseApplication : Application() {

    companion object {
        const val notificationChannelID = "JelajahinChannel"
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                notificationChannelID,
                "Jelajahin Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        createNotificationChannel()

        UploadServiceConfig.initialize(
            context = this,
            defaultNotificationChannel = notificationChannelID,
            debug = BuildConfig.DEBUG
        )

        UploadServiceConfig.httpStack = OkHttpStack(getOkHttpClient())
        UploadServiceConfig.idleTimeoutSeconds = 60 * 5
        UploadServiceConfig.bufferSizeBytes = 4096
        UploadServiceLogger.setLogLevel(UploadServiceLogger.LogLevel.Debug)
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(300L, TimeUnit.SECONDS)
            .readTimeout(300L, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor { message: String? ->
                if (message != null) {
                    Timber.tag("message").d(message)
                }
            }.setLevel(HttpLoggingInterceptor.Level.BODY))
            .cache(null)
            .build()
    }
}