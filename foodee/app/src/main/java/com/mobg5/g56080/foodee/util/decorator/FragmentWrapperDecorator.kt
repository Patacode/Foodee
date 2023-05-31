package com.mobg5.g56080.foodee.util.decorator

// Simple decorator implementation
open class FragmentWrapperDecorator(protected val fragmentWrapper: FragmentWrapper): FragmentWrapper{
    override val destination: Int
        get() = fragmentWrapper.destination
}