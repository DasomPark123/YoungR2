package com.example.youngr2.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.youngr2.models.NutrientModel
import com.example.youngr2.repositories.NutrientRepository

class NutrientViewModelFactory(private val nutrientRepository: NutrientRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(NutrientRepository::class.java).newInstance(nutrientRepository)
    }
}