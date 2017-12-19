package com.chetdeva.olaplay.di

import com.chetdeva.olaplay.download.OlaFileProvider
import com.chetdeva.olaplay.player.OlaPlayer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 19/12/17
 */

@Module
class PlayerModule {

    @Singleton
    @Provides
    fun provideMediaPlayer(olaFileProvider: OlaFileProvider): OlaPlayer {
        val mediaPlayer = OlaPlayer(olaFileProvider)
        mediaPlayer.setAudioAttributesForMusic()
        return mediaPlayer
    }
}