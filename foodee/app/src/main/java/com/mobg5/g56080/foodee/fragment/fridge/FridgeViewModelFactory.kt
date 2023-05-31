package com.mobg5.g56080.foodee.fragment.fridge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FridgeViewModelFactory: ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FridgeViewModel::class.java)){
            return FridgeViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}