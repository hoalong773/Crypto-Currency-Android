package com.namnguyen.cryptocurrency.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.namnguyen.cryptocurrency.databinding.ItemCurrencyBinding
import com.namnguyen.cryptocurrency.domain.model.CryptoCurrencyModel

class CurrencyListAdapter : ListAdapter<CryptoCurrencyModel, CurrencyListHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListHolder {
        val binding = ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyListHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyListHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CryptoCurrencyModel>() {
            override fun areItemsTheSame(
                oldItem: CryptoCurrencyModel,
                newItem: CryptoCurrencyModel
            ): Boolean {
                return oldItem.buyPrice == newItem.buyPrice && oldItem.sellPrice == oldItem.sellPrice
            }

            override fun areContentsTheSame(
                oldItem: CryptoCurrencyModel,
                newItem: CryptoCurrencyModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
