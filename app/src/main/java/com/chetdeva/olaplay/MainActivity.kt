package com.chetdeva.olaplay

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.chetdeva.olaplay.navigation.NavigationController
import com.chetdeva.olaplay.ui.PlayerFragment
import com.chetdeva.olaplay.ui.PlaylistFragment
import com.chetdeva.olaplay.ui.SplashFragment
import com.chetdeva.olaplay.util.NAVIGATE_TO
import com.chetdeva.olaplay.util.NAVIGATE_TO_PLAYER
import com.chetdeva.olaplay.util.SONG_URL
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var navigate: NavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isFromNotification()) {
            if (intent.getStringExtra(NAVIGATE_TO) == NAVIGATE_TO_PLAYER) {
                navigate.toAdd(PlaylistFragment(), null)
                navigate.toReplacePush(PlayerFragment(), getBundle(intent.getStringExtra(SONG_URL)))
            }
            return
        }

        if (savedInstanceState == null) {
            navigate.toAdd(SplashFragment(), null)
        }
    }

    private fun isFromNotification() = intent?.let { intent.hasExtra(NAVIGATE_TO) } ?: false

    private fun getBundle(url: String): Bundle {
        val bundle = Bundle()
        bundle.putString(SONG_URL, url)
        return bundle
    }

    override fun supportFragmentInjector() = injector
}
