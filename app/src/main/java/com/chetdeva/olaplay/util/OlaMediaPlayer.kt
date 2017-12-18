package com.chetdeva.olaplay.util

import android.media.MediaPlayer

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 19/12/17
 */
class OlaMediaPlayer : MediaPlayer() {

    var urlPlaying: String? = null

    override fun setDataSource(path: String?) {
        super.setDataSource(path)
        urlPlaying = path
    }

    fun stopAndReset() {
        stop()
        reset()
    }
}
