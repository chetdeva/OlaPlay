package com.chetdeva.olaplay.ui


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingComponent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.chetdeva.olaplay.R
import com.chetdeva.olaplay.binding.FragmentDataBindingComponent
import com.chetdeva.olaplay.data.Song
import com.chetdeva.olaplay.databinding.FragmentPlayerBinding
import com.chetdeva.olaplay.di.Injectable
import com.chetdeva.olaplay.util.BindFragment
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class PlayerFragment : Fragment(), Injectable, PlayerCallback {

    private val bindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private val binding: FragmentPlayerBinding by BindFragment(R.layout.fragment_player, bindingComponent)

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.callback = this

        val playlistModel = ViewModelProviders.of(this, viewModelFactory).get(PlaylistModel::class.java)

        val songUrl: String = arguments.getString("song_url")
        playlistModel.getSong(songUrl).observe(this, Observer {
            it?.let {
                binding.song = it
                play(it)
            }
        })
    }

    override fun playOrPause(song: Song) {
        if (!mediaPlayer.isPlaying) {
            play(song)
        } else {
            pause(song)
        }
    }

    private fun play(song: Song) {
        if (mediaPlayer.isPlaying) {
            return
        }
        binding.playing = false
        try {
            mediaPlayer.setDataSource(song.url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener({
                mediaPlayer.start()
            })
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    private fun pause(song: Song) {
        if (mediaPlayer.isPlaying) {
            binding.playing = true
            mediaPlayer.pause()
        }
    }

    private fun stop(song: Song) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        super.onDestroy()
    }

}// Required empty public constructor
