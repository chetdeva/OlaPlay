package com.chetdeva.olaplay.di

import android.app.Application
import com.chetdeva.olaplay.OlaPlayApplication
import com.chetdeva.olaplay.download.OlaDownloadManager
import com.chetdeva.olaplay.download.OlaFileProvider
import com.chetdeva.olaplay.player.OlaPlayerService
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class,
        AppModule::class, MainActivityModule::class,
        PlayerModule::class, DataModule::class))
interface AppComponent {

    fun inject(olaPlayApplication: OlaPlayApplication)
    fun inject(playServiceOla: OlaPlayerService)

    fun olaFileProvider(): OlaFileProvider
    fun olaDownloadManager(): OlaDownloadManager

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}