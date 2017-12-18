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
import com.chetdeva.olaplay.data.Song
import com.chetdeva.olaplay.binding.FragmentDataBindingComponent
import com.chetdeva.olaplay.databinding.FragmentPlaylistBinding
import com.chetdeva.olaplay.di.Injectable
import com.chetdeva.olaplay.navigation.NavigationController
import com.chetdeva.olaplay.util.BindFragment
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class PlaylistFragment : Fragment(), Injectable, PlaylistAdapter.SongClickCallback {

    private val bindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private val binding: FragmentPlaylistBinding by BindFragment(R.layout.fragment_playlist, bindingComponent)
    private lateinit var playlistAdapter: PlaylistAdapter

    @Inject lateinit var navigate: NavigationController
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val playlistModel = ViewModelProviders.of(this, viewModelFactory).get(PlaylistModel::class.java)

        playlistAdapter = PlaylistAdapter(bindingComponent, this)
        binding.playlist.adapter = playlistAdapter

        playlistModel.songs.observe(this, Observer {
            playlistAdapter.replace(it)
            binding.executePendingBindings()
        })
    }

    override fun onClick(song: Song) {
        navigate.toReplacePush(PlayerFragment(), getBundle(song))
    }

    private fun getBundle(song: Song): Bundle {
        val bundle = Bundle()
        bundle.putString("song_url", song.url)
        return bundle
    }
}
