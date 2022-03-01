package com.example.youngr2

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youngr2.adapter.NutrientAdapter
import com.example.youngr2.application.NutrientApplication
import com.example.youngr2.databinding.ActivityNutrientListBinding
import com.example.youngr2.factory.NutrientViewModelFactory
import com.example.youngr2.models.NutrientResultModel
import com.example.youngr2.models.NutrientRowModel
import com.example.youngr2.repositories.NutrientRepository
import com.example.youngr2.viewmodels.NutrientViewModel

class NutrientListActivity :
    BaseActivity<ActivityNutrientListBinding>(R.layout.activity_nutrient_list) {
    private val tag = javaClass.simpleName

    private lateinit var viewModel: NutrientViewModel
    private lateinit var viewModelFactory: NutrientViewModelFactory
    private lateinit var nutrientAdapter: NutrientAdapter

    private lateinit var product: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun beforeSetContentView() {
        super.beforeSetContentView()
        setTheme(R.style.Theme_SubTheme)
    }

    override fun initView() {
        super.initView()
        val actionBar = supportActionBar
        actionBar?.let {
            actionBar!!.setDisplayHomeAsUpEnabled(true)
            intent?.let {
                actionBar.title = intent.getStringExtra(NutrientApplication.EXTRA_PRODUCT)
            }
        }
    }

    /* Invoked from onCreate() in BaseActivity */
    override fun initViewModel() {
        super.initViewModel()
        viewModelFactory = NutrientViewModelFactory(NutrientRepository())
        viewModel = ViewModelProvider(this, viewModelFactory).get(NutrientViewModel::class.java)
        /* Nutrient data observing */
        viewModel.nutrientRepositories.observe(this) {
            updateRepositories(it)
        }
        /* Result observing */
        viewModel.nutrientResult.observe(this) {
            progressBar.dismiss()
            checkResult(it)
        }
        /* Error observing */
        viewModel.nutrientError.observe(this) {
            progressBar.dismiss()
            showSnackBar(binding.llNutrientList, it)
        }
    }

    /* Invoked from onCreate() in BaseActivity */
    override fun afterOnCreate() {
        super.afterOnCreate()
        product = intent.getStringExtra(NutrientApplication.EXTRA_PRODUCT)!!
        requestProductList(product)
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

    private fun updateRepositories(repos: List<NutrientRowModel>) {
        if (::nutrientAdapter.isInitialized) {
            nutrientAdapter.update(repos)
        } else {
            nutrientAdapter = NutrientAdapter(repos).apply {
                clickListener = object : NutrientAdapter.OnNutrientClickListener {
                    override fun onItemClick(position: Int) {
                        nutrientAdapter.getItem(position).run {
                            //TODO : Nutrient Activity 로 이동
                        }
                    }
                }
            }
            binding.rvNutrients.run {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@NutrientListActivity)
                adapter = nutrientAdapter
            }
        }
    }

    private fun requestProductList(product: String) {
        showProgressDialog()
        viewModel.requestNutrientRepositories(product)
    }

    private fun checkResult(result: NutrientResultModel) {
        when(result.code) {
            "INFO-000" -> {
                Log.d(tag,result.msg)
            }
            "INFO-200" -> {
                //TODO: recyclerview visibility gone 처리 및 해당 데이터가 없다는 화면 띄우기
            }
            else -> {
                showSnackBar(binding.llNutrientList, result.msg)
            }
        }
    }
}