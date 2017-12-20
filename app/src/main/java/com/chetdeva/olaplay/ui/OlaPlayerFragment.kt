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
import com.chetdeva.olaplay.data.dto.Song
import com.chetdeva.olaplay.databinding.FragmentPlayerBinding
import com.chetdeva.olaplay.di.Injectable
import javax.inject.Inject
import android.content.Intent
import com.chetdeva.olaplay.arch.PlayerModel
import com.chetdeva.olaplay.player.OlaPlayerCallback
import com.chetdeva.olaplay.download.OlaDownloadState
import com.chetdeva.olaplay.player.OlaPlayer
import com.chetdeva.olaplay.player.OlaPlayerService
import com.chetdeva.olaplay.util.*
import io.reactivex.disposables.CompositeDisposable

/**
 * A simple [Fragment] subclass.
 */
class OlaPlayerFragment : Fragment(), Injectable, OlaPlayerCallback {

    private val bindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private val binding: FragmentPlayerBinding by BindFragment(R.layout.fragment_player, bindingComponent)
    private val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    private lateinit var playerModel: PlayerModel

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var player: OlaPlayer

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.callback = this

        playerModel = ViewModelProviders.of(this, viewModelFactory).get(PlayerModel::class.java)

        playerModel.getSong(getSongUrl(arguments)).observe(this,
                Observer { it?.let { init(it) } })

        subscribeToDownloadUpdates()
    }

    private fun subscribeToDownloadUpdates() {
        playerModel.olaDownloadState.observe(this,
                Observer {
                    when (it) {
                        OlaDownloadState.DOWNLOADING -> binding.downloading = true
                        OlaDownloadState.DOWNLOADED_ALREADY -> binding.downloading = false
                        OlaDownloadState.DOWNLOAD_COMPLETE -> {
                            binding.downloading = false
                            activity.showToast("Song downloaded successfully.")
                        }
                        else -> {
                        }
                    }
                })
    }


    private fun init(song: Song) {
        binding.song = song
        if (player.urlPlaying != song.url) {
            player.stopAndReset()
            playOrPause(song)
            load(song)
        } else {
            binding.playing = player.isPlaying
        }
    }

    private fun load(song: Song) {
        binding.loading = true
        disposables.add(player.readyStateDisposable {
            binding.loading = false
        })
    }

    private fun getSongUrl(arguments: Bundle) = arguments.getString(SONG_URL)

    override fun playOrPause(song: Song) {
        if (!player.isPlaying) {        // play
            play(song)
        } else {                        // pause
            pause(song)
        }
    }

    private fun pause(song: Song) {
        binding.playing = false
        activity.startService(getIntent(PAUSE_ACTION, song))
    }

    private fun play(song: Song) {
        binding.playing = true
        activity.startService(getIntent(PLAY_ACTION, song))
    }

    override fun stop(song: Song) {
        binding.playing = false
        activity.startService(getIntent(STOP_ACTION, song))
    }

    override fun download(song: Song) {
        playerModel.addToDownloadQueue(song.url, song.url, song.song.replace(" ", ""))
    }

    private fun getIntent(action: String, song: Song): Intent {
        val intent = Intent(activity, OlaPlayerService::class.java)
        intent.action = action
        intent.putExtra(SONG, song)
        return intent
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
