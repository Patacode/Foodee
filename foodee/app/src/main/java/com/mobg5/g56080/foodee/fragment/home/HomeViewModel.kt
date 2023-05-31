package com.mobg5.g56080.foodee.fragment.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobg5.g56080.foodee.fragment.login.UserForm

class HomeViewModel: ViewModel() {

    private val _currentUserForm = MutableLiveData<UserForm>()
    val currentUserForm: LiveData<UserForm>
        get() = _currentUserForm

    private val _showGreetings = MutableLiveData<Int>()
    val showGreetings: LiveData<Int>
        get() = _showGreetings

    init {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null){
            _showGreetings.value = View.VISIBLE
            Firebase.firestore
                .collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    _currentUserForm.value = UserForm(
                        name = (document["name"] as String?).toString(),
                    )
                }
        } else{
            _showGreetings.value = View.GONE
        }
    }
}