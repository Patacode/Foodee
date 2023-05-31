package com.mobg5.g56080.foodee.input

// static factory
object Checker{

    fun create(factory: String): InputChecker{
        return when(factory.lowercase()){
            "email" -> InputEmailChecker()
            "password" -> InputPasswordChecker()
            "name" -> InputNameChecker()
            "year" -> InputYearChecker()
            "month" -> InputMonthChecker()
            "day" -> InputDayChecker()
            else -> InputNullChecker()
        }
    }
}