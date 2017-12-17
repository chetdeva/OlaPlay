package com.chetdeva.olaplay.di

import com.chetdeva.olaplay.BuildConfig
import com.chetdeva.olaplay.api.OlaPlayService
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
 * @author chetansachdeva on 16/12/17
 */

@Module
public class ApiModule {

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
}