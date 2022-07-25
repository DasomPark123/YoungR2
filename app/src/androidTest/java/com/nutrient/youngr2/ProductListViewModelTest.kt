package com.nutrient.youngr2

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nutrient.youngr2.repositories.ProductInfoRepository
import com.nutrient.youngr2.views.product_list.ProductListViewModel

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class ProductListViewModelTest {

    @Mock
    lateinit var repository: MockProductInfoRepository

    private lateinit var viewModel : ProductListViewModel

    @Before
    fun setup() {

    }

    @Test
    fun useAppContext() {

    }
}