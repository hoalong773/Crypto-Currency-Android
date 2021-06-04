package com.namnguyen.cryptocurrency.data.service

import com.namnguyen.cryptocurrency.data.model.CryptoCurrencyResponse
import com.namnguyen.cryptocurrency.data.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/api/v3/price/all_prices_for_mobile")
    suspend fun getListCryptoCurrency(
        @Query("counter_currency") counterCurrency: String = Constants.CURRENCY
    ): CryptoCurrencyResponse
}