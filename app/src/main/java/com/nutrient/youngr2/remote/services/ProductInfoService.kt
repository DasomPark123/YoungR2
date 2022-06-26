package com.nutrient.youngr2.remote.services

import com.nutrient.youngr2.remote.api.ProductInfoApi

object ProductInfoService : BaseService() {
    private const val PRODUCT_INFO_URL = "http://apis.data.go.kr"

    fun create() : ProductInfoApi = getClient(PRODUCT_INFO_URL)!!.create(ProductInfoApi::class.java)
}