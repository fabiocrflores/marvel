package com.marvel.developer.rest.interceptor

import com.marvel.developer.rest.interceptor.ApiKeyInterceptor.Keys.apiKey
import com.marvel.developer.rest.interceptor.ApiKeyInterceptor.Keys.privateKey
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class ApiKeyInterceptor : Interceptor {

    object Keys {
        const val privateKey = "8bf03c0132a99e81383dab2298061c82e86c0d94"
        const val apiKey = "f4c8f084c2f5a73f09ae3c2ecff2be9f"
    }

    companion object {
        private const val TIME_STAMP = "ts"
        private const val API_KEY = "apikey"
        private const val HASH = "hash"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var originalRequest = chain.request()

        val timeStamp = System.currentTimeMillis().toString()
        val url = originalRequest.url().newBuilder()
            .addQueryParameter(TIME_STAMP, timeStamp)
            .addQueryParameter(API_KEY, apiKey)
            .addQueryParameter(HASH, getHash(timeStamp))
            .build()

        originalRequest = originalRequest.newBuilder().url(url).build()

        return chain.proceed(originalRequest)
    }

    private fun getHash(tS: String): String = md5(tS + privateKey + apiKey)

    private fun md5(input: String): String {
        return try {
            val digest = MessageDigest.getInstance("MD5")
            digest.update(input.toByteArray())
            val messageDigest = digest.digest()

            bytesToHexString(messageDigest)
        } catch (e: NoSuchAlgorithmException) {
            Timber.e("MD5 ${e.localizedMessage!!}")
            throw IllegalStateException(e)
        }
    }

    private fun bytesToHexString(bytes: ByteArray): String {
        val hexString = StringBuffer()
        for (i in bytes.indices) {
            var h = Integer.toHexString(0xFF and bytes[i].toInt())
            while (h.length < 2) {
                h = "0$h"
            }
            hexString.append(h)
        }

        return hexString.toString()
    }
}