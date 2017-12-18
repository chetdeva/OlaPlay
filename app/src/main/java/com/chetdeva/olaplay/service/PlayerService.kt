package com.chetdeva.olaplay.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.chetdeva.olaplay.OlaPlayApplication
import javax.inject.Inject
import android.app.PendingIntent
import android.support.v4.app.NotificationCompat
import com.chetdeva.olaplay.MainActivity
import com.chetdeva.olaplay.R
import com.chetdeva.olaplay.util.*


/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 18/12/17
 */
class PlayerService : Service() {

    private val PLAYER_SERVICE_ID = 101

    @Inject lateinit var mediaPlayer: OlaMediaPlayer

    override fun onCreate() {
        (application as OlaPlayApplication).component.inject(this)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when {
            intent?.action == PLAY_ACTION -> play(intent.getStringExtra(SONG_URL))
            intent?.action == PAUSE_ACTION -> pause(intent.getStringExtra(SONG_URL))
            intent?.action == STOP_ACTION -> stop(intent.getStringExtra(SONG_URL))
            else -> {
            }
        }
        intent?.let { showNotification(it.getStringExtra(SONG_URL)) }
        return START_STICKY
    }

    private fun showNotification(url: String) {
        val playIntent = Intent(this, PlayerService::class.java)
        playIntent.action = PLAY_ACTION
        playIntent.putExtra(SONG_URL, url)

        val pauseIntent = Intent(this, PlayerService::class.java)
        pauseIntent.action = PAUSE_ACTION
        pauseIntent.putExtra(SONG_URL, url)

        val notification = NotificationCompat.Builder(this, "")
                .setContentTitle("OlaPlay Music Player")
                .setTicker("OlaPlay Music Player")
                .setContentText("My Music")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .addAction(NotificationCompat.Action.Builder(R.drawable.ic_play, "PLAY",
                        getActionPendingIntent(PLAY_ACTION, url)).build())
                .addAction(NotificationCompat.Action.Builder(R.drawable.ic_pause, "PAUSE",
                        getActionPendingIntent(PAUSE_ACTION, url)).build())
                .addAction(NotificationCompat.Action.Builder(R.drawable.ic_pause, "STOP",
                        getActionPendingIntent(STOP_ACTION, url)).build())
                .setContentIntent(getPendingIntent(url))
                .build()
        startForeground(PLAYER_SERVICE_ID, notification)
    }

    private fun getPendingIntent(url: String): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(SONG_URL, url)
        intent.putExtra(NAVIGATE_TO, "player")
        val requestCode = System.currentTimeMillis().toInt()
        return PendingIntent.getActivity(this, requestCode, intent, 0)
    }

    private fun getActionPendingIntent(action: String, url: String): PendingIntent {
        val intent = Intent(this, PlayerService::class.java)
        intent.action = action
        intent.putExtra(SONG_URL, url)
        val requestCode = System.currentTimeMillis().toInt()
        return PendingIntent.getService(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun play(url: String) {
        if (mediaPlayer.isPlaying) {
            return
        }
        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener({
                startPlayer()
            })
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
        if (!mediaPlayer.isPlaying) {
            startPlayer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
    }

    private fun pause(url: String) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    private fun stop(url: String) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stopAndReset()
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaPlayer.stopAndReset()
        mediaPlayer.release()
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        mediaPlayer.stopAndReset()
        return null
    }
}