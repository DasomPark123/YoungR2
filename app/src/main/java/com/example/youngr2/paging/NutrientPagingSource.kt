package com.example.youngr2.paging

import android.renderscript.Sampler
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.youngr2.api.NutrientApi
import com.example.youngr2.models.NutrientBodyModel
import com.example.youngr2.models.NutrientModel
import com.example.youngr2.models.NutrientRowModel
import retrofit2.HttpException
import java.io.IOException

class NutrientPagingSource(
    private val service: NutrientApi,
    private val product: String
) : PagingSource<Int, NutrientRowModel>() {

    private val tag = javaClass.simpleName

    private lateinit var tempData : NutrientModel

    companion object {
        private const val NUTRIENT_STARTING_PAGE_INDEX = 1
        const val NETWORK_PAGE_SIZE = 20
    }

    // 사용자가 scroll 할 떄 표시할 더 많은 데이터를 비동기식으로 가져오기 위해 호출하는 부분
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NutrientRowModel> {
        val position = params.key ?: NUTRIENT_STARTING_PAGE_INDEX // params.key 는 첫 호출 시 null 임

        return try {
            Log.d(tag, "position : $position")
            val startIdx = ((position - 1) * 20 ) + NUTRIENT_STARTING_PAGE_INDEX
            val endIdx = ((position - 1) * 20 ) + NETWORK_PAGE_SIZE
            Log.d(tag, "startIdx : " + startIdx + "endIdx : " + endIdx)

            val response = service.getProductInfo(product, startIdx, endIdx) // 쿼리 call 부분
            var repos = response.body() // 전달된 response
            Log.d(tag, "repos : " + (repos ?: "null"))
            val nextKey = if (repos?.service?.row == null) { // response 가 null 일 경우 요청 실패
                repos = tempData
                null
            } else {
                tempData = repos
                position + 1
            }
            Log.d(tag, "nextkey : " + (nextKey ?: "null"))
            // 로드 성공한 경우
            LoadResult.Page(
                data = repos.service.row,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception) // 오류 발생
        } catch (exception: HttpException) {
            return LoadResult.Error(exception) // 오류 발생
        }
    }

    //Todo : 1,4,5,6,7 형태로 position이 반환됨,, 확인 필요
    // 리스트가 새로고침될떄 호출됨. 가장 최근에 액세스한 인덱스인 anchorPosition 주변 데이터를 로드함
    override fun getRefreshKey(state: PagingState<Int, NutrientRowModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}