package com.mobg5.g56080.foodee.fragment.login

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.androidadvance.topsnackbar.TSnackbar
import com.mobg5.g56080.foodee.MainActivity
import com.mobg5.g56080.foodee.R
import com.mobg5.g56080.foodee.databinding.FragmentLoginBinding
import com.mobg5.g56080.foodee.util.Utils
import com.mobg5.g56080.foodee.util.status.Email
import com.mobg5.g56080.foodee.util.status.Password
import com.mobg5.g56080.foodee.util.status.Status

class LoginFragment: Fragment(){

    private lateinit var binding: FragmentLoginBinding

    private lateinit var parentActivity: MainActivity
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        setupBinding(inflater)
        setupActivity()
        setupViewModel()
        setObservers()

        return binding.root
    }

    private fun setupBinding(inflater: LayoutInflater){
        binding = FragmentLoginBinding.inflate(inflater)
    }

    private fun setupActivity(){
        parentActivity = requireActivity() as MainActivity
    }

    private fun setupViewModel(){
        val viewModelFactory = LoginViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setObservers(){
        viewModel.onShowSnackbar.observe(viewLifecycleOwner){ pair ->
            displaySnackbar(pair.first, pair.second)
        }

        viewModel.onShowFirebaseSnackbar.observe(viewLifecycleOwner){ status ->
            displaySnackbar(status.customMessage!!, status.state.color)
        }

        viewModel.onMoveToSignupPage.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
                viewModel.doneMovingToSignupPage()
            }
        }

        viewModel.onMoveToHomePage.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                viewModel.doneMovingToHomePage()
            }
        }

        viewModel.onChangeFabIcon.observe(viewLifecycleOwner){
            val newImageDrawable: Drawable? = ResourcesCompat.getDrawable(resources, it, parentActivity.theme)
            parentActivity.binding.fab.setImageDrawable(newImageDrawable)
        }
    }

    private fun displaySnackbar(message: String, color: Int) =
        Utils.displaySnackbar(binding.loginLayout, message, resources, color, requireContext().theme)
}
