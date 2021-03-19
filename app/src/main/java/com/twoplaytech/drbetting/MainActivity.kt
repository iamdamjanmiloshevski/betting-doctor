package com.twoplaytech.drbetting

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.util.getSportColor
import com.twoplaytech.drbetting.util.getSportResource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var navView: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_football,
                R.id.navigation_basketball,
                R.id.navigation_tennis,
                R.id.navigation_handball,
                R.id.navigation_volleyball
            )
        )
        navView?.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_football -> changeTheme(Sport.FOOTBALL)
                R.id.navigation_basketball -> changeTheme(Sport.BASKETBALL)
                R.id.navigation_tennis -> changeTheme(Sport.TENNIS)
                R.id.navigation_handball -> changeTheme(Sport.HANDBALL)
                R.id.navigation_volleyball -> changeTheme(Sport.VOLLEYBALL)
            }
        }
    }

    private fun changeTheme(sport: Sport) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        window.statusBarColor = ContextCompat.getColor(this, sport.getSportColor())
        navView?.setBackgroundResource(sport.getSportResource())
    }
}