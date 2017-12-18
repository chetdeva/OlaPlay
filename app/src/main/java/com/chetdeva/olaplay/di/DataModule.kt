package com.chetdeva.olaplay.di

import android.app.Application
import android.arch.persistence.room.Room
import com.chetdeva.olaplay.BuildConfig
import com.chetdeva.olaplay.data.OlaPlayService
import com.chetdeva.olaplay.data.OlaPlayDb
import com.chetdeva.olaplay.data.SongsDao
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 18/12/17
 */

@Module(includes = arrayOf(ViewModelModule::class))
class DataModule {

    // API

    @Singleton @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Singleton @Provides
    fun provideHttpClient(interceptor: HttpLoggingInterceptor) : OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Singleton @Provides
    fun provideOlaPlayService(httpClient: OkHttpClient): OlaPlayService {
        return Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(OlaPlayService::class.java)
    }

    // Room DB

    @Singleton
    @Provides
    fun provideDb(app: Application): OlaPlayDb {
        return Room.databaseBuilder(app, OlaPlayDb::class.java, "olaplay.db").allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun provideSongDao(db: OlaPlayDb): SongsDao {
        return db.songsDao()
    }
}