package com.mobg5.g56080.foodee.util.decorator

// Simple implementation
class SimpleFragmentWrapper(fragmentId: Int): FragmentWrapper{
    override val destination: Int = fragmentId
}
