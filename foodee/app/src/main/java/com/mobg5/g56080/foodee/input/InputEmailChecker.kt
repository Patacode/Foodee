package com.mobg5.g56080.foodee.input

import android.content.Context
import com.mobg5.g56080.foodee.database.ApplicationDatabase
import com.mobg5.g56080.foodee.input.InputChecker

class InputEmailChecker: InputChecker() {
    override val pattern: String = "^\\p{Alnum}+([\\x2E\\-_]\\p{Alnum}+)*@\\p{Alnum}+(-\\p{Alnum}+)*\\x2E\\p{Alpha}{2,}\$"
}