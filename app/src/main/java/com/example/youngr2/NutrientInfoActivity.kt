package com.example.youngr2

import android.os.Bundle
import android.view.MenuItem
import com.example.youngr2.application.NutrientApplication
import com.example.youngr2.databinding.ActivityNutrientInfoBinding

class NutrientInfoActivity : BaseActivity<ActivityNutrientInfoBinding>(R.layout.activity_nutrient_info) {

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
        actionBar?.let {  actionBar.setDisplayHomeAsUpEnabled(true)
            intent?.let { actionBar.title = intent.getStringExtra(NutrientApplication.EXTRA_PRODUCT)
            }
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