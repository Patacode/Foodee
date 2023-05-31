package com.mobg5.g56080.foodee.fragment.signup

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mobg5.g56080.foodee.MainActivity
import com.mobg5.g56080.foodee.databinding.FragmentSignupBinding
import com.mobg5.g56080.foodee.util.Utils
import com.mobg5.g56080.foodee.util.status.Email
import com.mobg5.g56080.foodee.util.status.Name
import com.mobg5.g56080.foodee.util.status.Password

class SignupFragment: Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var parentActivity: MainActivity
    private lateinit var viewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        setupBinding(inflater)
        setupActivity()
        setupViewModel()
        setObservers()

        return binding.root
    }

    private fun setupBinding(inflater: LayoutInflater){
        binding = FragmentSignupBinding.inflate(inflater)
    }

    private fun setupActivity(){
        parentActivity = requireActivity() as MainActivity
    }

    private fun setupViewModel(){
        val viewModelFactory = SignupViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[SignupViewModel::class.java]
        binding.signupViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setObservers(){
        viewModel.onShowSnackbar.observe(viewLifecycleOwner){ pair ->
            displaySnackbar(pair.first, pair.second)
        }

        viewModel.onShowFirebaseSnackbar.observe(viewLifecycleOwner){ status ->
            displaySnackbar(status.customMessage!!, status.state.color)
        }

        viewModel.onMoveToLoginPage.observe(viewLifecycleOwner) {
            if(it){
                findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
                viewModel.doneMovingToLoginPage()
            }
        }

        viewModel.onMoveToHomePage.observe(viewLifecycleOwner) {
            if(it){
                findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToHomeFragment())
                viewModel.doneMovingToHomePage()
            }
        }

        viewModel.onChangeFabIcon.observe(viewLifecycleOwner){
            val newImageDrawable: Drawable? = ResourcesCompat.getDrawable(resources, it, parentActivity.theme)
            parentActivity.binding.fab.setImageDrawable(newImageDrawable)
        }
    }

    private fun displaySnackbar(message: String, color: Int) =
        Utils.displaySnackbar(binding.signupLayout, message, resources, color, requireContext().theme)
}
