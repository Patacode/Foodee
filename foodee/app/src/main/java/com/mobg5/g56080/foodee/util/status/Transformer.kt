package com.mobg5.g56080.foodee.util.status

abstract class Transformer {
    abstract fun transform(state: Status.State): String
}