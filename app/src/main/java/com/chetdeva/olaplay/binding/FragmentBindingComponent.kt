package com.chetdeva.olaplay.binding

import android.databinding.DataBindingComponent
import android.support.v4.app.Fragment

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 17/12/17
 */

public class FragmentBindingComponent constructor(private val fragment: Fragment) : DataBindingComponent {

    val adapter: FragmentBindingAdapters by lazy { FragmentBindingAdapters(fragment) }

    override fun getFragmentBindingAdapters(): FragmentBindingAdapters = adapter
}