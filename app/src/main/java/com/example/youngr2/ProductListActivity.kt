package com.example.youngr2

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youngr2.adapter.LoadingStateAdapter
import com.example.youngr2.adapter.ProductInfoAdapter
import com.example.youngr2.application.CustomApplication
import com.example.youngr2.databinding.ActivityNutrientListBinding
import com.example.youngr2.factory.ProductInfoViewModelFactory
import com.example.youngr2.models.ParsedProductInfo
import com.example.youngr2.modules.ProductInfoService
import com.example.youngr2.repositories.ProductInfoRepository
import com.example.youngr2.viewmodels.ProductInfoViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductListActivity :
    BaseActivity<ActivityNutrientListBinding>(R.layout.activity_nutrient_list) {
    private val tag = javaClass.simpleName

    private lateinit var viewModel: ProductInfoViewModel

    //    private lateinit var viewModelFactory: NutrientViewModelFactory
    private lateinit var productInfoAdapter: ProductInfoAdapter
    private lateinit var viewModelFactory: ProductInfoViewModelFactory


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
            actionBar.setDisplayHomeAsUpEnabled(true)
            intent?.let {
                actionBar.title = intent.getStringExtra(CustomApplication.EXTRA_PRODUCT)
            }
        }

        // Init adapter
        productInfoAdapter =
            ProductInfoAdapter(clickListener = object : ProductInfoAdapter.OnProductClickListener {
                override fun onItemClick(data: ParsedProductInfo) {
                    Intent(this@ProductListActivity, NutrientInfoActivity::class.java).apply {
                        putExtra(CustomApplication.EXTRA_PRODUCT_DATA, data)
                    }.run { startActivity(this) }
                }
            }).apply {
                addLoadStateListener { combinedLoadStates ->
                    binding.apply {
                        /* 로딩 중 */
                        linearProgress.isVisible =
                            combinedLoadStates.source.refresh is LoadState.Loading
                        /* 로딩중 X, 에러 X */
                        rvNutrients.isVisible =
                            combinedLoadStates.source.refresh is LoadState.NotLoading
                        /* 에러 발생 시 */
                        linearError.isVisible = combinedLoadStates.source.refresh is LoadState.Error
                        tvReload.setOnClickListener { productInfoAdapter.retry() }

                        /* 로딩 X, 에러 X, 데이터가 없을 경우 */
                        if (combinedLoadStates.source.refresh is LoadState.NotLoading
                            && combinedLoadStates.append.endOfPaginationReached
                            && productInfoAdapter.itemCount < 1
                        ) {
                            rvNutrients.isVisible = false
                            tvNoData.isVisible = true
                        } else {
                            tvNoData.isVisible = false
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
    override fun initViewModel() {
        super.initViewModel()
        //viewModelFactory = NutrientViewModelFactory(NutrientRepository(NutrientService.client!!))
        viewModelFactory =
            ProductInfoViewModelFactory(ProductInfoRepository(ProductInfoService.client!!))
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProductInfoViewModel::class.java)
    }

    /* Invoked from onCreate() in BaseActivity */
    override fun afterOnCreate() {
        super.afterOnCreate()
        intent.getStringExtra(CustomApplication.EXTRA_PRODUCT)?.let { searchProductInfo(it) } ?: run { searchAllProductInfo() }
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

    private fun searchProductInfo(product: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.requestProductInfo(product).collectLatest {
                if (::productInfoAdapter.isInitialized) {
                    productInfoAdapter.submitData(it)
                }
            }
        }
    }

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
}