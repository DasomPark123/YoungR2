package com.nutrient.youngr2.view

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.nutrient.youngr2.R
import com.nutrient.youngr2.application.CustomApplication
import com.nutrient.youngr2.databinding.ActivityNutrientInfoBinding
import com.nutrient.youngr2.view.base.BaseActivity

class NutrientInfoActivity : BaseActivity<ActivityNutrientInfoBinding>(R.layout.activity_nutrient_info) {

    private val tag = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun beforeSetContentView() {
        super.beforeSetContentView()
        setTheme(R.style.Theme_SubTheme)
    }

    override fun initView() {
        super.initView()
//        val actionBar = supportActionBar
//        actionBar?.let {  actionBar.setDisplayHomeAsUpEnabled(true)
//            intent?.let { actionBar.title = intent.getStringExtra(NutrientApplication.EXTRA_PRODUCT)
//            }
//        }
    }

    override fun initViewModel() {
        super.initViewModel()
    }

    override fun afterOnCreate() {
        super.afterOnCreate()
        binding.apply {
            nutrient = intent.getParcelableExtra(CustomApplication.EXTRA_PRODUCT_DATA)
            Log.d(tag, "data : $nutrient")
            Glide.with(this@NutrientInfoActivity)
                .load(nutrient?.imageUrl)
                .error(R.drawable.ic_no_image)
                .override(800, 800)
                .into(ivProduct)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}