package com.mobg5.g56080.foodee.util.decorator

import com.google.firebase.auth.FirebaseAuth
import com.mobg5.g56080.foodee.R

// Concrete decorator
class RequireLoginDecorator(fragmentWrapper: FragmentWrapper): FragmentWrapperDecorator(fragmentWrapper){
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override val destination: Int
        get() = if(mAuth.currentUser != null) fragmentWrapper.destination else R.id.loginFragment
}