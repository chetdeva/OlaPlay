package com.chetdeva.olaplay.player

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.chetdeva.olaplay.OlaPlayApplication
import com.chetdeva.olaplay.data.dto.Song
import javax.inject.Inject
import com.chetdeva.olaplay.notification.OlaNotificationManager
import com.chetdeva.olaplay.util.*
import io.reactivex.disposables.CompositeDisposable

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 18/12/17
 */
class OlaPlayerService : Service() {

    private val PLAYER_SERVICE_ID = 101
    private val disposables by lazy { CompositeDisposable() }

    @Inject lateinit var player: OlaPlayer
    @Inject lateinit var notifier: OlaNotificationManager

    override fun onCreate() {
        (application as OlaPlayApplication).component.inject(this)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val song = intent.getParcelableExtra<Song>(SONG)
        when {
            intent.action == PLAY_ACTION -> play(song)
            intent.action == PAUSE_ACTION -> pause(song)
            intent.action == STOP_ACTION -> stop(song)
            intent.action == RESET_ACTION -> reset(song)
            else -> {
            }
        }
        if (intent.action != RESET_ACTION) notify(intent.action, song)
        return START_STICKY
    }

    private fun notify(action: String, song: Song) {
        val notification = notifier.showNotification(action, song)
        if (action == PLAY_ACTION && !player.isPlaying) {
            disposables.add(player.readyStateDisposable({ startForeground(PLAYER_SERVICE_ID, notification) }))
        } else {
            startForeground(PLAYER_SERVICE_ID, notification)
        }
    }

    private fun play(song: Song) {
        player.play(song.url)
    }

    private fun pause(song: Song) {
        player.pause()
    }

    private fun stop(song: Song) {
        player.stop()
    }

    private fun reset(song: Song) {
        player.stopAndReset()
        stopSelf()
    }

    override fun onDestroy() {
        player.stopAndReset()
        disposables.clear()
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        player.stopAndReset()
        return null
    }
}