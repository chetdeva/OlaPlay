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
import com.chetdeva.olaplay.databinding.FragmentPlaylistBinding
import com.chetdeva.olaplay.di.Injectable
import com.chetdeva.olaplay.util.BindFragment
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class PlaylistFragment : Fragment(), Injectable {

    private val binding: FragmentPlaylistBinding by BindFragment(R.layout.fragment_playlist)
    private val bindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private val playlistAdapter: PlaylistAdapter by lazy { PlaylistAdapter(bindingComponent) }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val playlistModel = ViewModelProviders.of(this, viewModelFactory).get(PlaylistModel::class.java)

        binding.playlist.adapter = playlistAdapter

        playlistModel.songs.observe(this, Observer {
            playlistAdapter.replace(it)
            binding.executePendingBindings()
        })
    }
}
