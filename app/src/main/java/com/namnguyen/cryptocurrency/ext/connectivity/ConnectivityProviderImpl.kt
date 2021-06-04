package com.namnguyen.cryptocurrency.ext.connectivity

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class ConnectivityProviderImpl(
    private val cm: ConnectivityManager
) : ConnectivityProviderBaseImpl() {

    private val networkCallback = ConnectivityCallback()

    override fun subscribe() {
        cm.registerDefaultNetworkCallback(networkCallback)
    }

    override fun unsubscribe() {
        cm.unregisterNetworkCallback(networkCallback)
    }

    override fun getNetworkState() = cm.getNetworkCapabilities(cm.activeNetwork).toNetworkState()

    private fun NetworkCapabilities?.toNetworkState() = this
        ?.takeIf { it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) }
        ?.run { NetworkState.Connected }
        ?: NetworkState.NotConnectedState

    private inner class ConnectivityCallback : NetworkCallback() {

        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            dispatchChange(capabilities.toNetworkState())
        }

        override fun onLost(network: Network) {
            dispatchChange(NetworkState.NotConnectedState)
        }
    }
}
