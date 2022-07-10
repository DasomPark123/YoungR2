package com.nutrient.youngr2.utils

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
import com.nutrient.youngr2.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

const val CALORIE = "kcal"
const val CARBOHYDRATE = "탄수화물"
const val SUGAR = "당류"
const val PROTEIN = "단백질"
const val FAT = "지방"
const val SATURATED_FAT = "포화지방"
const val TRANS_FAT = "트랜스지방"
const val CHOLESTEROL = "콜레스테롤"
const val SALT = "나트륨"
const val UNKNOWN = "알수없음"

const val THROTTLE_DURATION = 300L

fun showSnackbar(view: View, msg: String) {
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).run { show() }
}









