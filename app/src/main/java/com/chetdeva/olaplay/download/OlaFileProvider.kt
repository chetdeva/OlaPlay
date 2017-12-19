package com.chetdeva.olaplay.download

import android.app.Application
import android.net.Uri
import android.support.v4.content.FileProvider
import com.chetdeva.olaplay.util.BuildInfo
import com.chetdeva.olaplay.util.DigestProvider
import okhttp3.Response
import okio.BufferedSink
import okio.Okio
import java.io.File
import java.io.IOException
import javax.inject.Inject

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 19/12/17
 */

class OlaFileProvider
@Inject constructor(private val application: Application,
                    private val digestProvider: DigestProvider,
                    private val buildInfo: BuildInfo) {

    private val DOWNLOAD_DIR = "downloads"

    @Throws(IOException::class)
    fun saveToFile(response: Response, key: String): File {
        val file = File(getCacheDir(), key)
        var bufferedSink: BufferedSink? = null

        try {
            bufferedSink = Okio.buffer(Okio.sink(file))
            bufferedSink!!.writeAll(response.body()!!.source())
            return file
        } finally {
            if (bufferedSink != null) {
                bufferedSink.close()
            }
        }
    }

    private fun getCacheDir(): File {
        val cache = File(application.externalCacheDir, DOWNLOAD_DIR)
        if (!cache.exists()) {
            cache.mkdirs()
        }
        return cache
    }

    private fun doesFileExists(key: String) = File(getCacheDir(), key).exists()

    private fun getFile(key: String): File? {
        if (doesFileExists(key)) {
            return File(getCacheDir(), key)
        }
        return null
    }

    fun getFileKey(url: String) = digestProvider.getSHA1HexString(url)

    fun isFileDownloaded(url: String) = doesFileExists(getFileKey(url))

    fun getDownloadedFile(url: String) = getFile(getFileKey(url))

    // more utils

    fun getUriForFile(file: File) = FileProvider.getUriForFile(application, buildInfo.authority, file)

    fun getFilePath(url: String) = getCacheDir().absolutePath + "/" + getFileKey(url)

}