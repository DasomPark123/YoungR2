package com.nutrient.youngr2.remote.models

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class ParsedProductListItemModel (
    var productId : String = "알수없음",
    var product : String = "알수없음",
    var seller : String = "알수없음",
    var imageUrl : String = "알수없음",
    var total_content : String = "알수없음",
    var calorie : String = "-",
    var carbohydrate : String = "-",
    var sugars : String = "-",
    var protein : String = "-",
    var fat : String = "-",
    var saturatedFat : String = "-",
    var transFat : String = "-",
    var cholesterol : String = "-",
    var salt : String = "-",
    var rawMtr : String = "-"
) : Parcelable
