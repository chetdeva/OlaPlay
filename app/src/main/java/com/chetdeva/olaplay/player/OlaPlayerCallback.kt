package com.chetdeva.olaplay.player

import com.chetdeva.olaplay.data.dto.Song

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 18/12/17
 */
interface OlaPlayerCallback {

    fun playOrPause(song: Song)

    fun stop(song: Song)

    fun download(song: Song)
}