package com.mobg5.g56080.foodee.camera.workflow

object WorkflowWorkerFactory {

    fun consume(workflowState: WorkflowViewModel.WorkflowState): WorkflowWorker{
        return when(workflowState){
            WorkflowViewModel.WorkflowState.DETECTING -> DetectingWorkflowWorker()
            WorkflowViewModel.WorkflowState.CONFIRMING -> ConfirmingWorkflowWorker()
            WorkflowViewModel.WorkflowState.SEARCHING -> SearchingWorkflowWorker()
            WorkflowViewModel.WorkflowState.DETECTED -> DetectedWorkflowWorker()
            WorkflowViewModel.WorkflowState.SEARCHED -> SearchedWorkflowWorker()
            else -> DefaultWorkflowWorker()
        }
    }
}