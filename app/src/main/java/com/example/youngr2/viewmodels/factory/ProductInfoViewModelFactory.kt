package com.example.youngr2.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.youngr2.repositories.ProductInfoRepository

class ProductInfoViewModelFactory(private val productInfoRepository: ProductInfoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ProductInfoRepository::class.java).newInstance(productInfoRepository)
    }
}