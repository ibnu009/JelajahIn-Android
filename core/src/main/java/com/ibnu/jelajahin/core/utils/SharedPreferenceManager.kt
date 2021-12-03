package com.ibnu.jelajahin.core.utils

import android.content.Context
import android.content.SharedPreferences
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.KEY_IS_ALREADY_INTRODUCED
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.KEY_TOKEN
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.PREFS_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class SharedPreferenceManager(context: Context) {
    private var prefs: SharedPreferences = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    fun setStringPreference(prefKey: String, value:String){
        editor.putString(prefKey, value)
        editor.apply()
    }

    fun setBooleanPreference(prefKey: String, value: Boolean){
        editor.putBoolean(prefKey, value)
        editor.apply()
    }

    fun clearPreferenceByKey(prefKey: String){
        editor.remove(prefKey)
        editor.apply()
    }

    val getToken = prefs.getString(KEY_TOKEN, "")
    val isAlreadyIntroduced = prefs.getBoolean(KEY_IS_ALREADY_INTRODUCED, false)
}