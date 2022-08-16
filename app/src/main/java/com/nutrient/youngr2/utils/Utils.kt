package com.nutrient.youngr2.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

const val DEBUG = false

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









