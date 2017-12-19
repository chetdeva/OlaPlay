package com.chetdeva.olaplay.download

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 19/12/17
 */
class DownloadService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}