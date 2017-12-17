package com.chetdeva.olaplay

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.chetdeva.olaplay.navigation.NavigationController
import com.chetdeva.olaplay.ui.SplashFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var navigator: NavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            navigator.toAdd(SplashFragment())
        }
    }

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return injector
    }
}
