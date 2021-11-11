package com.ibnu.jelajahin.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _bindingMainActivity: ActivityMainBinding? = null
    private val binding get() = _bindingMainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        _bindingMainActivity = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_bindingMainActivity?.root)

        binding?.bottomNav?.setupWithNavController(findNavController(R.id.nav_host_fragment))
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment || destination.id == R.id.eventFragment || destination.id == R.id.discoverFragment || destination.id == R.id.savedFragment || destination.id == R.id.profileFragment) {
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