package com.mobg5.g56080.foodee.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.mobg5.g56080.foodee.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        setupBinding(inflater)
        setupViewModel()
        setupObservers()

        return binding.root
    }

    private fun setupBinding(inflater: LayoutInflater){
        binding = FragmentHomeBinding.inflate(inflater)
    }

    private fun setupViewModel(){
        val viewModelFactory = HomeViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        binding.homeViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupObservers(){
        viewModel.showGreetings.observe(viewLifecycleOwner){
            binding.helloText.visibility = it
        }
    }
}