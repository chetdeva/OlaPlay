package com.chetdeva.olaplay.download

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.chetdeva.olaplay.data.dto.DownloadItem
import com.chetdeva.olaplay.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.util.HashSet
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 19/12/17
 */

@Singleton
class OlaDownloadManager
@Inject constructor(private val application: Application,
                    private val httpClient: OkHttpClient,
                    private val fileProvider: OlaFileProvider,
                    private val connectivityManager: ConnectivityManager,
                    private val schedulerProvider: SchedulerProvider) {

    private val downloadQueue: HashSet<DownloadItem> = LinkedHashSet()
    private val downloading: HashSet<DownloadItem> = LinkedHashSet()

    val downloadStatePublisher: PublishSubject<DownloadItem> = PublishSubject.create()

    init {
        registerConnectivityReceiver()
    }


    /**
     * intended to resume download when connectivity is available
     */
    private fun registerConnectivityReceiver() {
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)

        application.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                resumeDownload()
            }
        }, filter)
    }

    private fun resumeDownload() {
        if (downloading.size == 0 && isConnected) {
            downloadNextItem()
        }
    }

    /**
     * call this method to add url to download queue
     */
    fun addToDownloadQueue(item: DownloadItem) {
        if (fileProvider.isFileDownloaded(url = item.url)) {
            item.file = fileProvider.getDownloadedFile(item.url)
            item.isLoadedFromCache = true
            item.endTime = System.currentTimeMillis()
            item.state = SongDownloadState.DOWNLOAD_COMPLETE

            downloadStatePublisher.onNext(item)
        } else if (!downloading.contains(item)) {
            downloadQueue.add(item)

            if (downloading.size == 0 && isConnected) {
                downloadNextItem()
            }
        }
    }

    private fun downloadNextItem() {
        val it = downloadQueue.iterator()
        if (it.hasNext()) {
            val next = it.next()
            it.remove()
            startFileDownload(next)
        }
    }

    private fun startFileDownload(item: DownloadItem) {
        downloading.add(item)

        downloadFile(url = item.url)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.io())
                .subscribe({ file ->
                    item.file = file
                    item.endTime = System.currentTimeMillis()
                    item.state = SongDownloadState.DOWNLOAD_COMPLETE

                    downloadStatePublisher.onNext(item)
                    downloading.remove(item)
                    downloadNextItem()
                }, {
                    // Add download to the queue again
                    downloading.remove(item)
                    addToDownloadQueue(item)
                })
    }

    /**
     * download file from url and save it
     */
    private fun downloadFile(url: String): Single<File> {
        return Single.create { emitter ->
            try {
                val response = httpClient.newCall(Request.Builder()
                        .url(url)
                        .build()).execute()

                if (response.isSuccessful) {
                    emitter.onSuccess(fileProvider.saveToFile(response, fileProvider.getFileKey(url)))
                } else {
                    emitter.onError(HttpException(Response.error<Any>(response.body()!!,
                            response)))
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    private val isConnected: Boolean get() = connectivityManager.activeNetworkInfo?.isConnected ?: false

}