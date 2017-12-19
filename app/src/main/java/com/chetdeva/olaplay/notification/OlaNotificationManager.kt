package com.chetdeva.olaplay.notification

import android.app.Application
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.chetdeva.olaplay.MainActivity
import com.chetdeva.olaplay.R
import com.chetdeva.olaplay.data.dto.Song
import com.chetdeva.olaplay.player.OlaPlayerService
import com.chetdeva.olaplay.util.*
import javax.inject.Inject

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 19/12/17
 */
class OlaNotificationManager
@Inject constructor(val application: Application) {

    fun showNotification(action: String, song: Song): Notification {
        val builder = NotificationCompat.Builder(application, "")
                .setContentTitle(application.getString(R.string.playing, song.song))
                .setTicker(application.getString(R.string.playing, song.song))
                .setContentText(song.artists)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setContentIntent(getPendingIntent(song.url))

        when (action) {
            PLAY_ACTION -> builder.addAction(NotificationCompat.Action.Builder(R.drawable.ic_pause, "PAUSE",
                    getActionPendingIntent(PAUSE_ACTION, song)).build())

            PAUSE_ACTION, STOP_ACTION -> builder.addAction(NotificationCompat.Action.Builder(R.drawable.ic_play, "PLAY",
                    getActionPendingIntent(PLAY_ACTION, song)).build())
        }
        builder.addAction(NotificationCompat.Action.Builder(R.drawable.ic_stop, "STOP",
                getActionPendingIntent(RESET_ACTION, song)).build())
        return builder.build()
    }

    private fun getPendingIntent(url: String): PendingIntent {
        val intent = Intent(application, MainActivity::class.java)
        intent.putExtra(SONG_URL, url)
        intent.putExtra(NAVIGATE_TO, NAVIGATE_TO_PLAYER)
        val requestCode = System.currentTimeMillis().toInt()
        return PendingIntent.getActivity(application, requestCode, intent, 0)
    }

    private fun getActionPendingIntent(action: String, song: Song): PendingIntent {
        val intent = Intent(application, OlaPlayerService::class.java)
        intent.action = action
        intent.putExtra(SONG, song)
        val requestCode = System.currentTimeMillis().toInt()
        return PendingIntent.getService(application, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}