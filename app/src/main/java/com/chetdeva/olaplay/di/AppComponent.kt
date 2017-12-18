package com.chetdeva.olaplay.di

import android.app.Application
import com.chetdeva.olaplay.OlaPlayApplication
import com.chetdeva.olaplay.data.OlaPlayService
import com.chetdeva.olaplay.service.PlayerService
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
        DataModule::class))
interface AppComponent {

    fun inject(olaPlayApplication: OlaPlayApplication)
    fun inject(playService: PlayerService)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}