package com.mobg5.g56080.foodee.dto.network

import com.mobg5.g56080.foodee.database.foodproduct.FoodProductDatabase
import com.mobg5.g56080.foodee.dto.domain.FoodProductDomain
import com.squareup.moshi.Json

data class FoodProductNetwork(
    @Json(name = "code") val id: String,
    @Json(name = "product") val detail: FoodProductDetailNetwork?,
    @Json(name = "status") val statusCode: Int,
    @Json(name = "status_verbose") val statusVerbose: String,
){
    fun asDomainModel(): FoodProductDomain{
        return FoodProductDomain(
            id = id,
            detail = detail?.asDomainModel(),
            statusCode = statusCode,
        )
    }

    fun asDatabaseModel(): FoodProductDatabase{
        return FoodProductDatabase(
            barcode = id,
            statusCode = statusCode,
            name = detail?.name,
            imageUrl = detail?.image,
            brands = detail?.brands,
            quantity = detail?.quantity,
            categories = detail?.categories,
            labels = detail?.labels,
            stores = detail?.stores,
            countries = detail?.countries,
            nutriscoreGrade = detail?.nutriscoreGrade,
            ingredients = detail?.ingredients,
        )
    }
}
