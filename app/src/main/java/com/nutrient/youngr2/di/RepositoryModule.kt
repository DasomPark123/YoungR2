package com.nutrient.youngr2.di

import com.nutrient.youngr2.remote.api.BarcodeInfoApi
import com.nutrient.youngr2.remote.api.ProductInfoApi
import com.nutrient.youngr2.repositories.ProductInfoRepository
import com.nutrient.youngr2.repositories.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideProductRepository(productInfoApi : ProductInfoApi, barcodeInfoApi: BarcodeInfoApi) : ProductInfoRepository {
        return ProductRepositoryImpl(productInfoApi, barcodeInfoApi)
    }
}