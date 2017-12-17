package com.chetdeva.olaplay.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.chetdeva.olaplay.api.entity.Song
import com.chetdeva.olaplay.repository.PlaylistRepository
import com.chetdeva.olaplay.util.toLiveData
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

class PlaylistModel
@Inject constructor(private val playlistRepository: PlaylistRepository) : ViewModel() {

    val songs: LiveData<List<Song>>

    init {
        songs = getPlaylistRepository()
    }

    private fun getPlaylistRepository(): LiveData<List<Song>> {
        return playlistRepository.getPlaylist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(Flowable.empty())
                .toLiveData()
    }
}