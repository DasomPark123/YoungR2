package com.example.youngr2.interfaces

import com.example.youngr2.models.NutrientModel
import com.example.youngr2.models.NutrientRowModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NutrientApi {
    @GET("/api/tokenId/I2790/json/1/5/DESC_KOR={product}")
    suspend fun getProductInfo(
        @Path("product") product : String
    ):retrofit2.Response<NutrientModel>
}