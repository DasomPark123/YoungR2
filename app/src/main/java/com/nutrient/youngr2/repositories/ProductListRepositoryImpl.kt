package com.nutrient.youngr2.repositories

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nutrient.youngr2.remote.api.BarcodeInfoApi
import com.nutrient.youngr2.remote.api.ProductInfoApi
import com.nutrient.youngr2.remote.models.BarcodeServiceModel
import com.nutrient.youngr2.remote.models.ParsedProductInfoModel
import com.nutrient.youngr2.remote.models.ParsedProductListItemModel
import com.nutrient.youngr2.remote.responses.ApiResult
import com.nutrient.youngr2.remote.responses.safeCall
import com.nutrient.youngr2.remote.responses.safeCallProductInfo
import com.nutrient.youngr2.repositories.paging.ProductInfoPagingSource
import com.nutrient.youngr2.utils.DEBUG
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductListRepositoryImpl @Inject constructor(
    private val productInfoApi: ProductInfoApi,
    private val barcodeInfoApi: BarcodeInfoApi
) :
    ProductListRepository {
    private val tag = javaClass.simpleName

    override fun getProductInfoByProductName(product: String): Flow<PagingData<ParsedProductListItemModel>> {
        if(DEBUG) Log.d(tag, "getProductInfoByProductName : $product")
        return Pager(
            config = PagingConfig(
                pageSize = ProductInfoPagingSource.NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductInfoPagingSource(productInfoApi, product) }
        ).flow
    }

    override fun getProductInfoByProductReportNo(reportNo: String): Flow<ApiResult<ParsedProductInfoModel>> =
        safeCallProductInfo {
            productInfoApi.getProductInfoByProductReportNo(prdlstReportNo = reportNo)
        }

    override fun getProductNameByBarcode(barcodeNo: String): Flow<ApiResult<BarcodeServiceModel>> =
        safeCall {
            barcodeInfoApi.getProductByBarcode(barcodeNum = barcodeNo)
        }
}