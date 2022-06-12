package com.nutrient.youngr2.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.client.android.Intents
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.nutrient.youngr2.R
import com.nutrient.youngr2.application.CustomApplication
import com.nutrient.youngr2.databinding.ActivityMainBinding
import com.nutrient.youngr2.utils.hideKeyboard
import com.nutrient.youngr2.utils.showSnackbar
import com.nutrient.youngr2.view.base.BaseActivity
import com.nutrient.youngr2.view.productlist.ProductListActivity

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
            llSearchByBarcode.setOnClickListener(onClickListener)
        }
    }

    private fun startScanBarcode() {
        val options : ScanOptions = ScanOptions().setOrientationLocked(true).setCaptureActivity(BarcodeActivity::class.java)
        options.apply {
            setBarcodeImageEnabled(true)
            setBarcodeImageEnabled(true)
            setBeepEnabled(false)
            setPrompt(getString(R.string.scan_barcode))
            barcodeLauncher.launch(options)
        }
    }

    private val barcodeLauncher : ActivityResultLauncher<ScanOptions> = registerForActivityResult(ScanContract()) { result ->
        result.contents?.let {
            Intent(this@MainActivity, ProductListActivity::class.java).apply {
                putExtra(CustomApplication.EXTRA_BARCODE_DATA, it)
            }.run { startActivity(this) }
            //showSnackbar(binding.llMain, "상품 바코드 : $it")
        } ?: run {
            val originalIntent = result.originalIntent
            originalIntent?.let {
                if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                    showSnackbar(binding.llMain, getString(R.string.camera_permission_required))
                }
            } ?: run { showSnackbar(binding.llMain, getString(R.string.scan_canceled))}
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
                R.id.ll_search_by_barcode -> startScanBarcode()
            }
        }
}