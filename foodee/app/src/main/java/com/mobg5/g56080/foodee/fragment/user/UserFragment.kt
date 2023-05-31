package com.mobg5.g56080.foodee.fragment.user

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.androidadvance.topsnackbar.TSnackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobg5.g56080.foodee.MainActivity
import com.mobg5.g56080.foodee.R
import com.mobg5.g56080.foodee.databinding.FragmentUserBinding
import com.mobg5.g56080.foodee.fragment.signup.SignupViewModel
import com.mobg5.g56080.foodee.fragment.signup.SignupViewModelFactory
import com.mobg5.g56080.foodee.util.Utils
import com.mobg5.g56080.foodee.util.status.Password
import com.mobg5.g56080.foodee.util.status.Status

class UserFragment: Fragment(){

    private lateinit var binding: FragmentUserBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var parentActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{

        setupBinding(inflater)
        setupActivity()
        setupViewModel()
        setupObservers()

        return binding.root
    }

    private fun setupBinding(inflater: LayoutInflater){
        binding = FragmentUserBinding.inflate(inflater)
    }

    private fun setupActivity(){
        parentActivity = requireActivity() as MainActivity
    }

    private fun setupViewModel(){
        val viewModelFactory = UserViewModelFactory()

        viewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]
        binding.userViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupObservers(){
        viewModel.onUserSignOut.observe(viewLifecycleOwner){
            if(it){
                FirebaseAuth.getInstance().signOut()
                viewModel.doneUserSignOut()
            }
        }

        viewModel.onMoveToHomePage.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(UserFragmentDirections.actionUserFragmentToHomeFragment())
                viewModel.doneMovingToHomePage()
            }
        }

        viewModel.onChangeFabIcon.observe(viewLifecycleOwner){
            if(it){
                val newImageDrawable: Drawable? = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_baseline_camera_alt_24,
                    parentActivity.theme)

                parentActivity.binding.fab.setImageDrawable(newImageDrawable)
                viewModel.doneChangingFabIcon()
            }
        }

        viewModel.onShowFirebaseSnackbar.observe(viewLifecycleOwner){status ->
            displaySnackbar(status.customMessage!!, status.state.color)
        }
    }

    private fun displaySnackbar(message: String, color: Int) =
        Utils.displaySnackbar(binding.userLayout, message, resources, color, requireContext().theme)
}