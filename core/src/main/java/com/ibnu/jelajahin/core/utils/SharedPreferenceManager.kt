package com.ibnu.jelajahin.core.utils

import android.content.Context
import android.content.SharedPreferences
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.KEY_TOKEN
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.PREFS_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class SharedPreferenceManager @Inject constructor(@ApplicationContext context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    fun setStringPreference(prefKey: String, value:String){
        editor.putString(prefKey, value)
        editor.apply()
    }

    fun clearPreferenceByKey(prefKey: String){
        editor.remove(prefKey)
        editor.apply()
    }

    val getToken = prefs.getString(KEY_TOKEN, "")
}