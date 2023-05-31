package com.mobg5.g56080.foodee.util.chain

import com.mobg5.g56080.foodee.util.chain.link.ChainLink

interface Chain<T>{
    fun run(): Chain<T>
    fun onSuccess(callback: () -> Unit): Chain<T>
    fun onFailure(callback: (failure: ChainLink<T>) -> Unit): Chain<T>
}
