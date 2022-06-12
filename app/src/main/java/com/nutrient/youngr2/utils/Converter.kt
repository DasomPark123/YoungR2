package com.nutrient.youngr2

import com.nutrient.youngr2.remote.models.ParsedProductListItemModel
import com.nutrient.youngr2.remote.models.ProductListItemModel
import com.nutrient.youngr2.utils.*
import java.util.regex.Pattern

fun removeKorean(string: String) : String {
    val regex = "[\\uAC00-\\uD7A3]".toRegex()
    return string.replace(regex, "")
}

fun findWordBetweenBracket(str : String) : String {
    var word : String? = ""
    val pattern = Pattern.compile("[(](.*?)[)]")
    val matcher = pattern.matcher(str)
    while (matcher.find()) {
        word = matcher.group(1)
        if (word == null) break
    }
    return word ?: ""
}

fun parseProductInfo(productInfoList: List<ProductListItemModel>?): List<ParsedProductListItemModel> {
    if (productInfoList == null || productInfoList.isEmpty()) {
        return listOf()
    }
    val parsedDataListModel: ArrayList<ParsedProductListItemModel> = ArrayList()
    for (productInfo in productInfoList) {
        val parsedProductInfo = ParsedProductListItemModel()
        productInfo.prdlstReportNo?.let {
            parsedProductInfo.productId = productInfo.prdlstReportNo
        }
        productInfo.prdlstNm?.let { parsedProductInfo.product = productInfo.prdlstNm }
        productInfo.manufacture?.let {
            parsedProductInfo.seller = productInfo.manufacture.split(" ", "/", "_", ",")[0]
        }
        productInfo.imgurl1?.let { parsedProductInfo.imageUrl = productInfo.imgurl1 }
        productInfo.capacity?.let {
            if (productInfo.capacity.contains("(")) {
                parsedProductInfo.total_content =
                    productInfo.capacity.substring(0, productInfo.capacity.indexOf("("))
                parsedProductInfo.calorie = findWordBetweenBracket(productInfo.capacity).replace(">","")
            } else {
                parsedProductInfo.total_content = productInfo.capacity.replace(">","")
            }
        } ?: continue

        if (productInfo.nutrient != null && productInfo.nutrient != UNKNOWN) {
            val nutrientArr = productInfo.nutrient.split(",")
            for (i in nutrientArr.indices) {
                when {
                    nutrientArr[i].contains(CALORIE) -> {
                        if (!parsedProductInfo.calorie.contains(CALORIE)) {
                            val calArr = nutrientArr[i].split(" ")
                            for (i in calArr.indices) {
                                if (calArr[i].contains("kcal")) {
                                    when {
                                        calArr[i].length == 4 && i != 0 -> { // ex) 250 kcal : 숫자와 kcal 사이에 띄어쓰기가 있는 경우
                                            parsedProductInfo.calorie =
                                                (removeKorean(calArr[i - 1]) + removeKorean(
                                                    calArr[i]
                                                )).replace(">","")
                                        }
                                        calArr[i].contains("(") && calArr[i].contains(")") -> { // ex) 열량(kcal)350 : kcal 이 괄호안에 있는 경우
                                            parsedProductInfo.calorie = calArr[i].replace(">","")
                                        }
                                        else -> {
                                            parsedProductInfo.calorie =
                                                removeKorean(calArr[i]).substring(
                                                    0,
                                                    removeKorean(calArr[i])
                                                        .indexOf(CALORIE) + 4
                                                ).replace(">","")
                                        }
                                    }
                                    break
                                }
                            }
                        }
                    }
                    nutrientArr[i].contains(CARBOHYDRATE) -> {
                        val carArr = nutrientArr[i].split(" ")
                        for (word in carArr) {
                            if (word.contains("g")) {
                                parsedProductInfo.carbohydrate = removeKorean(word).replace(">","")
                                break
                            }
                        }
                    }
                    nutrientArr[i].contains(SUGAR) -> {
                        val sugarArr = nutrientArr[i].split(" ")
                        for (word in sugarArr) {
                            if (word.contains("g")) {
                                parsedProductInfo.sugars = removeKorean(word).replace(">","")
                                break
                            }
                        }
                    }
                    nutrientArr[i].contains(SUGAR) -> {
                        val sugarArr = nutrientArr[i].split(" ")
                        for (word in sugarArr) {
                            if (word.contains("g")) {
                                parsedProductInfo.sugars = removeKorean(word).replace(">","")
                                break
                            }
                        }
                    }
                    nutrientArr[i].contains(PROTEIN) -> {
                        val proteinArr = nutrientArr[i].split(" ")
                        for (word in proteinArr) {
                            if (word.contains("g")) {
                                parsedProductInfo.protein = removeKorean(word).replace(">","")
                                break
                            }
                        }
                    }
                    nutrientArr[i].contains(FAT) -> {
                        when {
                            nutrientArr[i].contains(SATURATED_FAT) -> {
                                val satArr = nutrientArr[i].split(" ")
                                for (word in satArr) {
                                    if (word.contains("g")) {
                                        parsedProductInfo.saturatedFat =
                                            removeKorean(word).replace(">","")
                                        break
                                    }
                                }
                            }
                            nutrientArr[i].contains(TRANS_FAT) -> {
                                val transArr = nutrientArr[i].split(" ")
                                for (word in transArr) {
                                    if (word.contains("g")) {
                                        parsedProductInfo.transFat = removeKorean(word).replace(">","")
                                        break
                                    }
                                }
                            }
                            else -> {
                                val fatArr = nutrientArr[i].split(" ")
                                for (word in fatArr) {
                                    if (word.contains("g")) {
                                        parsedProductInfo.fat = removeKorean(word).replace(">","")
                                        break
                                    }
                                }
                            }
                        }
                    }
                    nutrientArr[i].contains(CARBOHYDRATE) -> {
                        val cholArr = nutrientArr[i].split(" ")
                        for (word in cholArr) {
                            if (word.contains("g")) {
                                parsedProductInfo.cholesterol = removeKorean(word).replace(">","")
                                break
                            }
                        }
                    }
                    nutrientArr[i].contains(SALT) -> {
                        val saltArr = nutrientArr[i].split(" ")
                        for (word in saltArr) {
                            if (word.contains("g")) {
                                parsedProductInfo.salt = removeKorean(word).replace(">","")
                                break
                            }
                        }
                    }
                }
            }
            parsedDataListModel.add(parsedProductInfo)
        }
    }
    //Log.d(tag, "Paging data : $parsedDataList")
    return parsedDataListModel
}