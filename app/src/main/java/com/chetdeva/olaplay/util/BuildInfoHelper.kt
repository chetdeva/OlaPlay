package com.chetdeva.olaplay.util

import android.annotation.SuppressLint
import android.app.Application
import android.provider.Settings
import com.chetdeva.olaplay.BuildConfig
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 19/12/17
 */
object BuildInfoHelper {

    private val ALGORITHM_MD5 = "MD5"

    fun getBuildInfo(application: Application): BuildInfo {
        return BuildInfo(BuildConfig.BASE_URL, getDeviceId(application), BuildConfig.APPLICATION_ID + ".provider")
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(application: Application): String {
        var id: String = Settings.Secure.getString(application.contentResolver,
                Settings.Secure.ANDROID_ID)

        // Convert ID to 128 bits using MD5 hash
        id = getMD5Hash(id)

        return id
    }

    fun getMD5Hash(value: String?): String {
        try {
            val md = MessageDigest.getInstance(ALGORITHM_MD5)
            val array = md.digest(value!!.toByteArray())
            val sb = StringBuilder()
            for (byt in array) {
                sb.append(Integer.toHexString(((byt.toInt() and 0xFF) or 0x100)).substring(1, 3))
            }
            return sb.toString().replace("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})".toRegex(), "$1-$2-$3-$4-$5")
        } catch (ignored: NoSuchAlgorithmException) {
            ignored.printStackTrace()
        }
        return ""
    }

}