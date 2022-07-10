package com.nutrient.youngr2.views.product_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.onNavDestinationSelected
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.nutrient.youngr2.R
import com.nutrient.youngr2.base.BaseFragment
import com.nutrient.youngr2.databinding.FragmentProductListBinding
import com.nutrient.youngr2.remote.models.BarcodeListItemModel
import com.nutrient.youngr2.remote.models.ParsedProductListItemModel
import com.nutrient.youngr2.remote.responses.ApiResult
import com.nutrient.youngr2.remote.responses.ApiState
import com.nutrient.youngr2.views.product_list.adapter.LoadingStateAdapter
import com.nutrient.youngr2.views.product_list.adapter.ProductListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment :
    BaseFragment<FragmentProductListBinding>(R.layout.fragment_product_list) {

    private val viewModel: ProductListViewModel by viewModels()

    private lateinit var productListAdapter: ProductListAdapter

    private var searchJob: Job? = null
    private val safeArgs: ProductListFragmentArgs by navArgs()

    override fun init() {
        /* Action bar 초기화 */
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            show()
        }

        /* Adapter 초기화 */
        productListAdapter =
            ProductListAdapter(clickListener = object : ProductListAdapter.OnProductClickListener {
                override fun onItemClick(data: ParsedProductListItemModel) {
                    Log.d(tag, "data : $data")
                    navigateToProductInfoFragment(data)
                }
            }).apply {
                addLoadStateListener { combinedLoadStates ->
                    when (combinedLoadStates.source.refresh) {
                        is LoadState.Loading -> {
                            updateState(ApiState.LOADING)
                        }
                        is LoadState.NotLoading -> {
                            if (combinedLoadStates.append.endOfPaginationReached
                                && productListAdapter.itemCount < 1) {
                                updateState(ApiState.NO_DATA)
                            } else {
                                updateState(ApiState.SUCCESS)
                            }
                        }
                        is LoadState.Error -> {
                            updateState(ApiState.ERROR)
                            binding.tvReload.setOnClickListener { productListAdapter.retry() }
                        }
                    }
                }
            }

        /* Recyclerview 초기화 */
        binding.rvNutrients.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productListAdapter.withLoadStateHeaderAndFooter(
                header = LoadingStateAdapter { productListAdapter.retry() },
                footer = LoadingStateAdapter { productListAdapter.retry() }
            )
        }

        /* Product 이름 검색 */
        if (safeArgs.productName.isNotEmpty()) {
            Log.d(tag, "Search Product")
            actionBar?.title = safeArgs.productName
            searchProductInfoByProductName(safeArgs.productName)
        }
        /* Barcode 로 검색 */
        if (safeArgs.barcodeNo.isNotEmpty()) {
            Log.d(tag, "Search Barcode")
            actionBar?.title = ""
            searchProductInfoByBarcode(safeArgs.barcodeNo)
        }
        /* 전체 상품 검색 */
        if (safeArgs.productName.isEmpty() && safeArgs.barcodeNo.isEmpty()) {
            Log.d(tag, "Search all data")
            actionBar?.title = requireContext().getString(R.string.title_all_product)
            searchAllProductInfo()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> {
                (item.onNavDestinationSelected(findNavController())
                        || super.onOptionsItemSelected(item))
            }
        }
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

    private fun navigateToProductInfoFragment(data: ParsedProductListItemModel) {
        val action = ProductListFragmentDirections.nutrientInfoAction(productInfo = data)
        findNavController().navigate(action)
    }

    private fun navigateToProductInfoFragmentByBarcode(data: ParsedProductListItemModel) {
        val action = ProductListFragmentDirections.nutrientInfoActionByBarcode(productInfo = data)
        findNavController().navigate(action)
    }

    /* 상품이름으로 상품 정보 검색 후 리스트에 데이터 전달 */
    private fun searchProductInfoByProductName(product: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.requestProductInfoByProductName(product).collectLatest {
                if (::productListAdapter.isInitialized) {
                    productListAdapter.submitData(it)
                }
            }
        }
    }

    /* 모든 상품 조회 후 리스트로 데이터 전달 */
    private fun searchAllProductInfo() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.requestAllProductInfo().collectLatest {
                if (::productListAdapter.isInitialized) {
                    productListAdapter.submitData(it)
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
                                navigateToProductInfoFragmentByBarcode(result.data.list[0])
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