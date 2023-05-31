package com.mobg5.g56080.foodee.input

class InputNameChecker: InputChecker() {
    override val pattern: String = "^\\p{Alnum}{3,}$"
}
