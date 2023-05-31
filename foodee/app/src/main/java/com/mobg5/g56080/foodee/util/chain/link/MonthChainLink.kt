package com.mobg5.g56080.foodee.util.chain.link

import com.mobg5.g56080.foodee.input.Checker
import com.mobg5.g56080.foodee.input.InputChecker

class MonthChainLink: ChainLinkAdapter<String>() {

    override fun next(value: String): ChainLink<String> {
        val checker: InputChecker = Checker.create("month")
        return if(checker.check(value)) nextLink else FailureChainLink()
    }
}