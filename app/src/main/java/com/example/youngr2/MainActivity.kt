package com.example.youngr2

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.example.youngr2.application.NutrientApplication
import com.example.youngr2.databinding.ActivityMainBinding
import com.example.youngr2.utils.Utils
import com.google.android.material.snackbar.Snackbar

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val tag : String = javaClass.simpleName

    private lateinit var utils : Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        utils = Utils(this)
    }

    override fun beforeSetContentView() {
        super.beforeSetContentView()
        setTheme(R.style.Theme_YoungR2)
    }

    override fun initListener() {
        super.initListener()
        binding.apply {
            etSearch.setOnEditorActionListener(onEditorActionListener)
        }
    }

    private val onEditorActionListener =
        TextView.OnEditorActionListener { v, actionId, event ->
            var handled = false

            when(actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val product: String = binding.etSearch.text.toString()
                    if (product.isEmpty()) {
                        utils.hideKeyboard(v)
                        showSnackBar(binding.llMain, getString(R.string.request_input_text))
                    } else {
                        val intent = Intent(this@MainActivity, NutrientListActivity::class.java)
                        intent.putExtra(NutrientApplication.EXTRA_PRODUCT, product)
                        startActivity(intent)
                    }
                    handled = true
                }
            }
            handled
        }
}