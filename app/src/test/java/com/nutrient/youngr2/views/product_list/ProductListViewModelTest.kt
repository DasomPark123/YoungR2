package com.nutrient.youngr2.views.product_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import com.nutrient.youngr2.data.source.FakeProductListRepository
import com.nutrient.youngr2.remote.models.*
import com.nutrient.youngr2.remote.responses.ApiResult
import com.nutrient.youngr2.util.MyDiffCallback
import com.nutrient.youngr2.util.NoopListCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProductListViewModelTest {

    private lateinit var productListViewModel: ProductListViewModel
    private lateinit var productListRepository: FakeProductListRepository

    private val testScope = TestScope()
    private val testDispatcher = StandardTestDispatcher(testScope.testScheduler)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        productListRepository = FakeProductListRepository()
        productListViewModel = ProductListViewModel(productListRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun should_productNameIsNotNull_when_requestProductInfoByProductName() = testScope.runTest {
        //Given
        val item = ParsedProductListItemModel(product = "새우깡", total_content = "1")
        val differ = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        //When
        val firstItem = productListViewModel.requestProductInfoByProductName("새우깡").first()
        differ.submitData(firstItem)

        //Then
        advanceUntilIdle()
        assertEquals(item,differ.snapshot().items[0])
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
        //Given
        val item = ParsedProductListItemModel(product = "", total_content = "1")
        val differ = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        //When
        val firstItem = productListViewModel.requestAllProductInfo().first()
        differ.submitData(firstItem)

        //Then
        advanceUntilIdle()
        assertEquals(item,differ.snapshot().items[0])
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
