package com.nutrient.youngr2.di

import com.nutrient.youngr2.remote.api.BarcodeInfoApi
import com.nutrient.youngr2.remote.api.ProductInfoApi
import com.nutrient.youngr2.remote.services.BarcodeInfoService
import com.nutrient.youngr2.remote.services.ProductInfoService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideProductInfoService() : ProductInfoApi {
        return ProductInfoService.create()
    }

    @Singleton
    @Provides
    fun provideBarcodeInfoService() : BarcodeInfoApi {
        return BarcodeInfoService.create()
    }
}