package com.mobg5.g56080.foodee.util.chain

import android.util.Log
import com.mobg5.g56080.foodee.util.chain.link.ChainLink
import com.mobg5.g56080.foodee.util.chain.link.EOCL

abstract class ChainAdapter<T>(protected val value: T): Chain<T> {

    abstract val chain: ChainLink<T>
    private var isSuccess: Boolean = true
    private lateinit var successCallback: () -> Unit
    private lateinit var failureCallback: (failure: ChainLink<T>) -> Unit
    private lateinit var endingLink: ChainLink<T>

    override fun run(): Chain<T>{
        var previousChainLink: ChainLink<T> = chain
        var currentChainLink: ChainLink<T> = previousChainLink
        while(currentChainLink !is EOCL){
            previousChainLink = currentChainLink
            currentChainLink = currentChainLink.next(value)
        }

        isSuccess = currentChainLink.asBool()
        if(!isSuccess) failureCallback(previousChainLink) else successCallback()

        return this
    }

    override fun onSuccess(callback: () -> Unit): Chain<T> {
        successCallback = callback
        return this
    }

    override fun onFailure(callback: (failure: ChainLink<T>) -> Unit): Chain<T> {
        failureCallback = callback
        return this
    }
}
