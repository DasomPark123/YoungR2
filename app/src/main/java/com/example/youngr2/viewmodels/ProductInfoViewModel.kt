package com.example.youngr2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.youngr2.remote.models.ParsedProductInfoModel
import com.example.youngr2.repositories.ProductInfoRepository
import kotlinx.coroutines.flow.Flow

class ProductInfoViewModel(private val productInfoRepository: ProductInfoRepository) : ViewModel() {
    private var currentQueryValue : String? = null
    private var currentSearchResult : Flow<PagingData<ParsedProductInfoModel>>? = null

    fun requestProductInfo(product : String) : Flow<PagingData<ParsedProductInfoModel>> {
        val lastResult = currentSearchResult
        // 검색어가 이전과 같거나 첫번째 시도가 아닌 경우는 이전 결과를 리턴함
        if(product == currentQueryValue && lastResult != null) {
            return lastResult
        }

        currentQueryValue = product
        // cachedIn() 메서드 호출하여 Flow<PagingData<NutrientRowModel>> 의 내용을 캐싱함
        val newResult : Flow<PagingData<ParsedProductInfoModel>> = productInfoRepository.getProductInfo(product).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    fun requestAllProductInfo(): Flow<PagingData<ParsedProductInfoModel>> {
        return productInfoRepository.getProductInfo("").cachedIn(viewModelScope)
    }
}