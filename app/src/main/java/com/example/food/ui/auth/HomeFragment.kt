package com.example.food.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.food.MainActivity
import com.example.food.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val childNavHost =
            childFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
        val navController = childNavHost.navController

        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)

        (activity as? MainActivity)?.registerBottomNav(bottomNav)
    }
}
