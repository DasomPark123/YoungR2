package com.nutrient.youngr2.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nutrient.youngr2.remote.models.BarcodeServiceModel
import com.nutrient.youngr2.remote.models.ParsedProductInfoModel
import com.nutrient.youngr2.repositories.ProductInfoRepository
import com.nutrient.youngr2.utils.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class ProductInfoViewModel(private val productInfoRepository: ProductInfoRepository) : ViewModel() {

    private val tag = ProductInfoViewModel::class.java.simpleName

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

    fun getProductNameByBarcode(barcodeNo: String) : Flow<Result<BarcodeServiceModel>> {
        Log.d(tag, "barcodeNo : $barcodeNo")
        getProductNameJob?.isActive?.let { isActive ->
            if (isActive)
                getProductNameJob?.cancel()
        }

        //if(safeApiCall { productInfoRepository.getProductNameByBarcode(barcodeNo) } is Flow<Result<BarcodeServiceModel>>)
        return safeApiCall { productInfoRepository.getProductNameByBarcode(barcodeNo) } as Flow<Result<BarcodeServiceModel>>
    }

    /* 파라미터로 suspend 메소드를 받고 통신 성공/실패 여부에 따라 Result 값을 반환하는 함수 */
    private fun <T : Any> safeApiCall(call: suspend () -> Response<T>?) : Flow<Result<Any>>{
        return flow {
            try {
                val myResponse = call.invoke()

                if (myResponse!!.isSuccessful) {
                    emit(Result.Success(myResponse.body()!!))
                } else {
                    emit(Result.Error(myResponse.message() ?: "Something goes wrong"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Internet connection error"))
            }
        }
    }
}