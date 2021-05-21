package com.mobile.maahita.utilities

import android.content.Context
import android.content.res.AssetManager
import java.io.IOException
import java.util.*

class Utility {
    companion object {
        fun atStartOfDay(): Date {
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return calendar.time
        }

        fun getJsonDataFromAsset(context: Context, fileName: String): String? {
            val jsonString: String
            try {
                jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
                return jsonString
            } catch (ioException: IOException) {
                throw ioException
            } catch (exception: Exception) {
                throw exception
            }
        }
    }
}