package com.namnguyen.cryptocurrency.domain.usecase

import com.namnguyen.cryptocurrency.domain.model.CryptoCurrencyModel
import com.namnguyen.cryptocurrency.domain.repository.UserRepository
import com.namnguyen.cryptocurrency.domain.usecase.base.UseCase

class GetListCryptoUseCase constructor(
    private val userRepository: UserRepository
) : UseCase<List<CryptoCurrencyModel>, Any?>() {

    override suspend fun run(params: Any?): List<CryptoCurrencyModel> {
        return userRepository.getListCurrency(params)
    }

}