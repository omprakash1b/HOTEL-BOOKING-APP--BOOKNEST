package com.example.booknest.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serial

@Serializable
data class BestPlaces(
    @SerialName(value = "desc") val desc:String,
    @SerialName(value = "image") val imageUrl:String,
    @SerialName(value = "name") val name:String
)
