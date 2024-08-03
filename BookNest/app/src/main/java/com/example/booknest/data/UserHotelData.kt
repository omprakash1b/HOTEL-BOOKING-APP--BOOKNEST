package com.example.booknest.data

data class UserHotelData(
    val hotelName:String="",
    val city:String="",
    val image :String="",
    var roomsBooked:List<RoomType> = emptyList()
)
