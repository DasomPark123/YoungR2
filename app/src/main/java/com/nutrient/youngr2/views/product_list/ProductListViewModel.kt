package com.nutrient.youngr2.views.product_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nutrient.youngr2.remote.models.BarcodeServiceModel
import com.nutrient.youngr2.remote.models.ParsedProductInfoModel
import com.nutrient.youngr2.remote.models.ParsedProductListItemModel
import com.nutrient.youngr2.repositories.ProductInfoRepository
import com.nutrient.youngr2.remote.responses.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(private val repository: ProductInfoRepository) : ViewModel() {

    private val tag = ProductListViewModel::class.java.simpleName

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<ParsedProductListItemModel>>? = null

    fun requestProductInfoByProductName(product: String): Flow<PagingData<ParsedProductListItemModel>> {
        val lastResult = currentSearchResult
        // 검색어가 이전과 같거나 첫번째 시도가 아닌 경우는 이전 결과를 리턴함
        if (product == currentQueryValue && lastResult != null) {
            return lastResult
        }

        currentQueryValue = product
        // cachedIn() 메서드 호출하여 Flow<PagingData<NutrientRowModel>> 의 내용을 캐싱함
        val newResult: Flow<PagingData<ParsedProductListItemModel>> =
            repository.getProductInfoByProductName(product).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    fun requestProductInfoByProductReportNo(productReportNo : String) : Flow<ApiResult<ParsedProductInfoModel>> =
        repository.getProductInfoByProductReportNo(productReportNo)

    fun requestAllProductInfo(): Flow<PagingData<ParsedProductListItemModel>> =
        repository.getProductInfoByProductName("").cachedIn(viewModelScope)

    fun requestProductInfoByBarcode(barcodeNo: String) : Flow<ApiResult<BarcodeServiceModel>> =
        repository.getProductNameByBarcode(barcodeNo)
}