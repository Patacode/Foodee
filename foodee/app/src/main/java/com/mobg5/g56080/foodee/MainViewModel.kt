package com.mobg5.g56080.foodee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mobg5.g56080.foodee.util.decorator.FragmentWrapper
import com.mobg5.g56080.foodee.util.decorator.RequireLoginDecorator
import com.mobg5.g56080.foodee.util.decorator.SimpleFragmentWrapper

class MainViewModel: ViewModel(), FirebaseAuth.AuthStateListener{

    private val _moveToHomePage = MutableLiveData<Boolean>()
    val moveToHomePage: LiveData<Boolean>
        get() = _moveToHomePage

    private val _changeFabIcon = MutableLiveData<Int>()
    val changeFabIcon: LiveData<Int>
        get() = _changeFabIcon

    private val _openCamera = MutableLiveData<Boolean>()
    val openCamera: LiveData<Boolean>
        get() = _openCamera

    private val _changeLoginIcon = MutableLiveData<Int>()
    val changeLoginIcon: LiveData<Int>
        get() = _changeLoginIcon

    // false for the default action, true otherwise
    // default: open camera
    // otherwise: move to home
    private var _switchFabAction: Boolean = false
    private val navigationMap = HashMap<Int, FragmentWrapper>()

    init{
        navigationMap[R.id.login] = RequireLoginDecorator(SimpleFragmentWrapper(R.id.userFragment))
        navigationMap[R.id.about] = SimpleFragmentWrapper(R.id.aboutFragment)
        navigationMap[R.id.favorite] = RequireLoginDecorator(SimpleFragmentWrapper(R.id.favoriteFragment))
        navigationMap[R.id.fridge] = RequireLoginDecorator(SimpleFragmentWrapper(R.id.fridgeFragment))
    }

    fun onFabAction(){
        if(_switchFabAction){
            _moveToHomePage.value = true
        } else {
            _openCamera.value = true
        }
    }

    override fun onAuthStateChanged(mAuth: FirebaseAuth){
        if(mAuth.currentUser != null){ // user is logged
            _changeLoginIcon.value = R.drawable.person_fill
        } else{
            _changeLoginIcon.value = R.drawable.ic_baseline_login_24
        }
    }

    fun doneMovingToHomePage(){
        _moveToHomePage.value = false
        _changeFabIcon.value = R.drawable.ic_baseline_camera_alt_24
        _switchFabAction = false
    }

    fun doneMovingOutOfHomePage(){
        _changeFabIcon.value = R.drawable.ic_baseline_home_24
        _switchFabAction = true // fab go to home
    }

    fun doneOpeningCamera(){
        _openCamera.value = false
        _changeFabIcon.value = R.drawable.ic_baseline_home_24
        _switchFabAction = true
    }

    fun destinationTo(itemId: Int): Int?{
        return navigationMap[itemId]?.destination
    }
}
