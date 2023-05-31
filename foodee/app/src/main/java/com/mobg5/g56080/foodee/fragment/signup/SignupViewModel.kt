package com.mobg5.g56080.foodee.fragment.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobg5.g56080.foodee.R
import com.mobg5.g56080.foodee.fragment.login.UserForm
import com.mobg5.g56080.foodee.util.chain.SignupChain
import com.mobg5.g56080.foodee.util.chain.link.EmailChainLink
import com.mobg5.g56080.foodee.util.chain.link.NameChainLink
import com.mobg5.g56080.foodee.util.chain.link.PasswordChainLink
import com.mobg5.g56080.foodee.util.status.*
import com.mobg5.g56080.foodee.util.status.Status.State.INVALID
import com.mobg5.g56080.foodee.util.status.Status.State.VALID
import kotlinx.coroutines.launch

class SignupViewModel: ViewModel(){

    val userForm: MutableLiveData<UserForm> = MutableLiveData<UserForm>(UserForm())

    private val _onShowSnackbar = MutableLiveData<Pair<String, Int>>()
    val onShowSnackbar: LiveData<Pair<String, Int>>
        get() = _onShowSnackbar

    private val _onShowFirebaseSnackbar = MutableLiveData<Status>()
    val onShowFirebaseSnackbar: LiveData<Status>
        get() = _onShowFirebaseSnackbar

    private val _onMoveToLoginPage = MutableLiveData<Boolean>()
    val onMoveToLoginPage: LiveData<Boolean>
        get() = _onMoveToLoginPage

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
            SignupChain(userForm)
                .onSuccess{ createUser(userForm) }
                .onFailure{
                    when(it){
                        is NameChainLink -> _onShowSnackbar.value =
                            Pair(Status[INVALID] by Name::class, Status[INVALID].state.color)
                        is EmailChainLink -> _onShowSnackbar.value =
                            Pair(Status[INVALID] by Email::class, Status[INVALID].state.color)
                        is PasswordChainLink -> _onShowSnackbar.value =
                            Pair(Status[INVALID] by Password::class, Status[INVALID].state.color)
                    }
                }.run()
        }
    }

    fun onMoveToLoginPage(){
        _onMoveToLoginPage.value = true
    }

    fun onMoveToHomePage(){
        _onMoveToHomePage.value = true
    }

    fun onShowFirebaseSnackbar(message: String, state: Status.State){
        _onShowFirebaseSnackbar.value = Status[state] with message
    }

    fun onChangeFabIcon(iconId: Int){
        _onChangeFabIcon.value = iconId
    }

    fun doneMovingToLoginPage(){
        _onMoveToLoginPage.value = false
    }

    fun doneMovingToHomePage(){
        _onMoveToHomePage.value = false
    }

    private fun createUser(userForm: UserForm){
        mAuth
            .createUserWithEmailAndPassword(userForm.email, userForm.password)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    val firebaseUser = mAuth.currentUser
                    firebaseUser?.let {
                        Firebase
                            .firestore
                            .collection("users")
                            .document(it.uid)
                            .set(mapOf(
                                "name" to userForm.name,
                                "email" to userForm.email,
                            )).addOnSuccessListener {
                                val message = "Account successfully created. You are now logged in !"
                                onShowFirebaseSnackbar(message, VALID)
                                onChangeFabIcon(R.drawable.ic_baseline_camera_alt_24)
                                onMoveToHomePage()
                            }.addOnFailureListener{ exc ->
                                onShowFirebaseSnackbar(exc.message.toString(), INVALID)
                                it.delete()
                            }
                    }
                } else{
                    onShowFirebaseSnackbar(task.exception?.message.toString(), INVALID)
                }
            }
    }
}