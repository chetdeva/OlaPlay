package com.chetdeva.olaplay.rx

import io.reactivex.Scheduler

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 17/12/17
 */
interface SchedulerProvider {
    fun computation(): Scheduler
    fun io(): Scheduler
    fun ui(): Scheduler
}