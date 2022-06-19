package com.nutrient.youngr2.remote.responses

import com.nutrient.youngr2.remote.models.ParsedProductInfoModel
import com.nutrient.youngr2.remote.models.ProductInfoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

sealed class ApiResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : ApiResult<T>()
    data class Error(val exception: String) : ApiResult<Nothing>()
}

enum class ApiState {
    LOADING, NO_DATA, SUCCESS, ERROR
}

/* 파라미터로 suspend 메소드를 받고 통신 성공/실패 여부에 따라 Result 값을 반환하는 함수 */
fun <T : Any> safeCall(call : suspend () -> Response<T>?): Flow<ApiResult<T>> {
    val tag = "safeApiCall"
    return flow {
        try {
            val myResponse = call.invoke()
            if (myResponse!!.isSuccessful) {
                emit(ApiResult.Success(myResponse.body()!!))
            } else {
                emit(ApiResult.Error(myResponse.message() ?: "Something goes wrong"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Internet connection error"))
        }
    }
}

fun safeCallProductInfo(call : suspend () -> Response<ProductInfoModel>): Flow<ApiResult<ParsedProductInfoModel>> {
    val tag = "safeApiCall"
    return flow {
        try {
            val myResponse = call.invoke()
            if (myResponse.isSuccessful) {
                emit(ApiResult.Success(myResponse.body()!!.mapper()))
            } else {
                emit(ApiResult.Error(myResponse.message() ?: "Something goes wrong"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Internet connection error"))
        }
    }
}
