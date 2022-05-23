package com.nutrient.youngr2.view

import android.os.Bundle
import com.journeyapps.barcodescanner.CaptureManager
import com.nutrient.youngr2.R
import com.nutrient.youngr2.databinding.ActivityBarcodeBinding
import com.nutrient.youngr2.view.base.BaseActivity

class BarcodeActivity : BaseActivity<ActivityBarcodeBinding>(R.layout.activity_barcode) {
    private lateinit var capture : CaptureManager
    private var switchFlashlightButtonCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


}