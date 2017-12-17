package com.chetdeva.olaplay.repository

import com.chetdeva.olaplay.api.OlaPlayService
import com.chetdeva.olaplay.api.entity.Song
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

@Singleton
class PlaylistRepository
@Inject constructor(private val olaPlayService: OlaPlayService) {

    fun getPlaylist(): Flowable<List<Song>> = olaPlayService.getPlaylist()

}