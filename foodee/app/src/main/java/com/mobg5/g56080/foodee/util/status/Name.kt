package com.mobg5.g56080.foodee.util.status

class Name: Transformer() {
    override fun transform(state: Status.State): String {
        return when(state){
            Status.State.VALID -> "Name is valid"
            Status.State.INVALID -> "Name is not valid, must be alphanumeric of length 3 minimum"
            Status.State.NEW -> "Name is valid and new"
            Status.State.PENDING -> ""
        }
    }
}