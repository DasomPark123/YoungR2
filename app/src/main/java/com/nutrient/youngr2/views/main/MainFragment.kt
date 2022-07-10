package com.nutrient.youngr2.views.main

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.nutrient.youngr2.R
import com.nutrient.youngr2.base.BaseFragment
import com.nutrient.youngr2.databinding.FragmentMainBinding
import com.nutrient.youngr2.utils.hideKeyboard
import com.nutrient.youngr2.utils.showSnackbar
import com.nutrient.youngr2.views.barcode.BarcodeActivity

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override fun init() {
        /* Action bar 초기화 */
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.hide()

        binding.apply {
            etSearch.setOnEditorActionListener(onEditorActionListener)
            llSearchFromList.setOnClickListener(onClickListener)
            llSearchByBarcode.setOnClickListener(onClickListener)
        }
    }

    /* Soft 키보드에서 돋보기 모양 아이콘 (검색) 을 클릭했을 때 동작하는 부분 */
    private val onEditorActionListener =
        TextView.OnEditorActionListener { v, actionId, event ->
            var handled = false

            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val product: String = binding.etSearch.text.toString()
                    if (product.isEmpty()) {   /* 식품 이름을 입력하지 않고 검색 버튼을 누른 경우 */
                        requireContext().hideKeyboard(v)
                        showSnackbar(binding.llMain, getString(R.string.request_input_text))
                    } else { /* 식품 이름으로 검색 */
                        /* Todo : fragment navigate 로 변경 예정.
                           debounds 로 delay 를 줘서 중복체크 방지 예정
                        */
                        navigateToSearchFragmentWithProductName(product)
                    }
                    handled = true
                }
            }
            handled
        }

    /* 검색창 하단의 전체리스트 검색/바코드로 검색 클릭 시에 대한 처리 */
    private val onClickListener =
        View.OnClickListener {
            when (it.id) {
                /*Todo : fragment navigate 로 변경 예정.
                    debounds 로 delay 를 줘서 중복체크 방지 예정
                 */
                R.id.ll_search_from_list -> navigateToSearchFragment()
                R.id.ll_search_by_barcode -> startScanBarcode()
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
                /* Todo : fragment navigate 로 변경 예정.
                    debounds 로 delay 를 줘서 중복체크 방지 예정
                */
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