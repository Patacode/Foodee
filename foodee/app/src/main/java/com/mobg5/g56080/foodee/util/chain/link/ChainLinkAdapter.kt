package com.mobg5.g56080.foodee.util.chain.link

abstract class ChainLinkAdapter<T>: ChainLink<T> {

    protected lateinit var nextLink: ChainLink<T>

    override fun plus(chainLink: ChainLink<T>): ChainLink<T>{
        nextLink = chainLink
        return chainLink
    }
}
