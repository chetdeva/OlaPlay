package com.chetdeva.olaplay.api

import com.chetdeva.olaplay.api.entity.Song
import io.reactivex.Flowable
import retrofit2.http.GET

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

interface OlaPlayService {

    @GET("studio")
    fun getPlaylist(): Flowable<List<Song>>
}