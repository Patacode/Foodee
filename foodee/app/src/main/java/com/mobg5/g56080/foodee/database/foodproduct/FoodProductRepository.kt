package com.mobg5.g56080.foodee.database.foodproduct

import android.util.Log
import androidx.lifecycle.LiveData
import com.mobg5.g56080.foodee.database.Repository
import com.mobg5.g56080.foodee.network.FoodProductApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodProductRepository(foodProductDao: FoodProductDAO): Repository<FoodProductDAO, FoodProductDatabase>(foodProductDao){

    suspend fun get(barcode: String): FoodProductDatabase{
        var result: FoodProductDatabase? = dao.select(barcode)
        if(result == null){
            result = withContext(Dispatchers.IO){
                val v = FoodProductApi.retrofitService.getProductInfo(barcode)
                v.asDatabaseModel()
            }
            add(result)
        }

        return result
    }

    fun getAll(): LiveData<List<FoodProductDatabase>> = dao.selectAll()
}