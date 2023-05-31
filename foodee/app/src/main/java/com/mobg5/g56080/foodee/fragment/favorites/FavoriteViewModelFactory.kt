package com.mobg5.g56080.foodee.fragment.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mobg5.g56080.foodee.fragment.home.HomeViewModel

class FavoriteViewModelFactory: ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            return FavoriteViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
