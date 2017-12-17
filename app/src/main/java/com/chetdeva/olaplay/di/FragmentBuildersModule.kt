package com.chetdeva.olaplay.di

import com.chetdeva.olaplay.ui.PlaylistFragment
import com.chetdeva.olaplay.ui.SplashFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    internal abstract fun contributeSplashFragment(): SplashFragment

    @ContributesAndroidInjector
    internal abstract fun contributePlaylistFragment(): PlaylistFragment

}
