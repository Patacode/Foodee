package com.mobg5.g56080.foodee.util.chain.link

class SuccessChainLink<T>: EOCL<T>() {
    override fun asBool(): Boolean {
        return true
    }
}
