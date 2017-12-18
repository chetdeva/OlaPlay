package com.chetdeva.olaplay.ui

import com.chetdeva.olaplay.data.Song

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 18/12/17
 */
interface PlayerCallback {

    fun playOrPause(song: Song)
}