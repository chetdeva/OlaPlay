package com.chetdeva.olaplay.arch

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.chetdeva.olaplay.data.dto.DownloadItem
import com.chetdeva.olaplay.data.dto.Song
import com.chetdeva.olaplay.download.OlaDownloadState
import com.chetdeva.olaplay.download.OlaDownloadManager
import com.chetdeva.olaplay.repository.PlaylistRepository
import com.chetdeva.olaplay.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

@Singleton
class PlayerModel
@Inject constructor(private val playlistRepository: PlaylistRepository,
                    private val schedulerProvider: SchedulerProvider,
                    private val downloader: OlaDownloadManager)
    : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }
    val olaDownloadState: MutableLiveData<OlaDownloadState> = MutableLiveData()
    private var urlToDownload: String = ""
    lateinit var song: LiveData<Song>

    init {
        subscribeToDownloader()
    }

    fun getSong(url: String): LiveData<Song> {
        song = playlistRepository.getSong(url)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .toLiveData()
        return song
    }

    private fun subscribeToDownloader() {
        val disposable = downloader.downloadStatePublisher
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ d ->
                    if (urlToDownload.isNotBlank() && d.url == urlToDownload) {
                        updateDownloadState(d.state)
                    }
                }, { println(it) })

        compositeDisposable.add(disposable)
    }

    fun addToDownloadQueue(url: String, id: String, name: String) {
        this.urlToDownload = url
        olaDownloadState.postValue(OlaDownloadState.DOWNLOADING)
        downloader.addToDownloadQueue(DownloadItem(url, id, name))
    }

    private fun updateDownloadState(state: OlaDownloadState) {
        olaDownloadState.postValue(state)
    }

}