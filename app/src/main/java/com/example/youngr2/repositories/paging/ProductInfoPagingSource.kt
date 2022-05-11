package com.example.youngr2.repositories.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.youngr2.remote.api.ProductInfoApi
import com.example.youngr2.findWordBetweenBracket
import com.example.youngr2.remote.models.ParsedProductInfoModel
import com.example.youngr2.remote.models.ProductListItemModel
import com.example.youngr2.removeKorean
import com.example.youngr2.utils.*
import retrofit2.HttpException
import java.io.IOException

class ProductInfoPagingSource(
    private val service: ProductInfoApi,
    private val product: String,
) : PagingSource<Int, ParsedProductInfoModel>() {

    private val tag = javaClass.simpleName

    companion object {
        private const val PRODUCT_INFO_STARTING_PAGE_INDEX = 1;
        const val NETWORK_PAGE_SIZE = 5
    }

    // 사용자가 scroll 할 떄 표시할 더 많은 데이터를 비동기식으로 가져오기 위해 호출하는 부분
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ParsedProductInfoModel> {
        val position = params.key ?: PRODUCT_INFO_STARTING_PAGE_INDEX // params.key 는 첫 호출 시 null 임

        return try {
            val startIdx = ((position - 1) * NETWORK_PAGE_SIZE) + PRODUCT_INFO_STARTING_PAGE_INDEX

            val response = service.getProductInfo(
                prdlstNm = product,
                pageNo = startIdx.toString(),
                numberOfRows = NETWORK_PAGE_SIZE.toString()
            )

            val dataList = response.body()?.list
            Log.d(tag, "Paging data : $dataList")
            val error = response.errorBody().toString()
            Log.d(tag, "retrofit error : $error")

            val nextKey = if (dataList == null || dataList.isEmpty()) {
                null
            } else {
                position + 1
            }

            LoadResult.Page(
                data = parseProductInfo(dataList),
                prevKey = null,
                nextKey = nextKey
            )

        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ParsedProductInfoModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    private fun parseProductInfo(productInfoList: List<ProductListItemModel>?): List<ParsedProductInfoModel> {
        if (productInfoList == null || productInfoList.isEmpty()) {
            return listOf()
        }
        val parsedDataListModel: ArrayList<ParsedProductInfoModel> = ArrayList()
        for (productInfo in productInfoList) {
            val parsedProductInfo = ParsedProductInfoModel()
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
}