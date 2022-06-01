package com.nutrient.youngr2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nutrient.youngr2.remote.models.BarcodeInfoModel
import com.nutrient.youngr2.remote.models.ParsedProductInfoModel
import com.nutrient.youngr2.repositories.ProductInfoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response

class ProductInfoViewModel(private val productInfoRepository: ProductInfoRepository) : ViewModel() {
    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<ParsedProductInfoModel>>? = null

    private var getProductNameJob: Job? = null

    fun requestProductInfo(product: String): Flow<PagingData<ParsedProductInfoModel>> {
        val lastResult = currentSearchResult
        // 검색어가 이전과 같거나 첫번째 시도가 아닌 경우는 이전 결과를 리턴함
        if (product == currentQueryValue && lastResult != null) {
            return lastResult
        }

        currentQueryValue = product
        // cachedIn() 메서드 호출하여 Flow<PagingData<NutrientRowModel>> 의 내용을 캐싱함
        val newResult: Flow<PagingData<ParsedProductInfoModel>> =
            productInfoRepository.getProductInfo(product).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    fun requestAllProductInfo(): Flow<PagingData<ParsedProductInfoModel>> {
        return productInfoRepository.getProductInfo("").cachedIn(viewModelScope)
    }

    fun getProductNameByBarcode(barcodeNo: String): String? {
        if (getProductNameJob?.isActive!!) {
            getProductNameJob?.cancel()
        }

        var productName : String? = null
        getProductNameJob = viewModelScope.launch {
            productName = when(val result = safeApiCall { productInfoRepository.getProductNameByBarcode(barcodeNo) }){
                is Result.Success -> {
                    if(result.data.total_count == "0") {
                        null
                    } else {
                        result.data.row[0].productName
                    }
                }
                is Result.Error -> {
                    null
                }
            }
        }
        return productName
    }

    sealed class Result<out T : Any> {
        data class Success<out T : Any>(val data: T) : Result<T>()
        data class Error(val exception: String) : Result<Nothing>()
    }

    /* 파라미터로 suspend 메소드를 받고 통신 성공/실패 여부에 따라 Result 값을 반환하는 함수 */
    private suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>?): Result<T> {
        return try {
            val myResponse = call.invoke()

            if (myResponse!!.isSuccessful) {
                Result.Success(myResponse.body()!!)
            } else {
                Result.Error(myResponse.message() ?: "Something goes wrong")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Internet connection error")
        }
    }
}