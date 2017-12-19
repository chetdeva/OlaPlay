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
import android.widget.SeekBar
import com.chetdeva.olaplay.arch.PlayerModel
import com.chetdeva.olaplay.core.PlayerCallback
import com.chetdeva.olaplay.download.SongDownloadState
import com.chetdeva.olaplay.player.OlaPlayer
import com.chetdeva.olaplay.player.OlaPlayerService
import com.chetdeva.olaplay.util.*
import io.reactivex.disposables.CompositeDisposable

/**
 * A simple [Fragment] subclass.
 */
class PlayerFragment : Fragment(), Injectable, PlayerCallback, SeekBar.OnSeekBarChangeListener {

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
        binding.seekBar.setOnSeekBarChangeListener(this)

        playerModel = ViewModelProviders.of(this, viewModelFactory).get(PlayerModel::class.java)

        playerModel.getSong(getSongUrl(arguments)).observe(this,
                Observer { it?.let { init(it) } })

        playerModel.songDownloadState.observe(this,
                Observer {
                    when (it) {
                        SongDownloadState.READY -> {
                        }
                        SongDownloadState.DOWNLOADING -> binding.downloading = true
                        SongDownloadState.DOWNLOADED -> {
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
        if (player.pathPlaying != song.url) {
            player.stopAndReset()
            playOrPause(song)
            loadAndSeek(song)
        } else {
            binding.playing = player.mp.isPlaying
        }
    }

    private fun loadAndSeek(song: Song) {
        binding.loading = true
        disposables.add(player.readyStateDisposable {
            binding.loading = false
            seek(song)
        })
    }

    private fun seek(song: Song) {
//        binding.seekBar.max = mp.duration
//        binding.seekBar.postDelayed({ binding.seekBar.progress = mp.currentPosition / 1000 }, 200)
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        player.seekTo(p1 * 1000)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar?) {

    }

    private fun getSongUrl(arguments: Bundle) = arguments.getString(SONG_URL)

    override fun playOrPause(song: Song) {
        if (!player.mp.isPlaying) {        // play
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
