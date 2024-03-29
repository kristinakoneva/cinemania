package mk.ukim.finki.cinemania.ui.screens.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.cinemania.databinding.ActivityAuthBinding
import mk.ukim.finki.cinemania.extensions.viewBinding

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityAuthBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
