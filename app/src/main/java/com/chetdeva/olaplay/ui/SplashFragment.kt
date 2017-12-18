package com.chetdeva.olaplay.ui


import android.databinding.DataBindingComponent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.chetdeva.olaplay.R
import com.chetdeva.olaplay.binding.FragmentDataBindingComponent
import com.chetdeva.olaplay.databinding.FragmentSplashBinding
import com.chetdeva.olaplay.di.Injectable
import com.chetdeva.olaplay.navigation.NavigationController
import com.chetdeva.olaplay.util.BindFragment
import com.chetdeva.olaplay.util.SPLASH_DELAY_IN_MILLIS
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : Fragment(), Injectable {

    private val bindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private val binding: FragmentSplashBinding by BindFragment(R.layout.fragment_splash, bindingComponent)

    @Inject lateinit var navigate: NavigationController

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Handler(Looper.getMainLooper())
                .postDelayed({ navigate.toReplace(PlaylistFragment(), null) }, SPLASH_DELAY_IN_MILLIS)
    }
}
