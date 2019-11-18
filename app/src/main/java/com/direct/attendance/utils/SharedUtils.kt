package com.direct.attendance.utils

import android.content.Context

class SharedUtils {

    companion object {
        private const val PREFERENCES = "PREFERENCES"
        private const val UPDATE_DATE = "UPDATE_DATE"

        fun saveUpdateDate(context: Context ,date: String) {
            val sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(UPDATE_DATE, date)
            editor.apply()
        }

        fun loadUpdateDate(context: Context) : String? {
            val sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            return sharedPreferences.getString(UPDATE_DATE, null)
        }

    }
}