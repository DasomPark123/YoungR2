package com.example.youngr2

class NutrientConst {
    companion object {
        const val SUCCESS = "INFO-000"
        const val INVALID_AUTHENTICATION_KEY = "INFO-100"
        const val NO_DATA = "INFO-200"
        const val EXCEEDED_CALL = "INFO-300"
        const val NO_PERMISSION = "INFO-400"
        const val MISSING_REQUIRED_VALUES = "ERROR-301"
        const val SERVICE_NOT_FOUND = "ERROR-310"
        const val WRONG_START_INDEX = "ERROR-331"
        const val WRONG_END_INDEX = "ERROR-332"
        const val END_INDEX_LARGER_THAN_START_INDEX = "ERROR-334"
        const val DATA_REQUESTS_EXCEED_1000 = "ERROR-336"
        const val SERVER_ERROR = "ERROR-500"
        const val SQL_ERROR = "ERROR-601"
    }
}