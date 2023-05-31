package com.mobg5.g56080.foodee.util.chain.link

import android.util.Log
import com.mobg5.g56080.foodee.input.Checker
import com.mobg5.g56080.foodee.input.InputChecker
import com.mobg5.g56080.foodee.fragment.login.UserForm

class EmailChainLink: ChainLinkAdapter<UserForm>() {

    override fun next(value: UserForm): ChainLink<UserForm> {
        val checker: InputChecker = Checker.create("email")
        Log.i("hello", "hh")
        return if(checker.check(value.email)) nextLink else FailureChainLink()
    }
}