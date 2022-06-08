package com.nutrient.youngr2.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nutrient.youngr2.const.ServiceKeyConst
import com.nutrient.youngr2.remote.api.BarcodeInfoApi
import com.nutrient.youngr2.remote.api.ProductInfoApi
import com.nutrient.youngr2.remote.models.BarcodeInfoModel
import com.nutrient.youngr2.remote.models.BarcodeServiceModel
import com.nutrient.youngr2.remote.models.ParsedProductInfoModel
import com.nutrient.youngr2.repositories.paging.ProductInfoPagingSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class ProductInfoRepository(
    private val productClient: ProductInfoApi,
    private val barcodeClient: BarcodeInfoApi) {

    fun getProductInfo(product: String): Flow<PagingData<ParsedProductInfoModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = ProductInfoPagingSource.NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductInfoPagingSource(productClient, product) }
        ).flow
    }

    suspend fun getProductNameByBarcode(barcodeNo : String) : Response<BarcodeServiceModel> {
        return barcodeClient.getProductByBarcode(ServiceKeyConst.PRODUCT_BARCODE_KEY, "json", barcodeNo)
    }
}