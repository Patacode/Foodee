package com.mobg5.g56080.foodee.fragment.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobg5.g56080.foodee.fragment.login.UserForm
import com.mobg5.g56080.foodee.util.status.Status

class UserViewModel: ViewModel() {

    private val _currentUserForm = MutableLiveData<UserForm>()
    val currentUserForm: LiveData<UserForm>
        get() = _currentUserForm

    private val _onUserSignOut = MutableLiveData<Boolean>()
    val onUserSignOut: LiveData<Boolean>
        get() = _onUserSignOut

    private val _onMoveToHomePage = MutableLiveData<Boolean>()
    val onMoveToHomePage: LiveData<Boolean>
        get() = _onMoveToHomePage

    private val _onChangeFabIcon = MutableLiveData<Boolean>()
    val onChangeFabIcon: LiveData<Boolean>
        get() = _onChangeFabIcon

    private val _onShowFirebaseSnackbar = MutableLiveData<Status>()
    val onShowFirebaseSnackbar: LiveData<Status>
        get() = _onShowFirebaseSnackbar

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        val currentUserUID: String? =  mAuth.currentUser?.uid
        val currentUserEmail: String? = mAuth.currentUser?.email

        currentUserUID?.let {
            Firebase
                .firestore
                .collection("users")
                .document(currentUserUID)
                .get()
                .addOnSuccessListener{
                    _currentUserForm.value = UserForm(
                        (it["name"] as String?).toString(),
                        currentUserEmail.toString()
                    )
                }
        }
    }

    fun onUserSignOut(){
        _onUserSignOut.value = true
    }

    fun onShowFirebaseSnackbar(message: String, state: Status.State){
        _onShowFirebaseSnackbar.value = Status[state] with message
    }

    fun doneUserSignOut(){
        _onUserSignOut.value = false
        _onMoveToHomePage.value = true
        _onChangeFabIcon.value = true
        onShowFirebaseSnackbar("Successfully signed out !", Status.State.VALID)
    }

    fun doneMovingToHomePage(){
        _onMoveToHomePage.value = false
    }

    fun doneChangingFabIcon(){
        _onChangeFabIcon.value = false
    }
}
