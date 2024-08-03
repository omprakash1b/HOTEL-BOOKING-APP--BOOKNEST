package com.example.booknest.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.booknest.data.HotelData

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelectHotel(appViewModel: AppViewModel,navHostController: NavHostController){
    val hotelList by appViewModel.hotelList.collectAsState()
    val appUiState by appViewModel.uiState.collectAsState()
    val hotelListCity = hotelList.filter {
        it.city.lowercase() == appUiState.selectedObject.lowercase()
    }

    val noOfRooms by appViewModel.noOfRooms.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
        .background(Color.White)
    ) {

        items(hotelListCity){
            var max = 0
            var min = Int.MAX_VALUE
            var totalRooms = 0
            for (rooms in it.roomTypes){
                max  = maxOf(max,rooms.price)
                min = minOf(min,rooms.price)
                totalRooms+=rooms.noOfRooms
            }

//            val available = it.roomTypes.any { type ->
//                type.noOfRooms >= noOfRooms
//            }
            val available = totalRooms>=noOfRooms
            HotelCard(image = it.imageUrl, name = it.name,
                price = if (available) "$min - $max" else "Sold Out"
                , appViewModel = appViewModel, hotelData = it, navHostController = navHostController)
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HotelCard(image:String,name:String,price:String,appViewModel: AppViewModel,hotelData: HotelData,navHostController: NavHostController){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                appViewModel.setHotelForRoomScreen(hotelData)
                navHostController.navigate(route = BookNestScreen.SelectRoomScreen.name)
            }
    ) {

            AsyncImage(
                model = image.trim(),
                contentDescription = name
            )


        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.LightGray)
        ){
            Text(text = name, modifier = Modifier.padding(10.dp), color = Color.Gray)

            val cardColor = if (price=="Sold Out") Color.Magenta else Color(46,167,245,255)

            Card(
                shape = RoundedCornerShape(bottomStart = 5.dp),
                colors = CardDefaults.cardColors(cardColor),
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(text = price, modifier = Modifier.padding(10.dp))
            }
        }
    }
}