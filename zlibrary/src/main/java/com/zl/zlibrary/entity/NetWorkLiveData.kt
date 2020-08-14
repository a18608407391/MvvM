package com.zl.zlibrary.entity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.zl.zlibrary.Utils.context


class NetWorkLiveData : LiveData<NetworkCapabilities> {


    var mContext: Context? = null
    var intentFilter: IntentFilter? = null

    var mReceiver: NetWorkReceiver? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context) {
        this.mContext = context


    }


    var callback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object : ConnectivityManager.NetworkCallback() {
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)

            this@NetWorkLiveData.value = networkCapabilities
        }

        override fun onLost(network: Network) {
            super.onLost(network)

            this@NetWorkLiveData.value = null
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActive() {
        super.onActive()
        registerNetworkCallback(mContext!!)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onInactive() {
        super.onInactive()
        unregisterNetworkCallback(mContext!!)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun registerNetworkCallback(context: Context) {
        var cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var builder = NetworkRequest.Builder()
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        cm.registerNetworkCallback(builder.build(), callback)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun unregisterNetworkCallback(context: Context) {
        var cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.unregisterNetworkCallback(callback)
    }


    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.M)
    fun isMobile(): Boolean {
        var cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var net: Network? = cm.activeNetwork ?: return false
        var cap: NetworkCapabilities? = cm.getNetworkCapabilities(net) ?: return false
        return cap!!.hasCapability(NetworkCapabilities.TRANSPORT_CELLULAR)
    }


    companion object {
        class NetWorkReceiver : BroadcastReceiver {
            var liveData: NetWorkLiveData? = null

            constructor(liveData: NetWorkLiveData) {
                this.liveData = liveData
            }

            override fun onReceive(context: Context?, intent: Intent?) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    val manager =
                        context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val activeNetwork = manager.activeNetworkInfo
//                    liveData!!.value = activeNetwork
                }
            }
        }
    }
}