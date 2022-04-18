package com.example.youngr2.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.youngr2.api.ProductInfoApi
import com.example.youngr2.models.ProductListItemModel
import retrofit2.HttpException
import java.io.IOException

class ProductInfoPagingSource(
    private val service: ProductInfoApi,
    private val product: String,
) : PagingSource<Int, ProductListItemModel>() {

    private val tag = javaClass.simpleName

    companion object {
        private const val PRODUCT_INFO_STARTING_PAGE_INDEX = 1;
        const val NETWORK_PAGE_SIZE = 5
    }

    // 사용자가 scroll 할 떄 표시할 더 많은 데이터를 비동기식으로 가져오기 위해 호출하는 부분
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductListItemModel> {
        val position = params.key ?: PRODUCT_INFO_STARTING_PAGE_INDEX // params.key 는 첫 호출 시 null 임

        return try {
            val startIdx = ((position - 1) * NETWORK_PAGE_SIZE) + PRODUCT_INFO_STARTING_PAGE_INDEX

            val response = service.getProductIInfo(
                prdlstNm = product,
                pageNo = startIdx.toString(),
                numberOfRows = NETWORK_PAGE_SIZE.toString()
            )
            var dataList = response.body()?.list
            var error = response.errorBody().toString()
            Log.d(tag, "retrofit error : $error")

            val nextKey = if (dataList == null || dataList.isEmpty()) {
                dataList = listOf()
                null
            } else {
                position + 1
            }

            LoadResult.Page(
                data = dataList,
                prevKey = null,
                nextKey = nextKey
            )

        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, ProductListItemModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}