package com.nutrient.youngr2.repositories

import androidx.paging.PagingData
import com.nutrient.youngr2.remote.models.BarcodeServiceModel
import com.nutrient.youngr2.remote.models.ParsedProductInfoModel
import com.nutrient.youngr2.remote.models.ParsedProductListItemModel
import com.nutrient.youngr2.remote.responses.ApiResult
import kotlinx.coroutines.flow.Flow

interface ProductListRepository {
    fun getProductInfoByProductName(product: String): Flow<PagingData<ParsedProductListItemModel>>
    fun getProductInfoByProductReportNo(reportNo: String): Flow<ApiResult<ParsedProductInfoModel>>
    fun getProductNameByBarcode(barcodeNo: String): Flow<ApiResult<BarcodeServiceModel>>
}