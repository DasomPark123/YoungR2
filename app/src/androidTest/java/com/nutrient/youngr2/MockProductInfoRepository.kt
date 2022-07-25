package com.nutrient.youngr2

import androidx.paging.PagingData
import com.nutrient.youngr2.remote.models.BarcodeServiceModel
import com.nutrient.youngr2.remote.models.ParsedProductInfoModel
import com.nutrient.youngr2.remote.models.ParsedProductListItemModel
import com.nutrient.youngr2.remote.responses.ApiResult
import com.nutrient.youngr2.repositories.ProductInfoRepository
import kotlinx.coroutines.flow.Flow

class MockProductInfoRepository : ProductInfoRepository {
    override fun getProductInfoByProductName(product: String): Flow<PagingData<ParsedProductListItemModel>> {
        TODO("Not yet implemented")
    }

    override fun getProductInfoByProductReportNo(reportNo: String): Flow<ApiResult<ParsedProductInfoModel>> {
        TODO("Not yet implemented")
    }

    override fun getProductNameByBarcode(barcodeNo: String): Flow<ApiResult<BarcodeServiceModel>> {
        TODO("Not yet implemented")
    }
}