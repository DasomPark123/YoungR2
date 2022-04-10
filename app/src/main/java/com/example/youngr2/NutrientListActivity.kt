package com.example.youngr2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youngr2.adapter.LoadingStateAdapter
import com.example.youngr2.adapter.NutrientAdapter
import com.example.youngr2.application.NutrientApplication
import com.example.youngr2.databinding.ActivityNutrientListBinding
import com.example.youngr2.factory.NutrientViewModelFactory
import com.example.youngr2.models.NutrientResultModel
import com.example.youngr2.models.NutrientRowModel
import com.example.youngr2.modules.NutrientService
import com.example.youngr2.repositories.NutrientRepository
import com.example.youngr2.viewmodels.NutrientViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NutrientListActivity :
    BaseActivity<ActivityNutrientListBinding>(R.layout.activity_nutrient_list) {
    private val tag = javaClass.simpleName

    private lateinit var viewModel: NutrientViewModel
    private lateinit var viewModelFactory: NutrientViewModelFactory
    private lateinit var nutrientAdapter: NutrientAdapter

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
                actionBar.title = intent.getStringExtra(NutrientApplication.EXTRA_PRODUCT)
            }
        }

        // Init adapter
        nutrientAdapter = NutrientAdapter().apply {
            clickListener = object : NutrientAdapter.OnNutrientClickListener {
                override fun onItemClick(position: Int) {
                    Log.d(tag, "clicked item : $position")
                }
            }
            addLoadStateListener { combinedLoadStates ->
                binding.apply {
                    /* 로딩 중 */
                    linearProgress.isVisible = combinedLoadStates.source.refresh is LoadState.Loading
                    /* 로딩중 X, 에러 X */
                    rvNutrients.isVisible = combinedLoadStates.source.refresh is LoadState.NotLoading
                    /* 에러 발생 시 */
                    linearError.isVisible = combinedLoadStates.source.refresh is LoadState.Error
                    tvReload.setOnClickListener { nutrientAdapter.retry() }

                    /* 로딩 X, 에러 X, 데이터가 없을 경우 */
                    if(combinedLoadStates.source.refresh is LoadState.NotLoading
                        && combinedLoadStates.append.endOfPaginationReached
                        && nutrientAdapter.itemCount < 1) {
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
            layoutManager = LinearLayoutManager(this@NutrientListActivity)
            adapter = nutrientAdapter.withLoadStateHeaderAndFooter(
                header = LoadingStateAdapter { nutrientAdapter.retry() },
                footer = LoadingStateAdapter { nutrientAdapter.retry() }
            )
        }
    }

    /* Invoked from onCreate() in BaseActivity */
    override fun initViewModel() {
        super.initViewModel()
        viewModelFactory = NutrientViewModelFactory(NutrientRepository(NutrientService.client!!))
        viewModel = ViewModelProvider(this, viewModelFactory).get(NutrientViewModel::class.java)
    }

    /* Invoked from onCreate() in BaseActivity */
    override fun afterOnCreate() {
        super.afterOnCreate()
        intent.getStringExtra(NutrientApplication.EXTRA_PRODUCT)!!.run { searchNutrientInfo(this) }
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

    private fun searchNutrientInfo(product: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.requestNutrientInfo(product).collectLatest {
                if (::nutrientAdapter.isInitialized) {
                    nutrientAdapter.submitData(it)
                }
            }
        }
    }

    private fun updateResult(result: NutrientResultModel) {
        when (result.code) {
            NutrientConst.SUCCESS -> {
                Log.d(tag, result.msg)
            }
            NutrientConst.NO_DATA -> {
                binding.apply {
                    rvNutrients.visibility = View.GONE
                    tvNoData.visibility = View.VISIBLE
                }
            }
            else -> {
                showAlertDialog(getString(R.string.notification), result.msg)
            }
        }
    }
}