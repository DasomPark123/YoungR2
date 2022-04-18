package com.example.youngr2.application

import android.app.Application

class CustomApplication : Application() {

    companion object {
        lateinit var INSTANCE : CustomApplication
        const val EXTRA_PRODUCT : String = "EXTRA_PRODUCT"
        const val EXTRA_PRODUCT_DATA : String = "EXTRA_PRODUCT_DATA"

    }

    init {
        INSTANCE = this
    }
}