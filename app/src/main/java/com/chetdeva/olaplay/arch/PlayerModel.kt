package com.chetdeva.olaplay.arch

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.chetdeva.olaplay.data.dto.DownloadItem
import com.chetdeva.olaplay.data.dto.Song
import com.chetdeva.olaplay.download.SongDownloadState
import com.chetdeva.olaplay.download.OlaDownloadManager
import com.chetdeva.olaplay.repository.PlaylistRepository
import com.chetdeva.olaplay.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

class PlayerModel
@Inject constructor(private val playlistRepository: PlaylistRepository,
                    private val schedulerProvider: SchedulerProvider,
                    private val downloader: OlaDownloadManager)
    : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }
    val songDownloadState: MutableLiveData<SongDownloadState> = MutableLiveData()
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
        songDownloadState.postValue(SongDownloadState.DOWNLOADING)
        downloader.addToDownloadQueue(DownloadItem(url, id, name))
    }

    private fun updateDownloadState(state: SongDownloadState) {
        songDownloadState.postValue(state)
    }

}