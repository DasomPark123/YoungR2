package com.example.youngr2.application

import android.app.Application

class NutrientApplication : Application() {

    companion object {
        lateinit var INSTANCE : NutrientApplication
        const val EXTRA_PRODUCT : String = "EXTRA_PRODUCT"
    }

    init {
        INSTANCE = this
    }
}