package com.chetdeva.olaplay.ui


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.chetdeva.olaplay.R
import com.chetdeva.olaplay.databinding.FragmentSplashBinding
import com.chetdeva.olaplay.di.Injectable
import com.chetdeva.olaplay.navigation.NavigationController
import com.chetdeva.olaplay.util.BindFragment
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : Fragment(), Injectable {

    private val binding: FragmentSplashBinding by BindFragment(R.layout.fragment_splash)

    @Inject
    lateinit var navigator: NavigationController

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Handler().postDelayed({ navigator.toReplace(PlaylistFragment()) }, 2000)
    }
}
