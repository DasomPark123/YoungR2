package com.nutrient.youngr2.remote.models

import com.google.gson.annotations.SerializedName

data class BarcodeInfoModel(
    @SerializedName("total_count")
    val total_count : String = "",

    @SerializedName("row")
    val row : List<BarcodeListItemModel> = listOf(),

    @SerializedName("RESULT")
    val result : ResultModel = ResultModel()
)
