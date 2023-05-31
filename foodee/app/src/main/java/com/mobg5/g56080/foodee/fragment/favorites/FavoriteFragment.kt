package com.mobg5.g56080.foodee.fragment.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobg5.g56080.foodee.MainActivity
import com.mobg5.g56080.foodee.databinding.FragmentFavoritesBinding
import com.mobg5.g56080.foodee.util.Utils
import com.mobg5.g56080.foodee.util.status.Status

class FavoriteFragment: Fragment(){

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        setupBinding(inflater)
        setupViewModel()
        setupRecycleView()
        setupObservers()

        return binding.root
    }

    private fun setupBinding(inflater: LayoutInflater){
        binding = FragmentFavoritesBinding.inflate(inflater)
    }

    private fun setupViewModel(){
        val viewModelFactory = FavoriteViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[FavoriteViewModel::class.java]
        binding.favoriteViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupRecycleView(){
        binding.recyclerView.adapter = FavoriteAdapter(viewModel)
    }

    private fun setupObservers(){
        viewModel.onShowProduct.observe(viewLifecycleOwner){
            findNavController().navigate(FavoriteFragmentDirections.actionFavoriteFragmentToProductDetailFragment(it.barcode))
        }

        viewModel.onShowFirebaseSnackbar.observe(viewLifecycleOwner){ status ->
            displaySnackbar(status.customMessage!!, status.state.color)
        }

        viewModel.showFallbackMessage.observe(viewLifecycleOwner){
            binding.favFallbackText.visibility = it
        }
    }

    private fun displaySnackbar(message: String, color: Int) =
        Utils.displaySnackbar(binding.favoriteLayout, message, resources, color, requireContext().theme)
}
