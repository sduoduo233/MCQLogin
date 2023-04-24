package com.duoduo.mcqlogin.utils

import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun md5(value: String, key: String): String {
    return try {
        val mac: Mac = Mac.getInstance("HmacMD5")
        val keySpec = SecretKeySpec(key.encodeToByteArray(), "HmacMD5")
        mac.init(keySpec)
        val macBytes: ByteArray = mac.doFinal(value.encodeToByteArray())

        byteToHex(macBytes)
    } catch (e: NoSuchAlgorithmException) {
        throw RuntimeException("Failed to calculate HMAC-MD5", e)
    } catch (e: InvalidKeyException) {
        throw RuntimeException("Failed to calculate HMAC-MD5", e)
    } catch (e: UnsupportedEncodingException) {
        throw RuntimeException("Failed to calculate HMAC-MD5", e)
    }
}


fun sha1(value: String): String {
    var sha1 = ""
    try {
        val crypt: MessageDigest = MessageDigest.getInstance("SHA-1")
        crypt.reset()
        crypt.update(value.toByteArray(charset("UTF-8")))
        sha1 = byteToHex(crypt.digest())
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    }
    return sha1
}

private fun byteToHex(hash: ByteArray): String {
    val formatter = Formatter()
    for (b in hash) {
        formatter.format("%02x", b)
    }
    val result: String = formatter.toString()
    formatter.close()
    return result
}


