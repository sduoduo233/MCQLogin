package com.duoduo.mcqlogin.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoduo.mcqlogin.api.ApiClient
import com.duoduo.mcqlogin.api.LoginService
import com.duoduo.mcqlogin.api.LoginUserInfo
import com.duoduo.mcqlogin.utils.Encoding2
import com.duoduo.mcqlogin.utils.md5
import com.duoduo.mcqlogin.utils.sha1
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import org.jetbrains.annotations.NotNull
import java.io.IOException
import java.util.*

private val TAG = "MainViewModel"

class MainViewModel : ViewModel() {
    val ipAddress: MutableLiveData<String> = MutableLiveData<String>("")
    val username: MutableLiveData<String> by lazy { MutableLiveData<String>("T796") }
    val password: MutableLiveData<String> by lazy { MutableLiveData<String>("1234") }
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val error: MutableLiveData<String> = MutableLiveData("")

    @NotNull
    private val service = ApiClient.get().create(LoginService::class.java)

    fun onUsernameChanged(it: String) {
        username.value = it
    }

    fun onPasswordChanged(it: String) {
        password.value = it
    }

    fun onIPAddressChanged(it: String) {
        ipAddress.value = it
    }

    init {
        // Get ip address
        viewModelScope.launch {
            try {
                val userInfo = service.userInfo()
                ipAddress.value = userInfo.body()!!.online_ip
            } catch (e: IOException) {
                error.value = e.message
                e.printStackTrace()
            }
            isLoading.value = false
        }
    }

    fun onLogin() {
        isLoading.value = true

        if (username.value!!.isEmpty() || password.value!!.isEmpty()) {
            isLoading.value = false
            error.value = "Empty username or password"
            return
        }

        viewModelScope.launch {
            try {
                Log.d(TAG, "username = ${username.value!!}")
                Log.d(TAG, "password = ${password.value!!}")
                Log.d(TAG, "ip = ${ipAddress.value!!}")

                // challenge
                val resp = service.getChallenge(username.value!!, ipAddress.value!!)
                val challenge = resp.body()!!.challenge
                Log.d(TAG, "challenge = $challenge")

                // constants
                val mType = 1
                val n = 200
                val enc = "srun_bx1"
                val ac_id = "1"

                // hmd5
                val hmd5 = md5(password.value!!, challenge)
                Log.d(TAG, "hmd5 = $hmd5")

                // user info
                val gson = GsonBuilder().create()
                val adapter = gson.getAdapter(LoginUserInfo::class.java)
                var info = adapter.toJson(LoginUserInfo(
                    acid = ac_id,
                    enc_ver = enc,
                    ip = ipAddress.value!!,
                    password = password.value!!,
                    username = username.value!!
                ))
                info = info.replace(" ", "")
                Log.d(TAG, "info = $info")

                // encode user info
                val encodedInfo = Encoding2.encode(info, challenge)
                Log.d(TAG, "encodedInfo = ${Base64.getEncoder().encodeToString(encodedInfo.encodeToByteArray())}")

                val b64EncodedInfo = "{SRBX1}" + com.duoduo.mcqlogin.utils.Base64.encode(encodedInfo)
                Log.d(TAG, "b64EncodedInfo = $b64EncodedInfo")

                // chksum
                val s = StringBuilder()
                s.append(challenge).append(username.value!!)
                s.append(challenge).append(hmd5)
                s.append(challenge).append(ac_id)
                s.append(challenge).append(ipAddress.value!!)
                s.append(challenge).append(n)
                s.append(challenge).append(mType)
                s.append(challenge).append(b64EncodedInfo)
                Log.d(TAG, "raw checksum = $s")

                val chksum = sha1(s.toString())
                Log.d(TAG, "sha1 check sum = $chksum")

                val loginResp = service.srunPortal(
                    callback = "c",
                    action = "login",
                    username = username.value!!,
                    password = "{MD5}$hmd5",
                    os = "Windows 10",
                    name = "Windows",
                    doubleStack = "0",
                    chksum = chksum,
                    info = b64EncodedInfo,
                    acid = ac_id,
                    ip = ipAddress.value!!,
                    n = n.toString(),
                    type = mType.toString()
                )
                Log.d(TAG, "resp = ${loginResp.body()}")

                if (loginResp.body()!!.error != "ok") {
                    error.value = "error=${loginResp.body()!!.error} msg=${loginResp.body()!!.error_msg}"
                }

            } catch (e: IOException) {
                error.value = e.message
                e.printStackTrace()
            }
            isLoading.value = false
        }
    }

    fun onErrorShown() {
        error.value = ""
    }

}