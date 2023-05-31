package com.mobg5.g56080.foodee.input

class InputYearChecker: InputChecker() {

    override val pattern: String = "^\\p{Digit}+$"

    override fun check(input: String): Boolean{
        val datePart = input.split("/")
        val regex: Regex = pattern.toRegex(RegexOption.IGNORE_CASE)
        return datePart.size == 3 && regex.matches(datePart[2]) && datePart[2].toInt() >= 2000
    }
}