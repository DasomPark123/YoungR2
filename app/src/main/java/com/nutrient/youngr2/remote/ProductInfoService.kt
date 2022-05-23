package com.nutrient.youngr2.remote

import com.nutrient.youngr2.remote.api.ProductInfoApi

object ProductInfoService : BaseService() {
    private const val PRODUCT_INFO_URL = "http://apis.data.go.kr"

    val client = getClient(PRODUCT_INFO_URL)?.create(ProductInfoApi::class.java)
}