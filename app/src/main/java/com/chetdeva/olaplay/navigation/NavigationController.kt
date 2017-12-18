package com.chetdeva.olaplay.navigation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.chetdeva.olaplay.MainActivity
import com.chetdeva.olaplay.R
import com.chetdeva.olaplay.ui.PlaylistFragment
import com.chetdeva.olaplay.ui.SplashFragment
import com.chetdeva.olaplay.util.add
import com.chetdeva.olaplay.util.replace
import javax.inject.Inject

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

class NavigationController
@Inject constructor(mainActivity: MainActivity) {

    private val containerId = R.id.container
    private val fragmentManager: FragmentManager by lazy { mainActivity.supportFragmentManager }

    fun toAdd(fragment: Fragment, bundle: Bundle?) {
        fragmentManager.add(containerId, fragment, false, bundle)
    }

    fun toReplace(fragment: Fragment, bundle: Bundle?) {
        fragmentManager.replace(containerId, fragment, false, bundle)
    }

    fun toReplacePush(fragment: Fragment, bundle: Bundle?) {
        fragmentManager.replace(containerId, fragment, true, bundle)
    }
}