package com.mobg5.g56080.foodee.util.chain

import com.mobg5.g56080.foodee.util.chain.link.*

class DateChain(date: String): ChainAdapter<String>(date) {

    override val chain: ChainLink<String> = YearChainLink()

    init{
        chain + MonthChainLink() + DayChainLink() + /* EOCL */ SuccessChainLink()
    }
}