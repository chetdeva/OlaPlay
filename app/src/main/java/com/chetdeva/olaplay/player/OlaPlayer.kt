package com.chetdeva.olaplay.player

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import com.chetdeva.olaplay.download.OlaFileProvider
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 18/12/17
 */
class OlaPlayer
@Inject constructor(private val fileProvider: OlaFileProvider) {

    private val player: MediaPlayer by lazy { MediaPlayer() }

    fun readyStateDisposable(onReady: () -> Unit): Disposable {
        return ready.subscribe { if (it) onReady() }
    }

    fun setAudioAttributesForMusic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val aa = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            player.setAudioAttributes(aa)
        } else {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
    }

    fun play(url: String) {
        if (isPlaying) {
            ready.onNext(true)
            return
        }
        try {
            ready.onNext(false)
            setDataSource(url)
            player.prepareAsync()
            player.setOnPreparedListener({
                ready.onNext(true)
                player.start()
            })
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
        if (!isPlaying) {
            player.start()
        }
    }

    private fun setDataSource(url: String) {
        urlPlaying = url
        if (fileProvider.isFileDownloaded(url)) {
            player.setDataSource(fileProvider.getDownloadedFile(url)?.absolutePath)
        } else {
            player.setDataSource(url)
        }
    }

    fun stopAndReset() {
        if (isPlaying) {
            player.stop()
            player.reset()
        }
    }

    fun pause() {
        if (isPlaying) player.pause()
    }

    fun stop() {
        if (isPlaying) player.stop()
    }

    fun seekTo(msec: Int) {
        if (isPlaying) player.seekTo(msec)
    }

    val isPlaying: Boolean get() = player.isPlaying
    var urlPlaying: String? = null
    val ready: PublishSubject<Boolean> = PublishSubject.create()
}