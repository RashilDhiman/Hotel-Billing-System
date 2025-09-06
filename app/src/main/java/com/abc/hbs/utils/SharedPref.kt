package com.abc.hbs.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {

    private val pref: SharedPreferences =
        context.getSharedPreferences("hbs_prefs", Context.MODE_PRIVATE)

    fun saveUser(token: String, role: String) {
        pref.edit().apply {
            putString("token", token)
            putString("role", role)
            apply()
        }
    }

    fun getToken(): String? = pref.getString("token", null)

    fun getRole(): String? = pref.getString("role", null)

    fun clear() {
        pref.edit().clear().apply()
    }
}
