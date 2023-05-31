package com.mobg5.g56080.foodee.database.foodproduct

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mobg5.g56080.foodee.dto.domain.FoodProductDetailDomain
import com.mobg5.g56080.foodee.dto.domain.FoodProductDomain

@Entity(tableName = "food_products", indices = [Index(value = ["barcode"], unique = true)])
data class FoodProductDatabase(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    @ColumnInfo(name = "barcode") val barcode: String = "",
    @ColumnInfo(name = "status_code") val statusCode: Int = 0,
    @ColumnInfo(name = "product_name") val name: String? = null,
    @ColumnInfo(name = "image_url") val imageUrl: String? = null,
    @ColumnInfo(name = "brands") val brands: String? = null,
    @ColumnInfo(name = "quantity") val quantity: String? = null,
    @ColumnInfo(name = "categories") val categories: String? = null,
    @ColumnInfo(name = "labels") val labels: String? = null,
    @ColumnInfo(name = "stores") val stores: String? = null,
    @ColumnInfo(name = "countries") val countries: String? = null,
    @ColumnInfo(name = "nutriscore_grade") val nutriscoreGrade: String? = null,
    @ColumnInfo(name = "ingredients") val ingredients: String? = null,
){
    fun asDomainModel(): FoodProductDomain{
        return FoodProductDomain(
            id = barcode,
            detail = FoodProductDetailDomain(
                name = name,
                image = imageUrl,
                brands = brands,
                quantity = quantity,
                categories = categories,
                labels = labels,
                stores = stores,
                countries = countries,
                nutriscoreGrade = nutriscoreGrade,
                ingredients = ingredients,
            ),
            statusCode = statusCode,
        )
    }
}
