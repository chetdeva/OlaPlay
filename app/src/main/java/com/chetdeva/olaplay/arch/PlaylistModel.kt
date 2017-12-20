package com.chetdeva.olaplay.arch

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.chetdeva.olaplay.data.dto.Song
import com.chetdeva.olaplay.repository.PlaylistRepository
import com.chetdeva.olaplay.rx.SchedulerProvider
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

@Singleton
class PlaylistModel
@Inject constructor(private val playlistRepository: PlaylistRepository,
                    private val schedulerProvider: SchedulerProvider)
    : ViewModel() {

    val songs: LiveData<List<Song>>
    lateinit var song: LiveData<Song>

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
}