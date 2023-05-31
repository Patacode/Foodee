package com.mobg5.g56080.foodee.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobg5.g56080.foodee.database.foodproduct.FoodProductDAO
import com.mobg5.g56080.foodee.database.foodproduct.FoodProductDatabase

@Database(entities = [FoodProductDatabase::class], version = 8, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ApplicationDatabase: RoomDatabase() {

    abstract val foodProductDao: FoodProductDAO

    companion object{
        private lateinit var INSTANCE: ApplicationDatabase
        fun getInstance(context: Context): ApplicationDatabase{
            synchronized(this){
                if(!this::INSTANCE.isInitialized)
                    INSTANCE = Room
                        .databaseBuilder(context.applicationContext, ApplicationDatabase::class.java, "foodee_database")
                        .fallbackToDestructiveMigration()
                        .build()

                return INSTANCE
            }
        }
    }
}