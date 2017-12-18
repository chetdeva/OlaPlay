package com.chetdeva.olaplay.repository

import com.chetdeva.olaplay.data.OlaPlayService
import com.chetdeva.olaplay.data.Song
import com.chetdeva.olaplay.data.SongsDao
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

@Singleton
class PlaylistRepository
@Inject constructor(private val songsDao: SongsDao,
                    private val olaPlayService: OlaPlayService) {

    fun getPlaylist(): Flowable<List<Song>> {
        return Maybe.concat(songsDao.get(),
                olaPlayService.getPlaylist().toMaybe().doOnSuccess { songsDao.addAll(it) })
                .subscribeOn(Schedulers.io())
    }

    fun getSong(url: String): Flowable<Song> {
        return songsDao.get(url).toFlowable()
    }
}