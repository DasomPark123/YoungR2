package com.example.youngr2

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity<T : ViewDataBinding>(@LayoutRes private val layoutId: Int) :
    AppCompatActivity() {

    private val TAG: String = javaClass.simpleName

    protected lateinit var binding: T
    private lateinit var progressBar: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeSetContentView()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)

        initView()
        initViewModel()
        initListener()
        afterOnCreate()
    }

    protected open fun beforeSetContentView() {}
    protected open fun initView() {}
    protected open fun initViewModel() {}
    protected open fun initListener() {}
    protected open fun afterOnCreate() {}

    protected fun showProgressDialog() {
        val llPadding = 30

        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
        }

        val ll = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(llPadding, llPadding, llPadding, llPadding)
            gravity = Gravity.CENTER
            layoutParams = llParam
        }

        val progressBar = ProgressBar(this).apply {
            isIndeterminate = true
            setPadding(0, 0, llPadding, 0)
            layoutParams = llParam
        }

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
        }

        val tvText = TextView(this).apply {
            text = getString(R.string.please_wait)
            setTextColor(ContextCompat.getColor(this@BaseActivity, R.color.black))
            textSize = 10f
            layoutParams = llParam
        }

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(this).apply {
            setCancelable(true)
            setView(ll)
        }

        val dialog = builder.create().apply {
            show()
        }

        dialog.window?.let {
            WindowManager.LayoutParams().apply {
                copyFrom(dialog.window!!.attributes)
                width = LinearLayout.LayoutParams.WRAP_CONTENT
                height = LinearLayout.LayoutParams.WRAP_CONTENT
                dialog.window!!.attributes = this
            }
        }
        this.progressBar = dialog
    }
}