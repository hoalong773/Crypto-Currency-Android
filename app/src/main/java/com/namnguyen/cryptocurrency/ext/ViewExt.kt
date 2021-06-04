package com.namnguyen.cryptocurrency.ext

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import java.text.NumberFormat
import java.util.*

fun Activity?.changeStatusBar(
    fullLayout: Boolean = false,
    lightBackground: Boolean = false,
    @ColorInt color: Int = Color.TRANSPARENT
) {
    val window = this?.window ?: return
    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE.or(View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        if (fullLayout) {
            decorView.systemUiVisibility = decorView.systemUiVisibility
                .or(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
        if (lightBackground) {
            decorView.systemUiVisibility = decorView.systemUiVisibility
                .or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        statusBarColor = color
    }
}

fun View?.hideSoftKeyboard() {
    this ?: return
    val inputMethod = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethod.hideSoftInputFromWindow(windowToken, 0)
}

fun View?.showSoftKeyboard() {
    this ?: return
    val inputMethod = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethod.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

private const val CURRENCY_MONEY = "USD"

fun Context.formatCurrencyMoney(pCurrency: String?, pValue: Double, setPrecision: Boolean): String {
    var currency = pCurrency
    var value = pValue
    val precision = 0
    val currencyNumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    currencyNumberFormat.let {
        it.run {
            this.isGroupingUsed = true
            this.minimumFractionDigits = if (setPrecision) precision else 0
            this.maximumFractionDigits = if (setPrecision) precision else 2.coerceAtLeast(precision)
        }
        if (currency.isNullOrBlank()) {
            currency = CURRENCY_MONEY
        }
        val curr = Currency.getInstance(currency)
        it.currency = curr
        value = roundOffFare(value)
        var result = it.format(value)
        result = result.replaceFirst("\\s".toRegex(), "")
        result = result.replace("BMD", "$")
        result = result.replace("TTD", "$")
        result = result.replace(curr.symbol, "")
//        return result + curr.symbol
        //todo format later
        return result
    }
}


private fun roundOffFare(pValue: Double): Double {
    var value = pValue
    val reminder = value % 1000
    if (reminder >= 300) {
        value = value - reminder + 1000
    } else {
        value -= reminder
    }
    return value
}