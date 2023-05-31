package com.mobg5.g56080.foodee.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface DAO<T> {

    @Insert
    suspend fun insert(value: T): Long

    @Delete
    suspend fun delete(value: T)

    @Update
    suspend fun set(value: T)
}