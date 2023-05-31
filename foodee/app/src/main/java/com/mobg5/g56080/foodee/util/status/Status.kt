package com.mobg5.g56080.foodee.util.status

import com.mobg5.g56080.foodee.R
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

open class Status(val state: State) {

    private var _customMessage: String? = null
    val customMessage: String?
        get() = _customMessage

    class Default: Transformer(){
        override fun transform(state: State): String = state.message
    }

    enum class State(val message: String, val color: Int){
        VALID("Valid", R.color.success_color),
        INVALID("Invalid", R.color.failure_color),
        NEW("New", R.color.new_color),
        PENDING("", R.color.black_light_shadow)
    }

    companion object{
        operator fun get(state: State): Status {
            return Status(state)
        }
    }

    infix fun <T: Transformer> by(classType: KClass<T>): String{
        val status = classType.createInstance()
        return status.transform(state)
    }

    infix fun with(message: String?): Status{
        _customMessage = message
        return this
    }

    override fun hashCode(): Int = state.message.hashCode()
    override fun toString(): String = state.message
    override fun equals(other: Any?): Boolean = if(other is Status) other.state == state else false
}