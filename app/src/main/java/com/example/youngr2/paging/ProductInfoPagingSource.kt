package com.example.youngr2.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.youngr2.NutrientConst
import com.example.youngr2.api.ProductInfoApi
import com.example.youngr2.models.ParsedProductInfo
import com.example.youngr2.models.ProductListItemModel
import com.example.youngr2.utils.Utils
import retrofit2.HttpException
import java.io.IOException
import java.lang.StringBuilder

class ProductInfoPagingSource(
    private val service: ProductInfoApi,
    private val product: String,
) : PagingSource<Int, ParsedProductInfo>() {

    private val tag = javaClass.simpleName

    companion object {
        private const val PRODUCT_INFO_STARTING_PAGE_INDEX = 1;
        const val NETWORK_PAGE_SIZE = 5
    }

    // 사용자가 scroll 할 떄 표시할 더 많은 데이터를 비동기식으로 가져오기 위해 호출하는 부분
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ParsedProductInfo> {
        val position = params.key ?: PRODUCT_INFO_STARTING_PAGE_INDEX // params.key 는 첫 호출 시 null 임

        return try {
            val startIdx = ((position - 1) * NETWORK_PAGE_SIZE) + PRODUCT_INFO_STARTING_PAGE_INDEX

            val response = service.getProductInfo(
                prdlstNm = product,
                pageNo = startIdx.toString(),
                numberOfRows = NETWORK_PAGE_SIZE.toString()
            )

            //Todo : 클래스 하나 만들어서 필요한 데이터만 파싱해서 넣고 리스트로 만들어서 뿌리기,,?
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


    override fun getRefreshKey(state: PagingState<Int, ParsedProductInfo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    private fun parseProductInfo(productInfoList: List<ProductListItemModel>?): List<ParsedProductInfo> {
        if (productInfoList == null || productInfoList.isEmpty()) {
            return listOf()
        }
        val parsedDataList: ArrayList<ParsedProductInfo> = ArrayList()
        for (productInfo in productInfoList) {
            val parsedProductInfo = ParsedProductInfo()
            parsedProductInfo.productId = productInfo.prdlstReportNo
            parsedProductInfo.product = productInfo.prdlstNm
            parsedProductInfo.seller =
                productInfo.manufacture.split(" ")[0].split("/")[0].split("_")[0]
            parsedProductInfo.imageUrl = productInfo.imgurl1
            if (productInfo.capacity.contains("(")) {
                parsedProductInfo.total_content =
                    productInfo.capacity.substring(0, productInfo.capacity.indexOf("("))
                parsedProductInfo.calorie = Utils.findWordBetweenBracket(productInfo.capacity)
            } else {
                parsedProductInfo.total_content = productInfo.capacity
            }

            if (productInfo.nutrient != null && productInfo.nutrient != NutrientConst.UNKNOWN) {
                val nutrientArr = productInfo.nutrient.split(",")
                for (i in nutrientArr.indices) {
                    if (nutrientArr[i].contains(NutrientConst.CALORIE)) {
                        if (!parsedProductInfo.calorie.contains(NutrientConst.CALORIE)) {
                            val calArr = nutrientArr[i].split(" ")
                            for (i in calArr.indices) {
                                if (calArr[i].contains("kcal")) {
                                    if (calArr[i].length == 4 && i != 0) { // ex) 250 kcal : 숫자와 kcal 사이에 띄어쓰기가 있는 경우
                                        parsedProductInfo.calorie =
                                            Utils.removeKorean(calArr[i - 1]) + Utils.removeKorean(calArr[i])
                                    } else if (calArr[i].contains("(") && calArr[i].contains(")")) { // ex) 열량(kcal)350 : kcal 이 괄호안에 있는 경우
                                        parsedProductInfo.calorie = calArr[i]
                                    } else {
                                        parsedProductInfo.calorie = Utils.removeKorean(calArr[i]).substring(0, Utils.removeKorean(calArr[i])
                                                    .indexOf(NutrientConst.CALORIE) + 4)
                                    }
                                    break
                                }
                            }
                        }
                    } else if (nutrientArr[i].contains(NutrientConst.CARBOHYDRATE)) {
                        val carArr = nutrientArr[i].split(" ")
                        for (word in carArr) {
                            if (word.contains("g")) {
                                parsedProductInfo.carbohydrate = Utils.removeKorean(word)
                                break
                            }
                        }
                    } else if (nutrientArr[i].contains(NutrientConst.SUGAR)) {
                        val sugarArr = nutrientArr[i].split(" ")
                        for (word in sugarArr) {
                            if (word.contains("g")) {
                                parsedProductInfo.sugars = Utils.removeKorean(word)
                                break
                            }
                        }
                    } else if (nutrientArr[i].contains(NutrientConst.PROTEIN)) {
                        val proteinArr = nutrientArr[i].split(" ")
                        for (word in proteinArr) {
                            if (word.contains("g")) {
                                parsedProductInfo.protein = Utils.removeKorean(word)
                                break
                            }
                        }
                    } else if (nutrientArr[i].contains(NutrientConst.FAT)) {
                        if (nutrientArr[i].contains(NutrientConst.SATURATED_FAT)) {
                            val satArr = nutrientArr[i].split(" ")
                            for (word in satArr) {
                                if (word.contains("g")) {
                                    parsedProductInfo.saturatedFat = Utils.removeKorean(word)
                                    break
                                }
                            }
                        } else if (nutrientArr[i].contains(NutrientConst.TRANS_FAT)) {
                            val transArr = nutrientArr[i].split(" ")
                            for (word in transArr) {
                                if (word.contains("g")) {
                                    parsedProductInfo.transFat = Utils.removeKorean(word)
                                    break
                                }
                            }
                        } else {
                            val fatArr = nutrientArr[i].split(" ")
                            for (word in fatArr) {
                                if (word.contains("g")) {
                                    parsedProductInfo.fat = Utils.removeKorean(word)
                                    break
                                }
                            }
                        }
                    } else if (nutrientArr[i].contains(NutrientConst.CHOLESTEROL)) {
                        val cholArr = nutrientArr[i].split(" ")
                        for (word in cholArr) {
                            if (word.contains("g")) {
                                parsedProductInfo.cholesterol = Utils.removeKorean(word)
                                break
                            }
                        }
                    } else if (nutrientArr[i].contains(NutrientConst.SALT)) {
                        val saltArr = nutrientArr[i].split(" ")
                        for (word in saltArr) {
                            if (word.contains("g")) {
                                parsedProductInfo.salt = Utils.removeKorean(word)
                                break
                            }
                        }
                    }
                }
            }
            parsedDataList.add(parsedProductInfo)
        }
        //Log.d(tag, "Paging data : $parsedDataList")
        return parsedDataList
    }
}