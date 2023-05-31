package com.mobg5.g56080.foodee.util.chain.link

import android.util.Log
import com.mobg5.g56080.foodee.input.Checker
import com.mobg5.g56080.foodee.input.InputChecker
import com.mobg5.g56080.foodee.fragment.login.UserForm

class NameChainLink: ChainLinkAdapter<UserForm>() {

    override fun next(value: UserForm): ChainLink<UserForm> {
        Log.i("Hello", "in name chain link")
        val checker: InputChecker = Checker.create("name")
        return if(checker.check(value.name)) nextLink else FailureChainLink()
    }
}
