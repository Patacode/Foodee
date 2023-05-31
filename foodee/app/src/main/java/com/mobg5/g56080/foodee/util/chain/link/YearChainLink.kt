package com.mobg5.g56080.foodee.util.chain.link

import android.util.Log
import com.mobg5.g56080.foodee.input.Checker
import com.mobg5.g56080.foodee.input.InputChecker

class YearChainLink: ChainLinkAdapter<String>() {

    override fun next(value: String): ChainLink<String> {
        val checker: InputChecker = Checker.create("year")
        return if(checker.check(value)) nextLink else FailureChainLink()
    }
}