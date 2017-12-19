package com.chetdeva.olaplay.util

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DigestProvider
@Inject constructor() {

    private val KEY_ALGO_SHA1 = "SHA-1"
    private val DEFAULT_CHARSET = "UTF-8"

    private val shA1Digest: MessageDigest?
        get() {
            try {
                return MessageDigest.getInstance(KEY_ALGO_SHA1)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return null
        }

    fun getSHA1HexString(data: String): String {
        try {
            return encodeHex(shA1Digest!!.digest(data.toByteArray(charset(DEFAULT_CHARSET))))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return ""
    }

    fun encodeHex(data: ByteArray): String {
        val sb = StringBuilder()
        for (b in data) {
            sb.append(Integer.toHexString(b.toInt() and 0xFF or 0x100).substring(1, 3))
        }
        return sb.toString()
    }
}