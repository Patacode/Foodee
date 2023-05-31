package com.mobg5.g56080.foodee.camera.workflow

import android.view.View

class SearchedWorkflowWorker: WorkflowWorker() {

    override fun launch() {
        super.launch()
        binding.overlay.bottomPromptChip.visibility = View.GONE
        fragment.stopCameraPreview()
    }
}