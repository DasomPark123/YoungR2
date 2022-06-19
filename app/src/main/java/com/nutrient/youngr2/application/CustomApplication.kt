package com.nutrient.youngr2.application

import android.app.Application

class CustomApplication : Application() {

    companion object {
        lateinit var INSTANCE : CustomApplication
        const val EXTRA_PRODUCT_DATA : String = "EXTRA_PRODUCT_DATA"
        const val EXTRA_ALL_DATA : String = "EXTRA_ALL_DATA"
        const val EXTRA_BARCODE_DATA : String = "EXTRA_BARCODE_DATA"
        const val EXTRA_PRODUCT_INFO : String = "EXTRA_PRODUCT_INFO"
    }

    init {
        INSTANCE = this
    }
}