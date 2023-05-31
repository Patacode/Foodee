package com.mobg5.g56080.foodee.database

abstract class Repository<T: DAO<V>, V>(protected val dao: T) {
    open suspend fun add(value: V): Long = dao.insert(value)
    open suspend fun remove(value: V): Unit = dao.delete(value)
    open suspend fun update(value: V): Unit = dao.set(value)
}
