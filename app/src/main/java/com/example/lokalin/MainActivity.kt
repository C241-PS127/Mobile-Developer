package com.example.lokalin

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.lokalin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNav()
    }

    private fun setupNav() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        findViewById<BottomNavigationView>(R.id.nav_view)
            .setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> hideBottomNav()
                R.id.detailProductFragment -> hideBottomNav()
                R.id.signUpFragment2 -> hideBottomNav()
                R.id.shopFragment -> hideBottomNav()
                R.id.addProductFragment -> hideBottomNav()
                R.id.searchFragment -> hideBottomNav()
                R.id.myProductFragment -> hideBottomNav()

                else -> showBottomNav()
            }
        }
    }

    private fun showBottomNav() {
         val navView: BottomNavigationView = binding.navView
        navView.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        val navView: BottomNavigationView = binding.navView
        navView.visibility = View.GONE

    }


}
