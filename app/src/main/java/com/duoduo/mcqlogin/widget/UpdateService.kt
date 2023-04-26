package com.duoduo.mcqlogin.widget

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.IBinder
import android.util.Log
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.duoduo.mcqlogin.R
import com.duoduo.mcqlogin.api.ApiClient
import com.duoduo.mcqlogin.api.LoginService
import kotlinx.coroutines.launch
import java.text.CharacterIterator
import java.text.StringCharacterIterator

class UpdateService : LifecycleService() {

    private val TAG = "UpdateService"


    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent!!.action != ACTION_UPDATE_SERVICE) {
            return START_NOT_STICKY
        }

        val loginService = ApiClient.get().create(LoginService::class.java)

        lifecycleScope.launch {
            val wifiManager =
                this@UpdateService.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val ssid = wifiInfo.ssid

            var status = "Unknown"
            var icon = R.drawable.baseline_error_outline_24
            var description = ""

            when (ssid) {
                "\"Malvern\"" -> {
                    val userInfo = loginService.userInfo().body()!!
                    Log.d(TAG, "userInfo = $userInfo")

                    when (userInfo.error) {
                        "not_online_error" -> {
                            status = "Not logged in"
                            icon = R.drawable.baseline_login_24
                        }
                        "ok" -> {
                            status = "Connected"
                            icon = R.drawable.baseline_wifi_24
                            description =
                                "User: ${userInfo.user_name}\n" +
                                        "Traffic: ${byteConvert(userInfo.sum_bytes)}\n" +
                                        "IP: ${userInfo.online_ip}"
                        }
                        else -> {
                            status = "Error"
                            icon = R.drawable.baseline_error_outline_24
                            description = "Error: ${userInfo.error}"
                        }
                    }
                }
                WifiManager.UNKNOWN_SSID -> {
                    if (wifiManager.wifiState == WifiManager.WIFI_STATE_DISABLED) {
                        status = "Not connected"
                        icon = R.drawable.baseline_signal_wifi_off_24
                    }
                }
                else -> {
                    status = "Not connected"
                    icon = R.drawable.baseline_signal_wifi_off_24
                }
            }

            val broadcast = Intent(this@UpdateService, LoginWidgetProvider::class.java)
            broadcast.action = LoginWidgetProvider.ACTION_UPDATE
            broadcast.putExtra(EXTRA_STATUS, status)
            broadcast.putExtra(EXTRA_ICON, icon)
            broadcast.putExtra(EXTRA_DESCRIPTION, description)
            sendBroadcast(broadcast)

        }


        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        const val ACTION_UPDATE_SERVICE = "com.duoduo.mcqlogin.UPDATE_SERVICE"
        const val EXTRA_STATUS = "STATUS"
        const val EXTRA_DESCRIPTION = "DESCRIPTION"
        const val EXTRA_ICON = "ICON"
    }
}

private fun byteConvert(bytes: Long): String? {
    val absB = if (bytes == Long.MIN_VALUE) Long.MAX_VALUE else Math.abs(bytes)
    if (absB < 1024) {
        return "$bytes B"
    }
    var value = absB
    val ci: CharacterIterator = StringCharacterIterator("KMGTPE")
    var i = 40
    while (i >= 0 && absB > 0xfffccccccccccccL shr i) {
        value = value shr 10
        ci.next()
        i -= 10
    }
    value *= java.lang.Long.signum(bytes).toLong()

    return java.lang.String.format(
        java.util.Locale.getDefault(),
        "%.1f %ciB",
        value / 1024.0,
        ci.current()
    )
}

