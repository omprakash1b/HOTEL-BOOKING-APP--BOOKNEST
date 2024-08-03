package com.example.booknest.network

import com.example.booknest.data.BestPlaces
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL="https://training-uploads.internshala.com"


//creating retrofit api

private val retrofit = Retrofit.Builder()
    .addConverterFactory(
        Json.asConverterFactory(
            "application/json".toMediaType()
        )
    ).baseUrl(BASE_URL).build()

interface ApiService{
    @GET("android/hotelbooking/places.json")
    suspend fun getBestPlaces():List<BestPlaces>
}

object AppApi {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}