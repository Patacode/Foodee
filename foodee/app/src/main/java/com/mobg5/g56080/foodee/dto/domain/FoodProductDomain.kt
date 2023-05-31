package com.mobg5.g56080.foodee.dto.domain

import com.mobg5.g56080.foodee.fragment.foodproduct.FoodProductEntry

data class FoodProductDomain(
    val id: String,
    val detail: FoodProductDetailDomain?,
    val statusCode: Int,
){
    fun asEntryPairList(): List<FoodProductEntry>{
        return listOf(
            FoodProductEntry("Barcode", id),
            FoodProductEntry("Product name", detail?.name),
            FoodProductEntry("Brands", detail?.brands),
            FoodProductEntry("Quantity", detail?.quantity),
            FoodProductEntry("Categories", detail?.categories),
            FoodProductEntry("Labels", detail?.labels),
            FoodProductEntry("Stores", detail?.stores),
            FoodProductEntry("Countries where sold", detail?.countries),
            FoodProductEntry("Nutriscore grade", detail?.nutriscoreGrade),
            FoodProductEntry("Ingredients", detail?.ingredients),
        )
    }
}
