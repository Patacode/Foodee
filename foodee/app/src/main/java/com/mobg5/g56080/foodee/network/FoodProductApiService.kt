package com.mobg5.g56080.foodee.network

import com.mobg5.g56080.foodee.dto.network.FoodProductNetwork
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://world.openfoodfacts.org"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface FoodProductApiService {
    @GET("/api/v0/product/{barcode}.json")
    suspend fun getProductInfo(@Path("barcode") barcode: String): FoodProductNetwork
}

object FoodProductApi {
    val retrofitService : FoodProductApiService by lazy {
        retrofit.create(FoodProductApiService::class.java)
    }
}
