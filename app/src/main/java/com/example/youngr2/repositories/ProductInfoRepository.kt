package com.example.youngr2.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.youngr2.remote.api.ProductInfoApi
import com.example.youngr2.remote.models.ParsedProductInfoModel
import com.example.youngr2.repositories.paging.ProductInfoPagingSource
import kotlinx.coroutines.flow.Flow

class ProductInfoRepository(private val productClient: ProductInfoApi) {

    fun getProductInfo(product: String): Flow<PagingData<ParsedProductInfoModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = ProductInfoPagingSource.NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductInfoPagingSource(productClient, product) }
        ).flow
    }
}