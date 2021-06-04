package com.namnguyen.cryptocurrency.data.repository

import com.namnguyen.cryptocurrency.data.service.ApiService
import com.namnguyen.cryptocurrency.data.util.Constants
import com.namnguyen.cryptocurrency.data.util.toCurrencyModels
import com.namnguyen.cryptocurrency.domain.model.CryptoCurrencyModel
import com.namnguyen.cryptocurrency.domain.repository.UserRepository

class UserRepositoryImp(private val apiService: ApiService) : UserRepository {

    override suspend fun getListCurrency(params: Any?): List<CryptoCurrencyModel> {
        return apiService.getListCryptoCurrency(
            (params ?: Constants.CURRENCY) as String
        ).data?.filterNotNull()?.toCurrencyModels()
            ?: emptyList()
    }
}