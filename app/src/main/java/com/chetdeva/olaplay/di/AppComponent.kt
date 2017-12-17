package com.chetdeva.olaplay.di

import android.app.Application
import com.chetdeva.olaplay.OlaPlayApplication
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
        AppModule::class, ApiModule::class, MainActivityModule::class))
public interface AppComponent {

    fun inject(olaPlayApplication: OlaPlayApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: Application): Builder
        fun build(): AppComponent
    }
}