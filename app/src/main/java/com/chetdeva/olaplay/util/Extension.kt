package com.chetdeva.olaplay.util

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.widget.Toast

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 16/12/17
 */

fun FragmentManager.replace(containerId: Int,
                            fragment: Fragment,
                            addToBackStack: Boolean,
                            bundle: Bundle?): FragmentTransaction {
    bundle?.let { fragment.arguments = bundle }
    val transaction = this.beginTransaction()
    transaction.replace(containerId, fragment)
    if (addToBackStack) transaction.addToBackStack(fragment::class.java.simpleName)
    transaction.commitAllowingStateLoss()
    return transaction
}

fun FragmentManager.add(containerId: Int,
                        fragment: Fragment,
                        addToBackStack: Boolean,
                        bundle: Bundle?): FragmentTransaction {
    bundle?.let { fragment.arguments = bundle }
    val transaction = this.beginTransaction()
    transaction.add(containerId, fragment)
    if (addToBackStack) transaction.addToBackStack(fragment::class.java.simpleName)
    transaction.commitAllowingStateLoss()
    return transaction
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}