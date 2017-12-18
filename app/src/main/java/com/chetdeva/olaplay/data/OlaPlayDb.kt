package com.chetdeva.olaplay.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 17/12/17
 */

@Database(entities = arrayOf(Song::class), version = 1, exportSchema = false)
abstract class OlaPlayDb : RoomDatabase() {

    internal abstract fun songsDao(): SongsDao
}