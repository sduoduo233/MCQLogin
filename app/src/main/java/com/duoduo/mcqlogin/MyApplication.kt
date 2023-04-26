package com.duoduo.mcqlogin

import android.app.Application
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import com.duoduo.mcqlogin.widget.LoginWidgetProvider
import com.duoduo.mcqlogin.widget.UpdateService

class MyApplication: Application() {
    private val TAG = "MyApplication"

    override fun onCreate() {
        super.onCreate()
        Log.d("Application", "onCreate")

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        val connectivityManager = getSystemService(ConnectivityManager::class.java)

        connectivityManager.requestNetwork(networkRequest, object: ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                Log.d(TAG, networkCapabilities.toString())

                val intent = Intent(this@MyApplication, UpdateService::class.java)
                intent.action = UpdateService.ACTION_UPDATE_SERVICE
                startService(intent)
            }
        })
    }
}