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

    val player: MediaPlayer by lazy { MediaPlayer() }
    val isPlaying: Boolean get() = player.isPlaying
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
        pathPlaying = url
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

    fun seekTo(mesc: Int) {
        if (isPlaying) player.seekTo(mesc)
    }
}