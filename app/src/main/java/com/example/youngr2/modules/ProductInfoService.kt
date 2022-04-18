package com.example.youngr2.modules

import com.example.youngr2.api.ProductInfoApi

object ProductInfoService : BaseService() {
    const val PRODCUT_INFO_URL = "http://apis.data.go.kr"

    val client = getClient(PRODCUT_INFO_URL)?.create(ProductInfoApi::class.java)
}