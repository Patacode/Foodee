package com.mobg5.g56080.foodee.input

class InputMonthChecker: InputChecker() {

    override val pattern: String = "^\\p{Digit}+$"

    override fun check(input: String): Boolean{
        val datePart = input.split("/")
        val regex: Regex = pattern.toRegex(RegexOption.IGNORE_CASE)
        return datePart.size == 3 && regex.matches(datePart[1]) && datePart[1].toInt() > 0 && datePart[1].toInt() < 13
    }
}