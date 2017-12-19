package com.chetdeva.olaplay.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.chetdeva.olaplay.arch.PlaylistModel
import com.chetdeva.olaplay.arch.OlaPlayViewModelFactory
import com.chetdeva.olaplay.arch.PlayerModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlaylistModel::class)
    internal abstract fun bindPlaylistModel(playlistModel: PlaylistModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayerModel::class)
    internal abstract fun bindPlayerModel(playerModel: PlayerModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: OlaPlayViewModelFactory): ViewModelProvider.Factory
}