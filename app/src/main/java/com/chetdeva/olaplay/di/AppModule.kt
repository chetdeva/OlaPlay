package com.chetdeva.olaplay.di

import android.media.AudioManager
import com.chetdeva.olaplay.rx.SchedulerProvider
import com.chetdeva.olaplay.rx.SchedulerProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import android.media.AudioAttributes
import android.os.Build
import com.chetdeva.olaplay.util.OlaMediaPlayer


/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideSchedulers(providerImpl: SchedulerProviderImpl): SchedulerProvider = providerImpl

    @Singleton
    @Provides
    fun provideMediaPlayer(): OlaMediaPlayer {
        val mediaPlayer = OlaMediaPlayer()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val aa = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            mediaPlayer.setAudioAttributes(aa)
        } else {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
        return mediaPlayer
    }
}