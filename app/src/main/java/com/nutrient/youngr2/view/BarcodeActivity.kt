package com.nutrient.youngr2.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.KeyEvent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.CaptureManager
import com.nutrient.youngr2.R
import com.nutrient.youngr2.databinding.ActivityBarcodeBinding
import com.nutrient.youngr2.view.base.BaseActivity

class BarcodeActivity : BaseActivity<ActivityBarcodeBinding>(R.layout.activity_barcode) {
    private lateinit var capture : CaptureManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        capture = CaptureManager(this@BarcodeActivity, binding.barcodeView)
        capture.apply {
            initializeFromIntent(intent, savedInstanceState)
            setShowMissingCameraPermissionDialog(true)
            decode()
        }
    }

    override fun onResume() {
        super.onResume()
        capture.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return binding.barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }


}