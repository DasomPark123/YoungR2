package com.nutrient.youngr2.remote.models

import com.google.gson.annotations.SerializedName

data class BarcodeListItemModel(

    @SerializedName("CLSBIZ_DT")
    val closedBusinessData : String = "",

    @SerializedName("SITE_ADDR")
    val address : String = "",

    @SerializedName("PRDLST_REPORT_NO")
    val productReportNo : String = "",

    @SerializedName("PRMS_DT")
    val reportDate : String = "",

    @SerializedName("PRDLST_NM")
    val productName : String = "",

    @SerializedName("BAR_CD")
    val barcodeNo : String = "",

    @SerializedName("POG_DAYCNT")
    val expirationDate : String = "",

    @SerializedName("PRDLST_DCNM")
    val productType : String = "",

    @SerializedName("BSSH_NM")
    val companyName : String = "",

    @SerializedName("END_DT")
    val productionStopDate : String = "",

    @SerializedName("INDUTY_NM")
    val sectors : String = ""
)
