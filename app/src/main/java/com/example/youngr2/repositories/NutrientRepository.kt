package com.example.youngr2.repositories

import com.example.youngr2.modules.NutrientService

class NutrientRepository {
    // TODO: 의존성 주입 필요
    private val nutrientClient = NutrientService.client

    suspend fun getProductInfo(product: String) = nutrientClient?.getProductInfo(product)
}