package com.mobg5.g56080.foodee.util.chain.link

import com.mobg5.g56080.foodee.input.Checker
import com.mobg5.g56080.foodee.input.InputChecker
import com.mobg5.g56080.foodee.fragment.login.UserForm

class PasswordChainLink: ChainLinkAdapter<UserForm>() {

    override fun next(value: UserForm): ChainLink<UserForm> {
        val checker: InputChecker = Checker.create("password")
        return if(checker.check(value.password)) nextLink else FailureChainLink()
    }
}