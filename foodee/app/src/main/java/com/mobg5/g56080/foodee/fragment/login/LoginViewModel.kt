package com.mobg5.g56080.foodee.fragment.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mobg5.g56080.foodee.R
import com.mobg5.g56080.foodee.util.chain.LoginChain
import com.mobg5.g56080.foodee.util.chain.link.EmailChainLink
import com.mobg5.g56080.foodee.util.chain.link.PasswordChainLink
import com.mobg5.g56080.foodee.util.status.Email
import com.mobg5.g56080.foodee.util.status.Password
import com.mobg5.g56080.foodee.util.status.Status
import com.mobg5.g56080.foodee.util.status.Status.State.INVALID
import com.mobg5.g56080.foodee.util.status.Status.State.VALID
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel(){

    val userForm: MutableLiveData<UserForm> = MutableLiveData<UserForm>(UserForm())

    private val _onShowSnackbar = MutableLiveData<Pair<String, Int>>()
    val onShowSnackbar: LiveData<Pair<String, Int>>
        get() = _onShowSnackbar

    private val _onShowFirebaseSnackbar = MutableLiveData<Status>()
    val onShowFirebaseSnackbar: LiveData<Status>
        get() = _onShowFirebaseSnackbar

    private val _onMoveToSignupPage = MutableLiveData<Boolean>()
    val onMoveToSignupPage: LiveData<Boolean>
        get() = _onMoveToSignupPage

    private val _onMoveToHomePage = MutableLiveData<Boolean>()
    val onMoveToHomePage: LiveData<Boolean>
        get() = _onMoveToHomePage

    private val _onChangeFabIcon = MutableLiveData<Int>()
    val onChangeFabIcon: LiveData<Int>
        get() = _onChangeFabIcon

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onCheckInput(){
        viewModelScope.launch {
            val userForm: UserForm = userForm.value ?: return@launch
            LoginChain(userForm)
                .onSuccess{ loginUser(userForm) }
                .onFailure{
                    when(it){
                        is EmailChainLink -> _onShowSnackbar.value =
                            Pair(Status[INVALID] by Email::class, Status[INVALID].state.color)
                        is PasswordChainLink -> _onShowSnackbar.value =
                            Pair(Status[INVALID] by Password::class, Status[INVALID].state.color)
                    }
                }.run()
        }
    }

    fun onMoveToSignupPage(){
        _onMoveToSignupPage.value = true
    }

    fun onMoveToHomePage(){
        _onMoveToHomePage.value = true
    }

    fun onChangeFabIcon(iconId: Int){
        _onChangeFabIcon.value = iconId
    }

    fun onShowFirebaseSnackbar(message: String, state: Status.State){
        _onShowFirebaseSnackbar.value = Status[state] with message
    }

    fun doneMovingToSignupPage(){
        _onMoveToSignupPage.value = false
    }

    fun doneMovingToHomePage(){
        _onMoveToHomePage.value = false
    }

    private fun loginUser(userForm: UserForm){
        mAuth
            .signInWithEmailAndPassword(userForm.email, userForm.password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val message = "Authentication successful. You are now logged in !"
                    onShowFirebaseSnackbar(message, VALID)
                    onChangeFabIcon(R.drawable.ic_baseline_camera_alt_24)
                    onMoveToHomePage()
                } else{
                    onShowFirebaseSnackbar(task.exception?.message.toString(), INVALID)
                }
            }
    }
}