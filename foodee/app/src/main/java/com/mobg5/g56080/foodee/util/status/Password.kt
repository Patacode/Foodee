package com.mobg5.g56080.foodee.util.status

class Password: Transformer() {
    override fun transform(state: Status.State): String {
        return when(state){
            Status.State.VALID -> "Password is valid"
            Status.State.INVALID -> "Password is not valid, must be of length 5 minimum"
            Status.State.NEW -> "Password is valid and new"
            Status.State.PENDING -> ""
        }
    }
}