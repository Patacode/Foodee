package com.mobg5.g56080.foodee.dto.network

import com.mobg5.g56080.foodee.dto.domain.FoodProductDetailDomain
import com.squareup.moshi.Json

data class FoodProductDetailNetwork(
    @Json(name = "product_name") val name: String?,
    @Json(name = "image_url") val image: String?,
    @Json(name = "brands") val brands: String?,
    @Json(name = "quantity") val quantity: String?,
    @Json(name = "categories") val categories: String?,
    @Json(name = "labels") val labels: String?,
    @Json(name = "stores") val stores: String?,
    @Json(name = "countries") val countries: String?,
    @Json(name = "nutriscore_grade") val nutriscoreGrade: String?,
    @Json(name = "ingredients_text") val ingredients: String?,
){
    fun asDomainModel(): FoodProductDetailDomain{
        return FoodProductDetailDomain(
            name = name,
            image = image,
            brands = brands,
            quantity = quantity,
            categories = categories,
            labels = labels,
            stores = stores,
            countries = countries,
            nutriscoreGrade = nutriscoreGrade,
            ingredients = ingredients,
        )
    }
}
