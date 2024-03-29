package mk.ukim.finki.cinemania.ui.screens.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.cinemania.R
import mk.ukim.finki.cinemania.databinding.FragmentProfileBinding
import mk.ukim.finki.cinemania.extensions.viewBinding
import mk.ukim.finki.cinemania.ui.shared.LoadingDialog

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)

    private val viewModel by viewModels<ProfileViewModel>()

    private var loadingDialog: LoadingDialog? = null
}
