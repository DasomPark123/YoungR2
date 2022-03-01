package com.example.youngr2.models

import com.google.gson.annotations.SerializedName

data class NutrientBodyModel(
    @SerializedName("total_count")
    val total_count : Int,

    @SerializedName("row")
    val row : List<NutrientRowModel>,

    @SerializedName("RESULT")
    val result : NutrientResultModel
)
