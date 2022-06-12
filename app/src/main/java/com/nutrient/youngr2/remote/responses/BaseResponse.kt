package com.nutrient.youngr2.remote.responses

abstract class BaseResponse<M> {
    abstract fun mapper() : M
}