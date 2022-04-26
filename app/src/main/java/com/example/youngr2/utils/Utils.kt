package com.example.youngr2.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.util.regex.Pattern

class Utils(private val context: Context) {

    fun hideKeyboard(view: View) {
        (context as Activity).runOnUiThread {
            val inputMethodManager: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    companion object {
        fun removeKorean(str: String): String {
            val regex = "[\\uAC00-\\uD7A3]".toRegex()
            return str.replace(regex, "")
        }

        fun findWordBetweenBracket(str : String) : String {
            var word : String? = ""
            val pattern = Pattern.compile("[(](.*?)[)]")
            val matcher = pattern.matcher(str)
            while (matcher.find()) {
                word = matcher.group(1)
                if (word == null) break
            }
            return word ?: ""
        }
    }
}