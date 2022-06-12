package com.nutrient.youngr2.remote.models

import com.google.gson.annotations.SerializedName
import com.nutrient.youngr2.parseProductInfo
import com.nutrient.youngr2.remote.responses.BaseResponse

data class ProductInfoModel(

    @SerializedName("list")
    val list: List<ProductListItemModel> = listOf(),

    @SerializedName("totalCount")
    val totalCount: String = "",

    @SerializedName("pageNo")
    val pageNo: String = "",

    @SerializedName("resultCode")
    val resultCode: String = "",

    @SerializedName("resultMessage")
    val resultMessage: String = "",

    @SerializedName("numOfRows")
    val numOfRows: String = ""
) : BaseResponse<ParsedProductInfoModel>() {
    override fun mapper(): ParsedProductInfoModel {
        return ParsedProductInfoModel(
            list = parseProductInfo(list),
            totalCount = totalCount,
            pageNo = pageNo,
            resultCode = resultCode,
            resultMessage = resultMessage,
            numOfRows = numOfRows
        )
    }
}
