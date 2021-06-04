package com.namnguyen.cryptocurrency.main.adapter

import androidx.recyclerview.widget.RecyclerView
import com.namnguyen.cryptocurrency.databinding.ItemCurrencyBinding
import com.namnguyen.cryptocurrency.domain.model.CryptoCurrencyModel

class CurrencyListHolder(private val binding: ItemCurrencyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(data: CryptoCurrencyModel) {
        binding.apply {
            currencyInfo = data
            executePendingBindings()
        }
    }
}