package com.namnguyen.cryptocurrency.ext.connectivity

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.os.Build

interface ConnectivityStateListener {
    fun onConnectivityStateChange(state: NetworkState)
}

@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
sealed class NetworkState {
    object NotConnectedState : NetworkState()
    object Connected : NetworkState()
}

interface ConnectivityProvider {

    fun addListener(listener: ConnectivityStateListener)
    fun removeListener(listener: ConnectivityStateListener)

    fun getNetworkState(): NetworkState

    companion object {
        fun createProvider(context: Context): ConnectivityProvider {
            val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ConnectivityProviderImpl(cm)
            } else {
                ConnectivityProviderLegacyImpl(context, cm)
            }
        }
    }
}
