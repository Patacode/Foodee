package com.mobg5.g56080.foodee.util.chain

import com.mobg5.g56080.foodee.fragment.login.UserForm
import com.mobg5.g56080.foodee.util.chain.link.*

class LoginChain(value: UserForm): ChainAdapter<UserForm>(value) {

    override val chain: ChainLink<UserForm> = EmailChainLink()

    init{
        chain + PasswordChainLink() + /* EOCL */ SuccessChainLink()
    }
}