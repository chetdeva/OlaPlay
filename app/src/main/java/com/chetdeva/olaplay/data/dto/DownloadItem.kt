package com.chetdeva.olaplay.data.dto

import com.chetdeva.olaplay.download.SongDownloadState
import java.io.File
import java.util.concurrent.TimeUnit

class DownloadItem @JvmOverloads constructor(var url: String, var id: String, var name: String, var isLoadedFromCache: Boolean = false) {
    var file: File? = null
    var startTime: Long = 0
    var endTime: Long = 0
    var state = SongDownloadState.DOWNLOAD_COMPLETE

    init {
        this.startTime = System.currentTimeMillis()
    }

    fun getDownloadTime(unit: TimeUnit): Long {
        return unit.convert(endTime - startTime, TimeUnit.MILLISECONDS)
    }

    override fun equals(obj: Any?): Boolean {
        return if (obj is DownloadItem) {
            obj.url == url
        } else super.equals(obj)

    }

    override fun hashCode(): Int {
        return url?.hashCode() ?: 0
    }

}