package com.nutrient.youngr2.remote

import com.nutrient.youngr2.remote.api.BarcodeInfoApi

object BarcodeInfoService : BaseService() {
    private const val BARCODE_INFO_URL = "http://openapi.foodsafetykorea.go.kr"

    val client = getClient(BARCODE_INFO_URL)?.create(BarcodeInfoApi::class.java)
}