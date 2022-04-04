package com.example.youngr2.api

import com.example.youngr2.models.NutrientModel
import retrofit2.http.GET
import retrofit2.http.Path

interface NutrientApi {
    @GET("/api/I2790/json/{startIdx}/{endIdx}/DESC_KOR={product}")
    suspend fun getProductInfo(
        @Path("product") product : String,
        @Path("startIdx") startIdx : Int,
        @Path("endIdx") endIdx : Int
    ):retrofit2.Response<NutrientModel>
}