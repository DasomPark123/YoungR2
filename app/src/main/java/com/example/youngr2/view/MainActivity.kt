package com.example.youngr2.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.example.youngr2.R
import com.example.youngr2.application.CustomApplication
import com.example.youngr2.databinding.ActivityMainBinding
import com.example.youngr2.utils.hideKeyboard
import com.example.youngr2.utils.showSnackbar
import com.example.youngr2.view.base.BaseActivity
import com.example.youngr2.view.product_list.ProductListActivity

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val tag: String = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun beforeSetContentView() {
        super.beforeSetContentView()
        setTheme(R.style.Theme_YoungR2)
    }

    override fun initListener() {
        super.initListener()
        binding.apply {
            etSearch.setOnEditorActionListener(onEditorActionListener)
            llSearchFromList.setOnClickListener(onClickListener)
        }
    }

    /* Soft 키보드에서 돋보기 모양 아이콘 (검색) 을 클릭했을 때 동작하는 부분 */
    private val onEditorActionListener =
        TextView.OnEditorActionListener { v, actionId, event ->
            var handled = false

            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val product: String = binding.etSearch.text.toString()
                    if (product.isEmpty()) {
                        hideKeyboard(v)
                        showSnackbar(binding.llMain, getString(R.string.request_input_text))
                    } else {
                        Intent(this@MainActivity, ProductListActivity::class.java).apply {
                            putExtra(CustomApplication.EXTRA_PRODUCT, product)
                        }.run { startActivity(this) }
                    }
                    handled = true
                }
            }
            handled
        }

    private val onClickListener =
        View.OnClickListener {
            when (it.id) {
                R.id.ll_search_from_list ->  Intent(this@MainActivity, ProductListActivity::class.java).run { startActivity(this) }
            }
        }
}