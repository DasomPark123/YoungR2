package com.example.youngr2.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.youngr2.models.NutrientResultModel
import com.example.youngr2.models.NutrientRowModel
import com.example.youngr2.repositories.NutrientRepository
import retrofit2.Response
import kotlinx.coroutines.flow.Flow

class NutrientViewModel(private val nutrientRepository: NutrientRepository) : ViewModel() {
    private val tag = javaClass.simpleName
    private val _nutrientRepositories = MutableLiveData<List<NutrientRowModel>>()
    private val _nutrientResult = MutableLiveData<NutrientResultModel>()
    private val _nutrientError = MutableLiveData<String>()
    val nutrientRepositories = _nutrientRepositories
    val nutrientResult = _nutrientResult
    val nutrientError = _nutrientError

    private var currentQueryValue : String ? = null
    private var currentSearchResult : Flow<PagingData<NutrientRowModel>>? = null

//    fun requestNutrientRepositories(product: String) {
//        CoroutineScope(Dispatchers.IO).launch {
//            when(val result = safeApiCall { nutrientRepository.getProductInfo(product) }) {
//                is Result.Success -> {
//                    _nutrientResult.postValue(result.data.service.result)
//                    if(result.data.service.result.code == NutrientConst.SUCCESS) {
//                        _nutrientRepositories.postValue(result.data.service.row)
//                    }
//                }
//                is Result.Error -> {
//                    _nutrientError.postValue(result.exception)
//                }
//            }
//        }
//    }

    fun requestNutrientInfo(product : String) : Flow<PagingData<NutrientRowModel>> {
        val lastResult = currentSearchResult
        // 검색어가 이전과 같거나 첫번째 시도가 아닌 경우는 이전 결과를 리턴함
        if (product == currentQueryValue && lastResult != null) {
            return lastResult
        }

        currentQueryValue = product
        // cachedIn() 메서드 호출하여 Flow<PagingData<NutrientRowModel>> 의 내용을 캐싱함
        val newResult : Flow<PagingData<NutrientRowModel>> = nutrientRepository.getProductInfo(product).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
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