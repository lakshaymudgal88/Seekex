package com.example.seekexinternet.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

class Utils {

    companion object {
        fun getCurrentTime(): String {
            val sdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.getDefault())
            return sdf.format(Date())
        }

        fun getFileSize(size: Long): String {
            if (size <= 0) return "0"
            val units = arrayOf("b/s", "kb/s", "mb/s", "gb/s", "tb/s")
            val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
            return DecimalFormat("#,##0.#").format(
                size / 1024.0.pow(digitGroups.toDouble())
            ) + " " + units[digitGroups]
        }

    }
}