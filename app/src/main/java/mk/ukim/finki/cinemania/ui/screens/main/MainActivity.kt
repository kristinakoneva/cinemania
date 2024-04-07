package mk.ukim.finki.cinemania.ui.screens.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.cinemania.R
import mk.ukim.finki.cinemania.databinding.ActivityMainBinding
import mk.ukim.finki.cinemania.extensions.viewBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initBottomNavigationView()
    }

    private fun initBottomNavigationView() = with(binding) {
        NavigationUI.setupWithNavController(
            bottomNavView,
            findNavController(
                this@MainActivity,
                R.id.mainNavHostFragment
            )
        )
    }
}
