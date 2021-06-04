package com.namnguyen.cryptocurrency.domain.model

data class CryptoCurrencyModel(
    val base: String,
    val counter: String,
    val buyPrice: Double,
    val sellPrice: Double,
    val icon: String,
    val name: String
)