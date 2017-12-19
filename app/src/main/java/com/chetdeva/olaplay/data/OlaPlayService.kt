package com.chetdeva.olaplay.data

import com.chetdeva.olaplay.data.dto.Song
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

interface OlaPlayService {

    @GET("studio")
    fun getPlaylist(): Single<List<Song>>
}