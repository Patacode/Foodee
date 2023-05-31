package com.mobg5.g56080.foodee.camera.workflow

import com.mobg5.g56080.foodee.camera.CameraFragment
import com.mobg5.g56080.foodee.databinding.FragmentCameraBinding

abstract class WorkflowWorker {

    protected lateinit var fragment: CameraFragment
    protected lateinit var binding: FragmentCameraBinding

    fun with(fragment: CameraFragment, binding: FragmentCameraBinding): WorkflowWorker{
        this.fragment = fragment
        this.binding = binding
        return this
    }

    open fun launch(){
        if(!::fragment.isInitialized || !::binding.isInitialized)
            throw IllegalStateException("Invalid state")
    }
}