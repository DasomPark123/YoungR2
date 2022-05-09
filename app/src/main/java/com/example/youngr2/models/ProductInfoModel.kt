package com.example.youngr2.models

import com.google.gson.annotations.SerializedName

data class ProductInfoModel(

    @SerializedName("list")
    val list : List<ProductListItemModel> = listOf(),

    @SerializedName("totalCount")
    val totalCount : String = "",

    @SerializedName("pageNo")
    val pageNo : String = "",

    @SerializedName("resultCode")
    val resultCode : String = "",

    @SerializedName("resultMessage")
    val resultMessage : String = "",

    @SerializedName("numOfRows")
    val numOfRows : String = ""
)
