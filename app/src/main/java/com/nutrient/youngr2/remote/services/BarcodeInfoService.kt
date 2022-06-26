package com.nutrient.youngr2.remote.services

import com.nutrient.youngr2.remote.api.BarcodeInfoApi

object BarcodeInfoService : BaseService() {
    private const val BARCODE_INFO_URL = "https://openapi.foodsafetykorea.go.kr"

    fun create() : BarcodeInfoApi = getClient(BARCODE_INFO_URL)!!.create(BarcodeInfoApi::class.java)
}