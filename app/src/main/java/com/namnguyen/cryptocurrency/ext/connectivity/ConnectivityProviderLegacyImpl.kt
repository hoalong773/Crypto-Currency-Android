package com.namnguyen.cryptocurrency.ext.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.net.ConnectivityManager.EXTRA_NETWORK_INFO
import android.net.NetworkInfo

@Suppress("DEPRECATION")
class ConnectivityProviderLegacyImpl(
    private val context: Context,
    private val cm: ConnectivityManager
) : ConnectivityProviderBaseImpl() {

    private val receiver = ConnectivityReceiver()

    override fun subscribe() {
        context.registerReceiver(receiver, IntentFilter(CONNECTIVITY_ACTION))
    }

    override fun unsubscribe() {
        context.unregisterReceiver(receiver)
    }

    override fun getNetworkState() = cm.activeNetworkInfo.toNetworkState()

    private fun NetworkInfo?.toNetworkState() = this
        ?.takeIf { it.isConnectedOrConnecting }
        ?.run { NetworkState.Connected }
        ?: NetworkState.NotConnectedState

    private inner class ConnectivityReceiver : BroadcastReceiver() {
        override fun onReceive(c: Context, intent: Intent) {
            // on some devices ConnectivityManager.getActiveNetworkInfo() does not provide the correct network state
            // https://issuetracker.google.com/issues/37137911
            val networkInfo = cm.activeNetworkInfo
            val fallbackNetworkInfo: NetworkInfo? = intent.getParcelableExtra(EXTRA_NETWORK_INFO)

            val state = when {
                networkInfo?.isConnectedOrConnecting == true -> networkInfo.toNetworkState()

                networkInfo != null && fallbackNetworkInfo != null &&
                        networkInfo.isConnectedOrConnecting != fallbackNetworkInfo.isConnectedOrConnecting
                -> fallbackNetworkInfo.toNetworkState()

                else -> (networkInfo ?: fallbackNetworkInfo).toNetworkState()
            }

            dispatchChange(state)
        }
    }
}
