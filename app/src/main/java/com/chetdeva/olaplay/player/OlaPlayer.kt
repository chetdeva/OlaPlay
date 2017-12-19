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
 * A wrapper class for [android.media.MediaPlayer].
 *
 *
 * Encapsulates an instance of MediaPlayer, and makes a record of its internal state accessible via a
 * [MediaPlayerWrapper.getState] accessor. Most of the frequently used methods are available, but some still
 * need adding.
 *
 */
class OlaPlayer
@Inject constructor(private val fileProvider: OlaFileProvider) {
    private val tag = "MediaPlayerWrapper"

    val mp: MediaPlayer by lazy { MediaPlayer() }
    val ready: PublishSubject<Boolean> = PublishSubject.create()

    fun readyStateDisposable(onReady: () -> Unit): Disposable {
        return ready.subscribe { if (it) onReady() }
    }

    var pathPlaying: String? = null

    fun setAudioAttributesForMusic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val aa = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            mp.setAudioAttributes(aa)
        } else {
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
    }

    fun play(url: String) {
        if (mp.isPlaying) {
            ready.onNext(true)
            return
        }
        try {
            ready.onNext(false)
            setDataSource(url)
            mp.prepareAsync()
            mp.setOnPreparedListener({
                ready.onNext(true)
                mp.start()
            })
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
        if (!mp.isPlaying) {
            mp.start()
        }
    }

    private fun setDataSource(url: String) {
        pathPlaying = url
        if (fileProvider.isFileDownloaded(url)) {
            mp.setDataSource(fileProvider.getDownloadedFile(url)?.absolutePath)
        } else {
            mp.setDataSource(url)
        }
    }

    fun stopAndReset() {
        if (mp.isPlaying) {
            mp.stop()
            mp.reset()
        }
    }

    fun pause() {
        if (mp.isPlaying) mp.pause()
    }

    fun stop() {
        if (mp.isPlaying) mp.stop()
    }

    fun seekTo(mesc: Int) {
        if (mp.isPlaying) mp.seekTo(mesc)
    }
}