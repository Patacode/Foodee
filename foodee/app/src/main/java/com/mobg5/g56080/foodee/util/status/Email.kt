package com.mobg5.g56080.foodee.util.status

class Email: Transformer() {
    override fun transform(state: Status.State): String {
        return when(state){
            Status.State.VALID -> "Email is valid"
            Status.State.INVALID -> "Email is not valid"
            Status.State.NEW -> "Email is valid and new"
            Status.State.PENDING -> ""
        }
    }
}