package com.chetdeva.olaplay.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.chetdeva.olaplay.notification.OlaNotificationManager
import com.chetdeva.olaplay.rx.SchedulerProvider
import com.chetdeva.olaplay.rx.SchedulerProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.chetdeva.olaplay.util.BuildInfoHelper


/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideBuildInfo(application: Application) =
            BuildInfoHelper.getBuildInfo(application)

    @Singleton
    @Provides
    fun provideSchedulers(providerImpl: SchedulerProviderImpl): SchedulerProvider =
            providerImpl

    @Provides
    @Singleton
    fun provideConnectivityManager(application: Application) =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideOlaNotificationManager(application: Application) =
            OlaNotificationManager(application)


}