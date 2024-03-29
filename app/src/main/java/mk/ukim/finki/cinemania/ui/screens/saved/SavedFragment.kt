package mk.ukim.finki.cinemania.ui.screens.saved

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.cinemania.R
import mk.ukim.finki.cinemania.databinding.FragmentSavedBinding
import mk.ukim.finki.cinemania.extensions.viewBinding
import mk.ukim.finki.cinemania.ui.shared.LoadingDialog

@AndroidEntryPoint
class SavedFragment : Fragment(R.layout.fragment_saved) {

    private val binding by viewBinding(FragmentSavedBinding::bind)

    private val viewModel by viewModels<SavedViewModel>()

    private var loadingDialog: LoadingDialog? = null
}

