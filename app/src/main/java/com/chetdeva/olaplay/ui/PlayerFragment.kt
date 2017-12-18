package com.chetdeva.olaplay.ui


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingComponent
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
import javax.inject.Inject
import android.content.Intent
import com.chetdeva.olaplay.service.PlayerService
import com.chetdeva.olaplay.util.*


/**
 * A simple [Fragment] subclass.
 */
class PlayerFragment : Fragment(), Injectable, PlayerCallback {

    private val bindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private val binding: FragmentPlayerBinding by BindFragment(R.layout.fragment_player, bindingComponent)

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var mediaPlayer: OlaMediaPlayer

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.callback = this

        val playlistModel = ViewModelProviders.of(this, viewModelFactory).get(PlaylistModel::class.java)

        playlistModel.getSong(getSongUrl(arguments))
                .observe(this, Observer {
                    it?.let {
                        binding.song = it
                        if (mediaPlayer.urlPlaying == it.url) {
                            binding.playing = mediaPlayer.isPlaying
                        } else {
                            mediaPlayer.stop()
                            mediaPlayer.reset()
                            playOrPause(it)
                        }
                    }
                })
    }

    private fun getSongUrl(arguments: Bundle) = arguments.getString(SONG_URL)

    override fun playOrPause(song: Song) {
        if (!mediaPlayer.isPlaying) {   // play
            binding.playing = true
            val intent = Intent(activity, PlayerService::class.java)
            intent.action = PLAY_ACTION
            intent.putExtra(SONG_URL, song.url)
            activity.startService(intent)
        } else {                        // pause
            binding.playing = false
            val intent = Intent(activity, PlayerService::class.java)
            intent.action = PAUSE_ACTION
            intent.putExtra(SONG_URL, song.url)
            activity.startService(intent)
        }
    }

}
