package com.nutrient.youngr2.views.main

import android.view.inputmethod.EditorInfo
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.nutrient.youngr2.R
import com.nutrient.youngr2.base.BaseFragment
import com.nutrient.youngr2.databinding.FragmentMainBinding
import com.nutrient.youngr2.utils.hideKeyboard
import com.nutrient.youngr2.utils.setClickEvent
import com.nutrient.youngr2.utils.setEditorEvent
import com.nutrient.youngr2.utils.showSnackbar
import com.nutrient.youngr2.views.barcode.BarcodeActivity

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override fun init() {
        /* Action bar 초기화 */
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.hide()

        binding.apply {
            /* Soft 키보드에서 돋보기 모양 아이콘 (검색) 을 클릭했을 때 동작하는 부분 */
            etSearch.setEditorEvent(lifecycleScope, EditorInfo.IME_ACTION_SEARCH) {
                val product: String = binding.etSearch.text.toString()
                if (product.isEmpty()) {   /* 식품 이름을 입력하지 않고 검색 버튼을 누른 경우 */
                    requireContext().hideKeyboard(etSearch)
                    showSnackbar(binding.llMain, getString(R.string.request_input_text))
                } else { /* 식품 이름으로 검색 */
                    navigateToSearchFragmentWithProductName(product)
                }
            }
            /* 리스트에서 찾기 클릭 */
            llSearchFromList.setClickEvent(lifecycleScope) {
                navigateToSearchFragment()
            }
            /* 바코드로 찾기 클릭 */
            llSearchByBarcode.setClickEvent(lifecycleScope) {
                startScanBarcode()
            }
        }
    }

    private fun navigateToSearchFragmentWithProductName(productName_: String) {
        val action = MainFragmentDirections.searchProductAction(productName = productName_)
        findNavController().navigate(action)
    }

    private fun navigateToSearchFragmentWithBarcodeNo(barcodeNo_: String) {
        val action = MainFragmentDirections.searchProductAction(barcodeNo = barcodeNo_)
        findNavController().navigate(action)
    }

    private fun navigateToSearchFragment() {
        val action = MainFragmentDirections.searchProductAction(productName = "", barcodeNo = "")
        findNavController().navigate(action)
    }

    /* 바코드 preview 띄움 */
    private fun startScanBarcode() {
        val options: ScanOptions =
            ScanOptions().setOrientationLocked(true).setCaptureActivity(BarcodeActivity::class.java)
        options.apply {
            setBarcodeImageEnabled(true)
            setBarcodeImageEnabled(true)
            setBeepEnabled(false)
            setPrompt(getString(R.string.scan_barcode))
            barcodeLauncher.launch(options)
        }
    }

    /* 바코드 preview 화면에서의 동작에 대한 결과 처리 */
    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> =
        registerForActivityResult(ScanContract()) { result ->
            result.contents?.let {
                navigateToSearchFragmentWithBarcodeNo(result.contents)
            } ?: run {
                val originalIntent = result.originalIntent
                originalIntent?.let {
                    if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        showSnackbar(binding.llMain, getString(R.string.camera_permission_required))
                    }
                } ?: run { showSnackbar(binding.llMain, getString(R.string.scan_canceled)) }
            }
        }
}