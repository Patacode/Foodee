package com.mobg5.g56080.foodee.fragment.foodproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobg5.g56080.foodee.database.foodproduct.FoodProductRepository

class FoodProductViewModelFactory(
    private val foodProductRepository: FoodProductRepository,
    private val barcode: String,
): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FoodProductViewModel::class.java)){
            return FoodProductViewModel(foodProductRepository, barcode) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
