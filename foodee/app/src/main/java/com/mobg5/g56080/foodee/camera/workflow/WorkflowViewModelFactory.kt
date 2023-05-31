package com.mobg5.g56080.foodee.camera.workflow

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WorkflowViewModelFactory(val application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WorkflowViewModel::class.java)){
            return WorkflowViewModel(application) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}