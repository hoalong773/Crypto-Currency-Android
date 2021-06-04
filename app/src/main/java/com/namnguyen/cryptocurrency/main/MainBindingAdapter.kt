package com.namnguyen.cryptocurrency.main

import android.text.SpannableStringBuilder
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Scale
import com.namnguyen.cryptocurrency.R
import com.namnguyen.cryptocurrency.domain.model.CryptoCurrencyModel

@BindingAdapter("app:nameCrypto")
fun AppCompatTextView.setNameCrypto(data: String?) {
    text = data.orEmpty()
}

@BindingAdapter(value = ["app:url"])
fun ImageView.loadImage(url: String?) {
    load(url) {
        crossfade(true)
        scale(Scale.FIT)
        placeholder(R.drawable.ic_dollar)
        error(R.drawable.ic_dollar)
    }
}

@BindingAdapter("app:nameBase")
fun AppCompatTextView.setBaseCrypto(data: CryptoCurrencyModel?) {
    data ?: return
    text =  SpannableStringBuilder().apply {
        append("${data.base} / ${data.counter}")
    }
}

@BindingAdapter("app:showUp")
fun AppCompatImageView.showUp(data: CryptoCurrencyModel?) {
    data?.let {
        isInvisible = it.buyPrice < it.sellPrice
    } ?: kotlin.run {
        isInvisible = true
    }
}

@BindingAdapter("app:showDown")
fun AppCompatImageView.showDown(data: CryptoCurrencyModel?) {
    data?.let {
        isInvisible = it.buyPrice >= it.sellPrice
    } ?: kotlin.run {
        isInvisible = true
    }
}

@BindingAdapter("app:priceBuy")
fun AppCompatTextView.setPriceBuy(data: CryptoCurrencyModel?) {
    data ?: return
    text = if (data.buyPrice > 0.0) data.buyPrice.toString() else ""
    setTextColor(
        ContextCompat.getColor(
            context,
            if (data.buyPrice < data.sellPrice) R.color.red_ribbon else R.color.apple
        )
    )
}

@BindingAdapter("app:priceSell")
fun AppCompatTextView.setPriceSell(data: CryptoCurrencyModel?) {
    data ?: return
    text = if (data.sellPrice > 0.0) data.sellPrice.toString() else ""
}