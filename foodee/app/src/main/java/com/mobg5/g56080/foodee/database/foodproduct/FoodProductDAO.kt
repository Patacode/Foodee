package com.mobg5.g56080.foodee.database.foodproduct

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.mobg5.g56080.foodee.database.DAO

@Dao
interface FoodProductDAO: DAO<FoodProductDatabase>{

    @Query("select * from food_products where barcode = :barcode")
    suspend fun select(barcode: String): FoodProductDatabase?

    @Query("select * from food_products")
    fun selectAll(): LiveData<List<FoodProductDatabase>>
}
