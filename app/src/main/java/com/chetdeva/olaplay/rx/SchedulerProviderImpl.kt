package com.chetdeva.olaplay.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 17/12/17
 */
class SchedulerProviderImpl @Inject constructor(): SchedulerProvider {
    override fun computation(): Scheduler = Schedulers.computation()
    override fun io(): Scheduler = Schedulers.io()
    override fun ui(): Scheduler = AndroidSchedulers.mainThread()
}