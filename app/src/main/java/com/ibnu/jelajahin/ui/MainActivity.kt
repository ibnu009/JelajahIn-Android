package com.ibnu.jelajahin.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _bindingMainActivity: ActivityMainBinding? = null
    private val binding get() = _bindingMainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _bindingMainActivity = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_bindingMainActivity?.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.statusBarColor = ContextCompat.getColor(this, R.color.dark_green);

        val navHostBottomBar = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navControllerBottomBar = navHostBottomBar.navController

        binding?.bottomNav?.setupWithNavController(navControllerBottomBar)
        navControllerBottomBar.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment || destination.id == R.id.eventFragment || destination.id == R.id.savedFragment || destination.id == R.id.discoveryFragment || destination.id == R.id.profileFragment) {
                binding?.bottomNav?.visibility = View.VISIBLE
            } else {
                binding?.bottomNav?.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}