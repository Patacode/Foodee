package com.mobg5.g56080.foodee.camera

import android.util.Log
import androidx.lifecycle.*
import com.mobg5.g56080.foodee.dto.network.FoodProductNetwork
import com.mobg5.g56080.foodee.network.FoodProductApi
import kotlinx.coroutines.launch

class CameraViewModel(val cameraSource: CameraSource): ViewModel() {

    companion object{
        private const val TAG = "CameraViewModel"
    }

    private val _onCloseButtonClicked = MutableLiveData<Boolean>()
    val onCloseButtonClicked: LiveData<Boolean>
        get() = _onCloseButtonClicked

    private val _onFlashButtonClicked = MutableLiveData<Boolean>()
    val onFlashButtonClicked: LiveData<Boolean>
        get() = _onFlashButtonClicked

    private val _onChangeFabIcon = MutableLiveData<Boolean>()
    val onChangeFabIcon: LiveData<Boolean>
        get() = _onChangeFabIcon

    override fun onCleared() {
        super.onCleared()
        releaseCamera()
    }

    fun updateFlash(mode: String){
        cameraSource.updateFlashMode(mode)
    }

    fun updateFrameProcessor(frameProcessor: FrameProcessor){
        cameraSource.setFrameProcessor(frameProcessor)
    }

    fun releaseCamera(){
        cameraSource.release()
    }

    fun onClose(){
        _onCloseButtonClicked.value = true
        onChangeFabIcon()
    }

    fun onFlash(){
        _onFlashButtonClicked.value = true
    }

    fun onChangeFabIcon(){
        _onChangeFabIcon.value = true
    }

    fun doneClosing(){
        _onCloseButtonClicked.value = false
    }

    fun doneFlashing(){
        _onFlashButtonClicked.value = false
    }

    fun doneChangingFabIcon(){
        _onChangeFabIcon.value = false
    }
}