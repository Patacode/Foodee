package com.mobg5.g56080.foodee.input

abstract class InputChecker{

    protected abstract val pattern: String

    open fun check(input: String): Boolean{
        val regex: Regex = pattern.toRegex(RegexOption.IGNORE_CASE)
        return regex.matches(input)
    }
}
