package com.example.booknest.data

data class HotelData(
    val name : String = "",
    val city : String="",
    val about : String="",
    val imageUrl : String="",
    val amenities : MutableList<String> = mutableListOf(),
    val roomTypes : MutableList<RoomType> = mutableListOf(),
    val contact : String = ""
)
