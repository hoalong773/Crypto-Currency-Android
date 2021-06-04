package com.namnguyen.cryptocurrency.data.util

import com.namnguyen.cryptocurrency.data.model.CurrencyResponse
import com.namnguyen.cryptocurrency.domain.model.CryptoCurrencyModel


fun CurrencyResponse.toCurrencyModel(): CryptoCurrencyModel = CryptoCurrencyModel(
    base = base.orEmpty(),
    counter = counter.orEmpty(),
    buyPrice = buyPrice ?: 0.0,
    sellPrice = sellPrice ?: 0.0,
    icon = icon.orEmpty(),
    name = name.orEmpty()
)

fun List<CurrencyResponse?>.toCurrencyModels(): List<CryptoCurrencyModel> =
    this.map { it?.toCurrencyModel() ?: CryptoCurrencyModel("", "", 0.0, 0.0, "", "") }
