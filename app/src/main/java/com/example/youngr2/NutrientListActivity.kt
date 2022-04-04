package com.example.youngr2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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

    private lateinit var product: String

    private lateinit var dataList : List<NutrientRowModel>

    private var searchJob : Job? = null

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
        }
        binding.rvNutrients.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@NutrientListActivity)
            adapter = nutrientAdapter
        }
    }

    /* Invoked from onCreate() in BaseActivity */
    override fun initViewModel() {
        super.initViewModel()
        viewModelFactory = NutrientViewModelFactory(NutrientRepository(NutrientService.client!!))
        viewModel = ViewModelProvider(this, viewModelFactory).get(NutrientViewModel::class.java)
//        /* Nutrient data observing */
//        viewModel.nutrientRepositories.observe(this) {
//            updateRepositories(it)
//            dataList = it
//        }
//        /* Result observing */
//        viewModel.nutrientResult.observe(this) {
//            progressBar.dismiss()
//            updateResult(it)
//        }
//        /* Error observing */
//        viewModel.nutrientError.observe(this) {
//            progressBar.dismiss()
//            showSnackBar(binding.llNutrientList, it)
//        }
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
                if(::nutrientAdapter.isInitialized) {
                    nutrientAdapter.submitData(it)
                }
            }
        }
    }

//    private fun updateRepositories(repos: List<NutrientRowModel>) {
//        if (::nutrientAdapter.isInitialized) {
//            nutrientAdapter.update(repos)
//        } else {
//            nutrientAdapter = NutrientAdapter(repos).apply {
//                clickListener = object : NutrientAdapter.OnNutrientClickListener {
//                    override fun onItemClick(position: Int) {
//                        nutrientAdapter.getItem(position).run {
//                            Intent(this@NutrientListActivity, NutrientInfoActivity::class.java).apply {
//                                putExtra(NutrientApplication.EXTRA_PRODUCT_DATA, dataList[position])
//                            }.run { startActivity(this) }
//                        }
//                    }
//                }
//            }
//            binding.rvNutrients.run {
//                setHasFixedSize(true)
//                layoutManager = LinearLayoutManager(this@NutrientListActivity)
//                adapter = nutrientAdapter
//            }
//        }
//    }
//
//    private fun requestProductList(product: String) {
//        showProgressDialog()
//        viewModel.requestNutrientRepositories(product)
//    }

    private fun updateResult(result: NutrientResultModel) {
        when(result.code) {
            NutrientConst.SUCCESS -> {
                Log.d(tag,result.msg)
            }
            NutrientConst.NO_DATA -> {
                binding.apply {
                    rvNutrients.visibility = View.GONE
                    tvNoData.visibility= View.VISIBLE
                }
            }
            else -> {
                showAlertDialog(getString(R.string.notification), result.msg)
            }
        }
    }
}