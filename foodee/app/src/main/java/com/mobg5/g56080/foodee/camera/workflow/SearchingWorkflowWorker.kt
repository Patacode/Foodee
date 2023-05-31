package com.mobg5.g56080.foodee.camera.workflow

import android.view.View
import com.mobg5.g56080.foodee.R

class SearchingWorkflowWorker: WorkflowWorker() {

    override fun launch() {
        super.launch()
        binding.overlay.bottomPromptChip.visibility = View.VISIBLE
        binding.overlay.bottomPromptChip.setText(R.string.prompt_searching)
        fragment.stopCameraPreview()
    }
}