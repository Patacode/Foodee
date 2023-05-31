package com.mobg5.g56080.foodee.input

import java.time.YearMonth

class InputDayChecker: InputChecker() {

    override val pattern: String = "^\\p{Digit}+$"

    override fun check(input: String): Boolean{
        val datePart = input.split("/")
        val regex: Regex = pattern.toRegex(RegexOption.IGNORE_CASE)
        return datePart.size == 3 &&
                regex.matches(datePart[2]) &&
                regex.matches(datePart[1]) &&
                regex.matches(datePart[0]) &&
                datePart[0].toInt() > 0 &&
                datePart[0].toInt() <= YearMonth.of(datePart[2].toInt(), datePart[1].toInt()).lengthOfMonth()
    }
}