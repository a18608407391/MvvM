package com.zl.zlibrary.entity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.zl.zlibrary.Utils.context

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class ONetWorkLiveData : LiveData<NetworkInfo> {


    var mContext: Context? = null
    var intentFilter: IntentFilter? = null

    var mReceiver: ONetWorkReceiver? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context) {
        this.mContext = context
        mReceiver = ONetWorkReceiver(this)
        intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    }

    override fun onActive() {
        super.onActive()
        mContext!!.registerReceiver(mReceiver, intentFilter)
    }

    override fun onInactive() {
        super.onInactive()
        mContext!!.unregisterReceiver(mReceiver)
    }


    companion object {
        val instance: ONetWorkLiveData by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ONetWorkLiveData(context)
        }

        class ONetWorkReceiver : BroadcastReceiver {
            var liveData: ONetWorkLiveData? = null

            constructor(liveData: ONetWorkLiveData) {
                this.liveData = liveData
            }

            override fun onReceive(context: Context?, intent: Intent?) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    val manager =
                        context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val activeNetwork = manager.activeNetworkInfo
                    liveData!!.value = activeNetwork
                }
            }
        }
    }
}