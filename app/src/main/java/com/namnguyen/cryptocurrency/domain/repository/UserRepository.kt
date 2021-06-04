package com.namnguyen.cryptocurrency.domain.repository

import com.namnguyen.cryptocurrency.domain.model.CryptoCurrencyModel

interface UserRepository {
    suspend fun getListCurrency(params: Any?): List<CryptoCurrencyModel>
}