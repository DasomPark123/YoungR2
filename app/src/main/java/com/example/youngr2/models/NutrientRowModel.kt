package com.example.youngr2.models

import com.google.gson.annotations.SerializedName

data class NutrientRowModel(
    @SerializedName("NUM")
    val num : String,

    @SerializedName("FOOD_CD")
    val food_code : String,

    @SerializedName("SAMPLING_REGION_NAME")
    val region : String,

    @SerializedName("SAMPLING_MONTH_NAME")
    val month : String,

    @SerializedName("SAMPLING_REGION_CD")
    val region_code : String,

    @SerializedName("SAMPLING_MONTH_CD")
    val month_code : String,

    @SerializedName("GROUP_NAME")
    val group : String,

    @SerializedName("DESC_KOR")
    val product : String,

    @SerializedName("RESEARCH_YEAR")
    val research_year : String,

    @SerializedName("MAKER_NAME")
    val maker : String,

    @SerializedName("SUB_REF_NAME")
    val data_source : String,

    @SerializedName("SERVING_SIZE")
    val total_content : String,

    @SerializedName("NUTR_CONT1")
    val calorie : String,

    @SerializedName("NUTR_CONT2")
    val carbohydrate : String,

    @SerializedName("NUTR_CONT3")
    val protein : String,

    @SerializedName("NUTR_CONT4")
    val fat : String,

    @SerializedName("NUTR_CONT5")
    val sugars : String,

    @SerializedName("NUTR_CONT6")
    val salt : String,

    @SerializedName("NUTR_CONT7")
    val cholesterol : String,

    @SerializedName("NUTR_CONT8")
    val saturated_fatty_acids : String,

    @SerializedName("NUTR_CONT9")
    val trans_fat : String
)
