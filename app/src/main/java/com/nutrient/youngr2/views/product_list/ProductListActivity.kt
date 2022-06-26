package com.nutrient.youngr2.views.product_list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.nutrient.youngr2.R
import com.nutrient.youngr2.views.product_list.adapter.LoadingStateAdapter
import com.nutrient.youngr2.views.product_list.adapter.ProductInfoAdapter
import com.nutrient.youngr2.application.CustomApplication
import com.nutrient.youngr2.databinding.ActivityNutrientListBinding
import com.nutrient.youngr2.remote.models.BarcodeListItemModel
import com.nutrient.youngr2.remote.models.ParsedProductListItemModel
import com.nutrient.youngr2.remote.responses.ApiResult
import com.nutrient.youngr2.remote.responses.ApiState
import com.nutrient.youngr2.base.BaseActivity
import com.nutrient.youngr2.views.nutrient_info.NutrientInfoActivity
import com.nutrient.youngr2.viewmodels.ProductInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListActivity :
    BaseActivity<ActivityNutrientListBinding>(R.layout.activity_nutrient_list) {
    private val tag = javaClass.simpleName

    private val viewModel : ProductInfoViewModel by viewModels()

    private lateinit var productInfoAdapter: ProductInfoAdapter

    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun beforeSetContentView() {
        super.beforeSetContentView()
        setTheme(R.style.Theme_SubTheme)
    }

    override fun initView() {
        super.initView()
        // Init action bar
        val actionBar = supportActionBar
        actionBar?.let {
            intent?.let {
                val productName = intent.getStringExtra(CustomApplication.EXTRA_PRODUCT_DATA)
                if (productName == null || productName == "") {
                    actionBar.title = getString(R.string.app_name)
                } else {
                    actionBar.title = productName
                    actionBar.setDisplayHomeAsUpEnabled(true)
                }
            }
        }

        // Init adapter
        productInfoAdapter =
            ProductInfoAdapter(clickListener = object : ProductInfoAdapter.OnProductClickListener {
                override fun onItemClick(data: ParsedProductListItemModel) {
                    Intent(this@ProductListActivity, NutrientInfoActivity::class.java).apply {
                        putExtra(CustomApplication.EXTRA_PRODUCT_INFO, data)
                    }.run { startActivity(this) }
                }
            }).apply {
                addLoadStateListener { combinedLoadStates ->
                    when (combinedLoadStates.source.refresh) {
                        is LoadState.Loading -> {
                            updateState(ApiState.LOADING)
                        }
                        is LoadState.NotLoading -> {
                            if (combinedLoadStates.append.endOfPaginationReached
                                && productInfoAdapter.itemCount < 1) {
                                updateState(ApiState.NO_DATA)
                            } else {
                                updateState(ApiState.SUCCESS)
                            }
                        }
                        is LoadState.Error -> {
                            updateState(ApiState.ERROR)
                            binding.tvReload.setOnClickListener { productInfoAdapter.retry() }
                        }
                    }
                }
            }

        binding.rvNutrients.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ProductListActivity)
            adapter = productInfoAdapter.withLoadStateHeaderAndFooter(
                header = LoadingStateAdapter { productInfoAdapter.retry() },
                footer = LoadingStateAdapter { productInfoAdapter.retry() }
            )
        }
    }

    /* Invoked from onCreate() in BaseActivity */
    override fun afterOnCreate() {
        super.afterOnCreate()
        /* Product 이름 검색 */
        intent.getStringExtra(CustomApplication.EXTRA_PRODUCT_DATA)
            ?.let { productName ->
                Log.d(tag, "Search Product")
                searchProductInfoByProductName(productName)
            }
        /* Barcode 로 검색 */
        intent.getStringExtra(CustomApplication.EXTRA_BARCODE_DATA)
            ?.let { barcodeNo ->
                Log.d(tag, "Search Barcode")
                searchProductInfoByBarcode(barcodeNo)
            }
        /* 전체 상품 검색 */
        intent.getStringExtra(CustomApplication.EXTRA_ALL_DATA)
            ?.let {
                Log.d(tag, "Search all data")
                searchAllProductInfo()
            }

        intent.apply {
            removeExtra(CustomApplication.EXTRA_PRODUCT_DATA)
            removeExtra(CustomApplication.EXTRA_ALL_DATA)
            removeExtra(CustomApplication.EXTRA_BARCODE_DATA)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateState(apiState: ApiState) {
        binding.apply {
            when (apiState) {
                ApiState.LOADING -> {
                    linearProgress.isVisible = true
                }
                ApiState.NO_DATA -> {
                    linearProgress.isVisible = false
                    tvNoData.isVisible = true
                }
                ApiState.SUCCESS -> {
                    linearProgress.isVisible = false
                    rvNutrients.isVisible = true
                }
                ApiState.ERROR -> {
                    linearProgress.isVisible = false
                    linearError.isVisible = true
                }
            }
        }
    }

    /* 상품이름으로 상품 정보 검색 후 리스트에 데이터 전달 */
    private fun searchProductInfoByProductName(product: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.requestProductInfoByProductName(product).collectLatest {
                if (::productInfoAdapter.isInitialized) {
                    productInfoAdapter.submitData(it)
                }
            }
        }
    }

    /* 모든 상품 조회 후 리스트로 데이터 전달 */
    private fun searchAllProductInfo() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.requestAllProductInfo().collectLatest {
                if (::productInfoAdapter.isInitialized) {
                    productInfoAdapter.submitData(it)
                }
            }
        }
    }

    /* 바코드로 상품 정보 조회 */
    private fun searchProductInfoByBarcode(barcodeNo: String) {
        val successCode = "INFO-000"
        searchJob?.cancel()
        updateState(ApiState.LOADING)
        searchJob = lifecycleScope.launch {
            viewModel.requestProductInfoByBarcode(barcodeNo).collectLatest { result ->
                when (result) {
                    is ApiResult.Success -> {
                        if (result.data.COO5.total_count == "0") {
                            /* 바코드와 일치하는 상품이 존재하지 않음 */
                            updateState(ApiState.NO_DATA)
                            Log.d(tag, "result no data : $result")
                        } else {
                            /* 바코드로 상품 검색 성공 및 일치하는 상품 존재 */
                            Log.d(tag, "result success : $result")
                            searchProductInfoByProductReportNo(result.data.COO5.row[0])
                        }
                    }
                    is ApiResult.Error -> {
                        Log.d(tag, "result error : ${result.exception}")
                        updateState(ApiState.ERROR)
                    }
                }
            }
        }
    }

    /* ReportNo로 상품검색 */
    private fun searchProductInfoByProductReportNo(barcodeInfo: BarcodeListItemModel) {
        val successCode = "OK"
        searchJob?.cancel()
        updateState(ApiState.LOADING)
        searchJob = lifecycleScope.launch {
            viewModel.requestProductInfoByProductReportNo(barcodeInfo.productReportNo)
                .collectLatest { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            if (result.data.totalCount == "0") {
                                /* ReportNo와 일치하는 상품이 존재하지 않음. 상품이름으로 한번 더 검색 */
                                searchProductInfoByProductName(barcodeInfo.productName)
                            } else {
                                /* ReportNo로 상품 검색 성공 및 일치하는 상품 존재 */
                                Log.d(tag, "result success : $result")
                                Intent(this@ProductListActivity,
                                    NutrientInfoActivity::class.java).apply {
                                    putExtra(CustomApplication.EXTRA_PRODUCT_INFO,
                                        result.data.list[0])
                                }.run {
                                    startActivity(this)
                                    finish()
                                }
                            }
                        }
                        is ApiResult.Error -> {
                            Log.d(tag, "result success : $result")
                            updateState(ApiState.ERROR)
                        }
                    }
                }
        }
    }
}