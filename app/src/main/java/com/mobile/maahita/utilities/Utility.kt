package com.mobile.maahita.utilities

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
    }
}