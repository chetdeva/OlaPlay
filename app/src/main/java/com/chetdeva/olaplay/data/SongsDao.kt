package com.chetdeva.olaplay.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.chetdeva.olaplay.data.dto.Song
import io.reactivex.Maybe

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 17/12/17
 */

@Dao
interface SongsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(song: Song)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(songs: List<Song>)

    @Query("SELECT * FROM songs")
    fun get(): Maybe<List<Song>>

    @Query("SELECT * FROM songs WHERE url=:url")
    fun get(url: String): Maybe<Song>

}