package com.mobg5.g56080.foodee.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CameraViewModelFactory(val cameraSource: CameraSource): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CameraViewModel::class.java)){
            return CameraViewModel(cameraSource) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
