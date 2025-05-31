package com.example.food

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    // Référence au BottomNavigationView (sera initialisée dans HomeFragment.onViewCreated)
    private var bottomNavView: BottomNavigationView? = null

    /**
     * Permet à HomeFragment de passer le BottomNavigationView pour qu’on puisse gérer le badge.
     * À appeler depuis HomeFragment.onViewCreated(...) juste après bottom_nav.setupWithNavController(...)
     */
    fun registerBottomNav(bottomNav: BottomNavigationView) {
        bottomNavView = bottomNav
    }

    /**
     * Met à jour le badge numérique de l’icône panier.
     * Si count = 0, supprime le badge.
     */
    fun updateCartBadge(count: Int) {
        bottomNavView?.let { nav ->
            // On cible l’item cartFragment (ID du menu BottomNavigation)
            val badge: BadgeDrawable = nav.getOrCreateBadge(R.id.cartFragment)
            if (count > 0) {
                badge.isVisible = true
                badge.number = count
            } else {
                badge.isVisible = false
                badge.clearNumber()
            }
        }
    }
}
