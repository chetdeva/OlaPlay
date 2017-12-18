package com.chetdeva.olaplay.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.chetdeva.olaplay.data.Song
import com.chetdeva.olaplay.repository.PlaylistRepository
import com.chetdeva.olaplay.rx.SchedulerProvider
import com.chetdeva.olaplay.util.toLiveData
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

class PlaylistModel
@Inject constructor(private val playlistRepository: PlaylistRepository,
                    private val schedulerProvider: SchedulerProvider)
    : ViewModel() {

    val songs: LiveData<List<Song>>

    init {
        songs = getPlaylistRepository()
    }

    private fun getPlaylistRepository(): LiveData<List<Song>> {
        return playlistRepository.getPlaylist()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .onErrorResumeNext(Flowable.empty())
                .toLiveData()
    }

    fun getSong(url: String): LiveData<Song> {
        return playlistRepository.getSong(url)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .toLiveData()
    }
}