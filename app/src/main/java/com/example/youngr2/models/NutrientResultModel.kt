package com.example.youngr2.models

import com.google.gson.annotations.SerializedName

data class NutrientResultModel(
    @SerializedName("MSG")
    val msg : String,

    @SerializedName("CODE")
    val code : String
)
