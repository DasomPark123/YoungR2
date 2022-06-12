package com.nutrient.youngr2.remote.models

import com.google.gson.annotations.SerializedName

data class BarcodeServiceModel(
    @SerializedName("C005")
    val COO5 : BarcodeInfoModel = BarcodeInfoModel()
)
