package com.chetdeva.olaplay

import android.app.Activity
import android.app.Application
import com.chetdeva.olaplay.di.AppComponent
import com.chetdeva.olaplay.di.AppInjector
import com.chetdeva.olaplay.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 *
 * @author chetansachdeva on 16/12/17
 */

class OlaPlayApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Activity>

    val component : AppComponent by lazy { DaggerAppComponent.builder().application(this).build() }

    override fun onCreate() {
        super.onCreate()

        component.inject(this)
        AppInjector.init(this)
    }

    override fun activityInjector() = injector
}
