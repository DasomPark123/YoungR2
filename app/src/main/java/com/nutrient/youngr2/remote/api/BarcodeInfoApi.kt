package com.nutrient.youngr2.remote.api

import com.nutrient.youngr2.remote.models.BarcodeInfoModel
import com.nutrient.youngr2.remote.models.BarcodeServiceModel
import retrofit2.http.GET
import retrofit2.http.Path

interface BarcodeInfoApi {
    @GET("/api/{serviceKey}/C005/{returnType}/1/1/BAR_CD={barcodeNo}")
    suspend fun getProductByBarcode(
        @Path("serviceKey") serviceKey: String,
        @Path("returnType") returnType: String,
        @Path("barcodeNo") barcodeNum: String
    ): retrofit2.Response<BarcodeServiceModel>
}