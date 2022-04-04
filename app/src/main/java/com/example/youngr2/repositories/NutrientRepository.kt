package com.example.youngr2.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.youngr2.api.NutrientApi
import com.example.youngr2.models.NutrientRowModel
import com.example.youngr2.paging.NutrientPagingSource
import kotlinx.coroutines.flow.Flow

class NutrientRepository(private val nutrientClient : NutrientApi) {

    //suspend fun getProductInfo(product: String) = nutrientClient?.getProductInfo(product)

    fun getProductInfo(product : String) : Flow<PagingData<NutrientRowModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = NutrientPagingSource.NETWORK_PAGE_SIZE, //실제 서버에 요청하는 아이템 개수와 동일해야함
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NutrientPagingSource(nutrientClient, product)}
        ).flow
    }
}