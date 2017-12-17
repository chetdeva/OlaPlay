package com.chetdeva.olaplay.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.chetdeva.olaplay.ui.PlaylistModel
import com.chetdeva.olaplay.util.OlaPlayViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlaylistModel::class)
    internal abstract fun bindPlaylistModel(playlistModel: PlaylistModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: OlaPlayViewModelFactory): ViewModelProvider.Factory
}