package com.example.youngr2.remote.api

import com.example.youngr2.const.ServiceKeyConst
import com.example.youngr2.remote.models.ProductInfoModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductInfoApi {
    @GET("/B553748/CertImgListService/getCertImgListService")
    suspend fun getProductInfo(
        @Query("ServiceKey") serviceKey : String = ServiceKeyConst.PRODUCT_INFO_KEY_DECODING,
        @Query("prdlstNm") prdlstNm : String,
        @Query("pageNo") pageNo : String = "1",
        @Query("numberOfRows") numberOfRows : String = "1",
        @Query("returnType") returnType : String = "json"
    ):retrofit2.Response<ProductInfoModel>
}