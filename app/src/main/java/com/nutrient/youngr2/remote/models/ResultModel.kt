package com.nutrient.youngr2.remote.models

import com.google.gson.annotations.SerializedName

data class ResultModel(

    @SerializedName("MSG")
    val msg : String = "",

    @SerializedName("CODE")
    val code : String = ""
)
