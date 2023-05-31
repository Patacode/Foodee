package com.mobg5.g56080.foodee.fragment.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mobg5.g56080.foodee.databinding.FragmentAboutBinding

class AboutFragment: Fragment() {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setupBinding(inflater)
        return binding.root
    }

    private fun setupBinding(inflater: LayoutInflater){
        binding = FragmentAboutBinding.inflate(inflater)
    }
}