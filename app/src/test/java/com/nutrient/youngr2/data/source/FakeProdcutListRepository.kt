package com.nutrient.youngr2.data.source

import androidx.paging.PagingData
import com.nutrient.youngr2.remote.models.*
import com.nutrient.youngr2.remote.responses.ApiResult
import com.nutrient.youngr2.repositories.ProductListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeProdcutListRepository : ProductListRepository {
    override fun getProductInfoByProductName(product: String): Flow<PagingData<ParsedProductListItemModel>> = flow {

    }

    override fun getProductInfoByProductReportNo(reportNo: String): Flow<ApiResult<ParsedProductInfoModel>> = flow {
        val item = ApiResult.Success(
            ParsedProductInfoModel(
                list = listOf(ParsedProductListItemModel(product = "새우깡")), totalCount = "1"
            )
        )
        emit(item)
    }

    override fun getProductNameByBarcode(barcodeNo: String): Flow<ApiResult<BarcodeServiceModel>> = flow {
        val item = ApiResult.Success(
            BarcodeServiceModel(
                BarcodeInfoModel(
                    total_count = "1", row = listOf(BarcodeListItemModel(barcodeNo = "1234"))
                )
            )
        )
        emit(item)
    }
}