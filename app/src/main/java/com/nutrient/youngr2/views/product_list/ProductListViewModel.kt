package com.nutrient.youngr2.views.product_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nutrient.youngr2.remote.models.BarcodeServiceModel
import com.nutrient.youngr2.remote.models.ParsedProductInfoModel
import com.nutrient.youngr2.remote.models.ParsedProductListItemModel
import com.nutrient.youngr2.remote.responses.ApiResult
import com.nutrient.youngr2.repositories.ProductListRepository
import com.nutrient.youngr2.utils.DEBUG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(private val repository: ProductListRepository) : ViewModel() {

    private val tag = ProductListViewModel::class.java.simpleName

    private var currentProductName : String? = null
    private var currentProductList : Flow<PagingData<ParsedProductListItemModel>>? = null

    private var currentProductReportNo : String? = null
    private var currentProductInfo : Flow<ApiResult<ParsedProductInfoModel>>? = null

    private var currentAllProductList : Flow<PagingData<ParsedProductListItemModel>>? = null

    private var currentBarcodeNo : String? = null
    private var currentBarcodeInfo : Flow<ApiResult<BarcodeServiceModel>>? = null

    fun requestProductInfoByProductName(product: String): Flow<PagingData<ParsedProductListItemModel>> {
        if(DEBUG) Log.d(tag,"requestProductInfoByProductName")
        val lastResult = currentProductList
        // 검색어가 이전과 같거나 첫번째 시도가 아닌 경우는 이전 결과를 리턴함
        if (product == currentProductName && lastResult != null) {
            if(DEBUG) Log.d(tag,"재호출")
            return lastResult
        }

        currentProductName = product
        // cachedIn() 메서드 호출하여 Flow<PagingData<NutrientRowModel>> 의 내용을 캐싱함
        val newResult = repository.getProductInfoByProductName(product).cachedIn(viewModelScope)
        currentProductList = newResult
        return newResult
    }

    fun requestProductInfoByProductReportNo(productReportNo : String) : Flow<ApiResult<ParsedProductInfoModel>>{
        if(DEBUG) Log.d(tag,"requestProductInfoByProductReportNo")
        val lastResult = currentProductInfo
        if(productReportNo == currentProductReportNo && lastResult != null) {
            if(DEBUG) Log.d(tag,"재호출")
            return lastResult
        }

        currentProductReportNo = productReportNo
        val newResult = repository.getProductInfoByProductReportNo(productReportNo)
        currentProductInfo = newResult
        return newResult
    }

    fun requestAllProductInfo(): Flow<PagingData<ParsedProductListItemModel>> {
        if(DEBUG) Log.d(tag,"requestAllProductInfo")
        val lastResult = currentAllProductList
        if(lastResult != null) {
            if(DEBUG) Log.d(tag,"재호출")
            return lastResult
        }

        val newResult = repository.getProductInfoByProductName("").cachedIn(viewModelScope)
        currentAllProductList = newResult
        return newResult
    }

    fun requestProductInfoByBarcode(barcodeNo: String) : Flow<ApiResult<BarcodeServiceModel>> {
        if(DEBUG) Log.d(tag,"requestProductInfoByBarcode")
        val lastResult = currentBarcodeInfo
        if(barcodeNo == currentBarcodeNo && lastResult != null) {
            if(DEBUG) Log.d(tag,"재호출")
            return lastResult
        }

        currentBarcodeNo = barcodeNo
        val newResult = repository.getProductNameByBarcode(barcodeNo)
        currentBarcodeInfo = newResult
        return newResult
    }
}