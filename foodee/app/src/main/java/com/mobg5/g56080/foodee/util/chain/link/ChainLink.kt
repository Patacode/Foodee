package com.mobg5.g56080.foodee.util.chain.link

interface ChainLink<T> {
    fun next(value: T): ChainLink<T>
    operator fun plus(chainLink: ChainLink<T>): ChainLink<T>
}
