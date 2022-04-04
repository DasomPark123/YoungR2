package com.example.youngr2.modules

import com.example.youngr2.api.NutrientApi

object NutrientService : BaseService() {
    const val NUTRIENT_URL = "https://openapi.foodsafetykorea.go.kr"

    val client = getClient(NUTRIENT_URL)?.create(NutrientApi::class.java)
}