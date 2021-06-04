package com.namnguyen.cryptocurrency.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namnguyen.cryptocurrency.domain.model.ApiError
import com.namnguyen.cryptocurrency.domain.model.CryptoCurrencyModel
import com.namnguyen.cryptocurrency.domain.usecase.GetListCryptoUseCase
import com.namnguyen.cryptocurrency.domain.usecase.base.UseCaseResponse
import com.namnguyen.cryptocurrency.ext.SingleLiveData
import kotlinx.coroutines.cancel
import java.io.IOException

class MainViewModel constructor(private val getListCryptoUseCase: GetListCryptoUseCase) : ViewModel() {

    private val mListCryptoCurrencyLive = MutableLiveData<List<CryptoCurrencyModel>>()
    val listCryptoCurrencyLive = mListCryptoCurrencyLive
    var listCryptoCurrency: List<CryptoCurrencyModel>
        get() = mListCryptoCurrencyLive.value ?: emptyList()
        set(value) {
            mListCryptoCurrencyLive.postValue(value)
        }

    private val mListCryptoCurrencyFilterLive = MutableLiveData<List<CryptoCurrencyModel>>()
    val listCryptoCurrencyFilterLive = mListCryptoCurrencyFilterLive
    private var listCryptoCurrencyFilter: List<CryptoCurrencyModel>
        get() = mListCryptoCurrencyFilterLive.value ?: emptyList()
        set(value) {
            mListCryptoCurrencyFilterLive.postValue(value)
        }

    val isOnLineLive = SingleLiveData<Boolean>()
    var isOnLine: Boolean
        get() = isOnLineLive.value == true
        set(value) {
            isOnLineLive.postValue(value)
        }

    val showProgressbar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    init {
        getListCryptoCurrency()
        isOnLineLive.value = checkPingGoogle()
    }

    private fun checkPingGoogle(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            val exitValue = ipProcess.waitFor()
            return exitValue == 0
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return false
    }

    fun getListCryptoCurrency() {
        showProgressbar.value = true
        getListCryptoUseCase.invoke(
            viewModelScope, null,
            object : UseCaseResponse<List<CryptoCurrencyModel>> {
                override fun onSuccess(result: List<CryptoCurrencyModel>) {
                    Log.i(TAG, "result: $result")
                    listCryptoCurrency = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.getErrorMessage()
                    showProgressbar.value = false
                }
            },
        )
    }

    fun filterCurrencyByName(query: String?) {
        query?.let {
            listCryptoCurrencyFilter = listCryptoCurrency.filter { item -> item.name.orEmpty().contains(it, ignoreCase = true)}
        } ?: kotlin.run {
            listCryptoCurrencyFilter = listCryptoCurrency
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }

    companion object {
        private val TAG = MainViewModel::class.java.name
    }

}