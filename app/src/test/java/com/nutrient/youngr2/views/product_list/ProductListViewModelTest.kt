package com.nutrient.youngr2.views.product_list

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.nutrient.youngr2.data.source.FakeProdcutListRepository
import com.nutrient.youngr2.remote.models.*
import com.nutrient.youngr2.remote.responses.ApiResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProductListViewModelTest {

    private lateinit var productListViewModel: ProductListViewModel
    private lateinit var productListRepository: FakeProdcutListRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        productListRepository = FakeProdcutListRepository()
        productListViewModel = ProductListViewModel(productListRepository)
    }

    @Test
    fun should_productNameIsNotNull_when_requestProductInfoByProductName() = runTest {
        //When
        val firstItem = productListViewModel.requestProductInfoByProductName("새우깡").first()
        //Then
    }

    @Test
    fun should_getSameDataAsTestData_when_requestProductInfoByProductReportNo() = runTest {

        //Given
        val item = ApiResult.Success(
            ParsedProductInfoModel(
                list = listOf(ParsedProductListItemModel(product = "새우깡")), totalCount = "1"
            )
        )

        //When
        val firstItem = productListViewModel.requestProductInfoByProductReportNo("1234").first()

        //Then
        assertEquals(item, firstItem)
    }

    @Test
    fun should_getSameDataAsTestData_when_requestAllProductInfo() = runTest {

    }

    @Test
    fun should_getSameDataAsTestData_when_requestProductInfoByBarcode() = runTest {

        //Given
        val item = ApiResult.Success(
            BarcodeServiceModel(
                BarcodeInfoModel(
                    total_count = "1", row = listOf(BarcodeListItemModel(barcodeNo = "1234"))
                )
            )
        )

        //When
        val firstItem = productListViewModel.requestProductInfoByBarcode(item.data.COO5.row[0].barcodeNo).first()

        //Then
        assertEquals(item, firstItem)
    }

}
