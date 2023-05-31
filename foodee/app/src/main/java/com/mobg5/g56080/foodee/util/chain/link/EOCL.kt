package com.mobg5.g56080.foodee.util.chain.link

abstract class EOCL<T>: ChainLinkAdapter<T>() {
    override fun next(value: T): ChainLink<T> = this
    abstract fun asBool(): Boolean
}

