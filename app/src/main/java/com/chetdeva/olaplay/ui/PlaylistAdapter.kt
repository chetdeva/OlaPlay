package com.chetdeva.olaplay.ui

import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chetdeva.olaplay.R
import com.chetdeva.olaplay.data.Song
import com.chetdeva.olaplay.binding.DataBoundListAdapter
import com.chetdeva.olaplay.databinding.ItemSongBinding
import java.util.*

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 17/12/17
 */

class PlaylistAdapter
constructor(private val dataBindingComponent: DataBindingComponent, private val songClickCallback: SongClickCallback)
    : DataBoundListAdapter<Song, ItemSongBinding>() {

    override fun createBinding(parent: ViewGroup): ItemSongBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_song,
                parent, false, dataBindingComponent)
    }

    override fun bind(binding: ItemSongBinding, item: Song) {
        binding.song = item
        binding.root.setOnClickListener({ songClickCallback.onClick(item) })
    }

    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return Objects.equals(oldItem.song, newItem.song) && Objects.equals(oldItem.artists, newItem.artists)
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return Objects.equals(oldItem.url, newItem.url) && oldItem.cover_image === newItem.cover_image
    }

    interface SongClickCallback {
        fun onClick(song: Song)
    }
}