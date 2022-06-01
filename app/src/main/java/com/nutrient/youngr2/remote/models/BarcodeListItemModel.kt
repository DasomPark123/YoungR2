package com.nutrient.youngr2.remote.models

import com.google.gson.annotations.SerializedName

data class BarcodeListItemModel(

    @SerializedName("PRDLST_REPORT_NO")
    val productListReportNo : String = "",

    @SerializedName("HTRK_PRDLST_NM")
    val itemMainCategory : String = "",

    @SerializedName("LAST_UPDT_DTM")
    val lastUpdateDateTime : String = "",

    @SerializedName("HRNK_PRDLST_NM")
    val itemMiddleCategory : String = "",

    @SerializedName("BRCD_NO")
    val barcodeNo : String = "",

    @SerializedName("PRDLST_NM")
    val itemSubCategory : String = "",

    @SerializedName("PRDT_NM")
    val productName : String = "",

    @SerializedName("CMPNY_NM")
    val companyName : String = ""
)
