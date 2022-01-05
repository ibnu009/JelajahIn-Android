package com.ibnu.jelajahin.handler

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ibnu.jelajahin.databinding.ActivityCrashHandlerBinding
import com.ibnu.jelajahin.ui.MainActivity
import java.io.IOException
import java.io.RandomAccessFile
import java.lang.Exception

class CrashHandlerActivity : AppCompatActivity() {

    private var _bindingCrashHandlerActivity: ActivityCrashHandlerBinding? = null
    private val binding get() = _bindingCrashHandlerActivity

    private lateinit var errorMessage: String
    private lateinit var deviceInformation: String
    private lateinit var firmwareInformation: String
    private lateinit var activityName: String

    private var memoryUsage = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bindingCrashHandlerActivity = ActivityCrashHandlerBinding.inflate(layoutInflater)
        setContentView(_bindingCrashHandlerActivity?.root)

        if (intent.extras!!.containsKey("error")) errorMessage =
            (intent.getSerializableExtra("error") as String?)!!

        if (intent.extras!!.containsKey("device")) deviceInformation =
            (intent.getSerializableExtra("device") as String?)!!

        if (intent.extras!!.containsKey("firmware")) firmwareInformation =
            (intent.getSerializableExtra("firmware") as String?)!!

        if (intent.extras!!.containsKey("activityName")) activityName =
            (intent.getSerializableExtra("activityName") as String?)!!

        readUsage()

        binding?.btnCheckError?.setOnClickListener {
            showCrashMessage()
        }

        binding?.btnKembali?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showCrashMessage() {
        binding?.errorPreview?.visibility = View.VISIBLE

        val text: String = if (memoryUsage == 0f) {
          """
             $errorMessage
             $deviceInformation
             $firmwareInformation
   
             
             Activity : $activityName
             """.trimIndent()
                }
            else
        {
          """
             $errorMessage
             $deviceInformation
             $firmwareInformation
            
             
             App Usage : $memoryUsage
             """.trimIndent()
        }

        binding?.txvErrorMessage?.text = text

        binding?.btnCloseError?.setOnClickListener {
            hideCrashMessage()
        }
    }

    private fun hideCrashMessage() {
        binding?.errorPreview?.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (binding?.errorPreview?.visibility == View.VISIBLE) {
            binding?.errorPreview?.visibility = View.GONE
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun readUsage() {
        try {
            val reader = RandomAccessFile("/proc/stat", "r")
            var load = reader.readLine()
            var toks = load.split(" +".toRegex()).toTypedArray()
            val idle1 = toks[4].toLong()
            val cpu1 =
                toks[2].toLong() + toks[3].toLong() + toks[5].toLong() + toks[6].toLong() + toks[7].toLong() + toks[8].toLong()
            try {
                Thread.sleep(360)
            } catch (e: Exception) {
            }
            reader.seek(0)
            load = reader.readLine()
            reader.close()
            toks = load.split(" +".toRegex()).toTypedArray()
            val idle2 = toks[4].toLong()
            val cpu2 =
                toks[2].toLong() + toks[3].toLong() + toks[5].toLong() + toks[6].toLong() + toks[7].toLong() + toks[8].toLong()
            memoryUsage = ((cpu2 - cpu1) / (cpu2 + idle2 - (cpu1 + idle1))).toFloat()
            Toast.makeText(applicationContext, "MEMORY USAGE = $memoryUsage", Toast.LENGTH_LONG)
                .show()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }
}