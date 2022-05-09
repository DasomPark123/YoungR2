package com.example.youngr2

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar

fun showSnackbar(view: View, msg: String) {
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).run { show() }
}

fun Context.hideKeyboard(view: View) {
    (this as Activity).runOnUiThread {
        val inputMethodManager: InputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
