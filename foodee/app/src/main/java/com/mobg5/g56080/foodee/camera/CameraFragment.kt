package com.mobg5.g56080.foodee.camera

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.graphics.drawable.Drawable
import android.hardware.Camera
import android.hardware.Camera.Parameters
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mobg5.g56080.foodee.MainActivity
import com.mobg5.g56080.foodee.R
import com.mobg5.g56080.foodee.camera.barcode.BarcodeProcessor
import com.mobg5.g56080.foodee.camera.workflow.WorkflowViewModel
import com.mobg5.g56080.foodee.camera.workflow.WorkflowViewModelFactory
import com.mobg5.g56080.foodee.camera.workflow.WorkflowWorkerFactory
import com.mobg5.g56080.foodee.databinding.FragmentCameraBinding
import java.io.IOException

class CameraFragment: Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private lateinit var viewModel: CameraViewModel
    private lateinit var workflowViewModel: WorkflowViewModel
    private lateinit var currentWorkflowState: WorkflowViewModel.WorkflowState
    private lateinit var promptChipAnimator: AnimatorSet
    private lateinit var parentActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        setupBinding(inflater)
        setupActivity()
        setupAnimator()
        setupViewModel()
        setupObservers()
        setUpWorkflowModel()

        return binding.root
    }

    private fun setupBinding(inflater: LayoutInflater){
        binding = FragmentCameraBinding.inflate(inflater)
    }

    private fun setupActivity(){
        parentActivity = requireActivity() as MainActivity
    }

    private fun setupAnimator(){
        val animator = AnimatorInflater.loadAnimator(context, R.animator.bottom_prompt_chip_enter)
        promptChipAnimator = (animator as AnimatorSet).apply {
            setTarget(binding.overlay.bottomPromptChip)
        }
    }

    private fun setupViewModel(){
        val cameraSource = CameraSource(binding.overlay.cameraPreviewGraphicOverlay)
        val viewModelFactory = CameraViewModelFactory(cameraSource)
        viewModel = ViewModelProvider(this, viewModelFactory)[CameraViewModel::class.java]
        binding.topActionBar.cameraViewModel = viewModel
    }

    private fun setupObservers(){
        viewModel.onCloseButtonClicked.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(CameraFragmentDirections.actionCameraFragmentToHomeFragment())
                val activity: MainActivity = requireActivity() as MainActivity
                activity.binding.bottomAppBar.performShow()
                activity.binding.fab.show()
                viewModel.doneClosing()
            }
        })

        viewModel.onFlashButtonClicked.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.topActionBar.flashButton.apply {
                    val mode = if(isSelected) Parameters.FLASH_MODE_OFF
                    else Parameters.FLASH_MODE_TORCH
                    viewModel.updateFlash(mode)
                    isSelected = !isSelected
                }
                viewModel.doneFlashing()
            }
        })

        viewModel.onChangeFabIcon.observe(viewLifecycleOwner){
            if(it){
                val newImageDrawable: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_camera_alt_24, parentActivity.theme)
                parentActivity.binding.fab.setImageDrawable(newImageDrawable)
                viewModel.doneChangingFabIcon()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val barcodeProcessor = BarcodeProcessor(binding.overlay.cameraPreviewGraphicOverlay, workflowViewModel)
        workflowViewModel.markCameraFrozen()
        currentWorkflowState = WorkflowViewModel.WorkflowState.NOT_STARTED
        viewModel.updateFrameProcessor(barcodeProcessor)
        workflowViewModel.setWorkflowState(WorkflowViewModel.WorkflowState.DETECTING)
    }

    override fun onPause() {
        super.onPause()
        currentWorkflowState = WorkflowViewModel.WorkflowState.NOT_STARTED
        stopCameraPreview()
    }

    internal fun startCameraPreview() {
        if(!workflowViewModel.isCameraLive){
            try{
                workflowViewModel.markCameraLive()
                binding.cameraPreview.start(viewModel.cameraSource)
            } catch(e: IOException) {
                viewModel.releaseCamera()
            }
        }
    }

    internal fun stopCameraPreview() {
        if (workflowViewModel.isCameraLive) {
            workflowViewModel.markCameraFrozen()
            binding.topActionBar.flashButton.isSelected = false
            binding.cameraPreview.stop()
        }
    }

    private fun setUpWorkflowModel(){
        val viewModelFactory = WorkflowViewModelFactory(requireActivity().application)
        workflowViewModel = ViewModelProvider(this, viewModelFactory)[WorkflowViewModel::class.java]

        // Observes the workflow state changes, if happens, update the overlay view indicators and
        // camera preview state.
        workflowViewModel.workflowState.observe(viewLifecycleOwner, Observer { workflowState ->
            if(::currentWorkflowState.isInitialized && currentWorkflowState == workflowState) return@Observer

            currentWorkflowState = workflowState

            val wasPromptChipGone = binding.overlay.bottomPromptChip.visibility == View.GONE
            WorkflowWorkerFactory
                .consume(workflowState)
                .with(this, binding)
                .launch()

            val shouldPlayPromptChipEnteringAnimation = wasPromptChipGone && binding.overlay.bottomPromptChip.visibility == View.VISIBLE
            promptChipAnimator.let {
                if(shouldPlayPromptChipEnteringAnimation && !it.isRunning) it.start()
            }
        })

        workflowViewModel.detectedBarcode.observe(viewLifecycleOwner, Observer { barcode ->
            if (barcode != null) {
                barcode.rawValue?.let {
                    val activity: MainActivity = requireActivity() as MainActivity
                    activity.binding.bottomAppBar.performShow()
                    activity.binding.fab.show()
                    findNavController().navigate(CameraFragmentDirections.actionCameraFragmentToProductDetailFragment(it))
                }
            }
        })
    }
}