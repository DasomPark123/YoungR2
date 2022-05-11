package com.example.youngr2.utils

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.youngr2.R
import com.google.android.material.snackbar.Snackbar

val CALORIE = "kcal"
val CARBOHYDRATE = "탄수화물"
val SUGAR = "당류"
val PROTEIN = "단백질"
val FAT = "지방"
val SATURATED_FAT = "포화지방"
val TRANS_FAT = "트랜스지방"
val CHOLESTEROL = "콜레스테롤"
val SALT = "나트륨"
val UNKNOWN = "알수없음"

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

fun Context.showProgressDialog() : AlertDialog {
    val llPadding = 30

    var llParam = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        gravity = Gravity.CENTER
    }

    val ll = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(llPadding, llPadding, llPadding, llPadding)
        gravity = Gravity.CENTER
        layoutParams = llParam
    }

    val progressBar = ProgressBar(this).apply {
        isIndeterminate = true
        setPadding(0, 0, llPadding, 0)
        layoutParams = llParam
    }

    llParam = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        gravity = Gravity.CENTER
    }

    val tvText = TextView(this).apply {
        text = getString(R.string.please_wait)
        setTextColor(ContextCompat.getColor(this@showProgressDialog, R.color.black))
        textSize = 10f
        layoutParams = llParam
    }

    ll.addView(progressBar)
    ll.addView(tvText)

    val builder = AlertDialog.Builder(this).apply {
        setCancelable(true)
        setView(ll)
    }

    val dialog = builder.create().apply {
        show()
    }

    dialog.window?.let {
        WindowManager.LayoutParams().apply {
            copyFrom(dialog.window!!.attributes)
            width = LinearLayout.LayoutParams.WRAP_CONTENT
            height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window!!.attributes = this
        }
    }
    return dialog
}
