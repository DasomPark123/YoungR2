package com.example.youngr2.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.youngr2.NutrientConst
import com.example.youngr2.api.ProductInfoApi
import com.example.youngr2.models.ParsedProductInfo
import com.example.youngr2.models.ProductListItemModel
import retrofit2.HttpException
import java.io.IOException

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

            if (productInfo.nutrient != null && productInfo.nutrient != NutrientConst.UNKNOWN) {

                // 총 내용량
                val itemArr = productInfo.nutrient.split(", ")
                parsedProductInfo.total_content = itemArr[0]

                // 이 외의 영양소
                val nutrientArr = productInfo.nutrient.split(" ")
                for (i in nutrientArr.indices) {
                    if (nutrientArr[i].contains(NutrientConst.CALORIE)) {
                        Log.d(tag, "칼로리 : " + nutrientArr[i])
                        if (nutrientArr[i].split(",")[0].length > 4 && nutrientArr[i].split(",")[0].substring(
                                nutrientArr[i].length - 3,
                                nutrientArr[i].length - 1
                            ) == NutrientConst.CALORIE
                        ) {
                            parsedProductInfo.calorie = nutrientArr[i]
                        }
                    } else if (nutrientArr[i].contains(NutrientConst.CARBOHYDRATE)) {
                        parsedProductInfo.carbohydrate = nutrientArr[i + 1].split(",")[0]
                    } else if (nutrientArr[i].contains(NutrientConst.SUGAR)) {
                        parsedProductInfo.sugars = nutrientArr[i + 1].split(",")[0]
                    } else if (nutrientArr[i].contains(NutrientConst.PROTEIN)) {
                        parsedProductInfo.protein = nutrientArr[i + 1].split(",")[0]
                    } else if (nutrientArr[i].contains(NutrientConst.FAT)) {
                        if (nutrientArr[i].contains(NutrientConst.SATURATED_FAT)) {
                            parsedProductInfo.saturatedFat = nutrientArr[i + 1].split(",")[0]
                        } else if (nutrientArr[i].contains(NutrientConst.TRANS_FAT)) {
                            parsedProductInfo.transFat = nutrientArr[i + 1].split(",")[0]
                        } else {
                            parsedProductInfo.fat = nutrientArr[i + 1].split(",")[0]
                        }
                    } else if (nutrientArr[i].contains(NutrientConst.CHOLESTEROL)) {
                        parsedProductInfo.cholesterol = nutrientArr[i + 1].split(",")[0]
                    } else if (nutrientArr[i].contains(NutrientConst.SALT)) {
                        parsedProductInfo.salt = nutrientArr[i + 1].split(",")[0]
                    }
                }
            }
            parsedDataList.add(parsedProductInfo)
        }
        Log.d(tag, "Paging data : $parsedDataList")
        return parsedDataList
    }
}