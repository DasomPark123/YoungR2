package com.nutrient.youngr2.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nutrient.youngr2.remote.api.BarcodeInfoApi
import com.nutrient.youngr2.remote.api.ProductInfoApi
import com.nutrient.youngr2.remote.models.BarcodeServiceModel
import com.nutrient.youngr2.remote.models.ParsedProductInfoModel
import com.nutrient.youngr2.remote.models.ParsedProductListItemModel
import com.nutrient.youngr2.remote.responses.ApiResult
import com.nutrient.youngr2.remote.responses.safeCallProductInfo
import com.nutrient.youngr2.remote.responses.safeCall
import com.nutrient.youngr2.repositories.paging.ProductInfoPagingSource
import kotlinx.coroutines.flow.Flow

interface ProductInfoRepository {
    fun getProductInfoByProductName(product: String) : Flow<PagingData<ParsedProductListItemModel>>
    fun getProductInfoByProductReportNo(reportNo : String) : Flow<ApiResult<ParsedProductInfoModel>>
    fun getProductNameByBarcode(barcodeNo : String) : Flow<ApiResult<BarcodeServiceModel>>
}