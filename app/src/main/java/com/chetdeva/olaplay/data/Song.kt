package com.chetdeva.olaplay.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

@Entity(tableName = "songs")
class Song(@SerializedName("song") val song: String,
           @PrimaryKey
           @SerializedName("url") val url: String,
           @SerializedName("artists") val artists: String,
           @SerializedName("cover_image") val cover_image: String) {

}