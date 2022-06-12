package com.nutrient.youngr2.remote.models

data class ParsedProductInfoModel(
    val list : List<ParsedProductListItemModel> = listOf(),
    val totalCount : String = "",
    val pageNo : String = "",
    val resultCode : String = "",
    val resultMessage : String = "",
    val numOfRows : String = ""
)

