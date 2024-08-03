package com.example.booknest.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.booknest.R
import com.example.booknest.data.HotelData
import com.example.booknest.data.Rules

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelectRoomScreen(appViewModel: AppViewModel,roomUiState: AppViewModel.RoomUiState,navHostController: NavHostController){

    var data = HotelData("","","","", mutableListOf(), mutableListOf(),"")
    when(roomUiState){
        is AppViewModel.RoomUiState.Success ->{
            data = roomUiState.hotelDetails
        }
        else ->{
            navHostController.navigate(route = BookNestScreen.ErrorScreen.name)
        }

    }
    
    val seeMore by appViewModel.seeMoreClicked.collectAsState()
    val userRoomsBooKed by appViewModel.userRoomsBooked.collectAsState()
    val checkInDate by appViewModel.checkInDate.collectAsState()
    val checkOutDate by appViewModel.checkOutDate.collectAsState()
    val bookingPrice by appViewModel.bookingPrice.collectAsState()
    val noOfDays by appViewModel.noOfDays.collectAsState()


    LazyColumn(
        modifier = Modifier.background(Color.White)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(bottomEnd = 50.dp)
                ) {
                    AsyncImage(model = data.imageUrl, contentDescription = data.name)
                }
                Text(
                    text = "${data.name}, ${data.city}",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(10.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color(46,167,240,255)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                repeat(times = 5) {x->

                        if (x<4) {
                            Icon(imageVector = Icons.Filled.Star, contentDescription ="Rating", modifier = Modifier.padding(2.dp),
                                    tint = Color.Black
                                )
                        }else{
                            Icon(imageVector = Icons.Outlined.Star, tint = Color.Gray, contentDescription = "Rating", modifier = Modifier.padding(2.dp))
                        }

                    }
                    Text(text = "4.3", fontSize = 18.sp, fontFamily = FontFamily.Serif, textAlign = TextAlign.Center, modifier = Modifier.padding(start = 10.dp),
                            fontWeight = FontWeight.Bold, color = Color.Black
                        )
                }
                Text(text = "About the Hotel", fontWeight = FontWeight.Bold,
                        color =Color(46,167,240,255),
                    modifier = Modifier.padding(start = 10.dp, top = 20.dp, bottom = 20.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 20.sp
                )




                if (seeMore) {

                        Text(text = data.about, fontSize = 13.sp, modifier = Modifier.padding(start = 10.dp, end = 10.dp), color = Color.Black)
                        Text(text = "See Less", fontSize = 13.sp, modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .clickable {
                                appViewModel.toggleSeeMore()
                            },
                            color =Color(46,167,240,255)
                        )

                }else{
                    Text(text = data.about.substring(0,200), fontSize = 13.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                    Text(text = "See More", fontSize = 13.sp, modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .clickable {
                            appViewModel.toggleSeeMore()
                        },
                        color =Color(46,167,240,255)
                    )
                }

                Box (
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .border(width = 1.dp, color = Color.Gray)

                    ) {}
                }
                
                Text(
                    text = "Amenities Available",
                    fontSize = 20.sp,
                    color = Color(46, 167, 245, 255),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(10.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for(amenity in data.amenities) {
                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {
                            when (amenity.lowercase().trim()) {
                            "spa" -> {
                                Image(
                                    painter = painterResource(id = R.drawable.spa),
                                    contentDescription = "Spa",
                                    modifier = Modifier.size(50.dp)
                                )
                            }

                            "gym" -> {
                                Image(
                                    painter = painterResource(id = R.drawable.gym),
                                    contentDescription = "gym",
                                    modifier = Modifier.size(50.dp)
                                )
                            }

                            "wifi" -> {
                                Image(
                                    painter = painterResource(id = R.drawable.wifi),
                                    contentDescription = "wifi",
                                    modifier = Modifier.size(50.dp)
                                )
                            }

                            "dining" -> {
                                Image(
                                    painter = painterResource(id = R.drawable.dining),
                                    contentDescription = "Dining",
                                    modifier = Modifier.size(50.dp)
                                )
                            }

                            else -> {
                                Text(text = "Something went wrong!!!")
                            }
                        }
                            Text(text = amenity, fontSize = 14.sp, color = Color.Black)
                        }
                    }
                }

                Box (
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .border(width = 1.dp, color = Color.Gray)

                    ) {}
                }
                
                Text(text = "Property Rules & Information",
                    color = Color(46, 167, 245, 255),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp),
                    fontSize = 20.sp
                    )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Rules.getRules().forEach { 
                        Text(text = "\u2022$it", color = Color.Black)
                    }
                }
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 20.dp),
                    horizontalAlignment = Alignment.Start
                ){
                    Text(text = "Select Room(s)", fontSize = 20.sp,
                        color = Color(47,167,245,255),
                        fontWeight = FontWeight.Bold
                        )
                }



            }

        }
        items(data.roomTypes){

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Card (
                    modifier = Modifier.fillMaxWidth()
                ){
                    AsyncImage(model = it.imageUrl, contentDescription = it.name)
                }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    Column (
                        modifier = Modifier
                            .width(200.dp)
                            .height(60.dp)
                            .padding(5.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ){
                       Text(text = it.name,color = Color.Black, fontWeight = FontWeight.Bold)
                       Text(text = "Rs. ${it.price}", color = Color.Black)
                    }

                    if (it in userRoomsBooKed){

                        Button(onClick = {
                                appViewModel.removeRoom(it)
                        },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonColors(containerColor = Color(45, 167, 245, 105), contentColor = Color(45,167,245,175), disabledContentColor = Color.Gray, disabledContainerColor = Color.White),
                            modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    color = Color(45, 167, 245, 175),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .height(40.dp)
                                .fillMaxWidth()
                        ) {
                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ){
                                Icon(imageVector = Icons.Filled.Check,
                                    contentDescription = "Selected",
                                    tint = Color(45, 167, 245, 175),
                                    modifier = Modifier.padding(end = 5.dp)
                                    )
                                Text(text = "SELECTED")
                            }
                        }
                    }
                    else{

                        Button(onClick = {
                            appViewModel.selectRoom(it)
                        },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonColors(containerColor = Color.White, contentColor = Color(45,167,245,175), disabledContentColor = Color.Gray, disabledContainerColor = Color.White),
                            modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    color = Color(45, 167, 245, 175),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .height(40.dp)
                                .fillMaxWidth()

                        ) {
                            Text(text = "SELECT")
                        }
                    }

                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {

            }
        }

    }

    if (userRoomsBooKed.isNotEmpty()) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .height(60.dp)
                ,
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(46, 127, 245, 255))
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${userRoomsBooKed.size}Rooms | $noOfDays Days | Rs.$bookingPrice",
                        color = Color.White
                    )
                    Button(onClick = {
                        navHostController.navigate(route = BookNestScreen.Checkout.name)
                        appViewModel.userHotelBooked(hotelName = data.name, city = data.city, image = data.imageUrl)
                    },
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonColors(
                            containerColor = Color(223,223,225,210),
                            contentColor = Color.Black,
                            disabledContentColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent
                        )
                    ) {
                        Text(text = "CheckOut")
                    }
                }
            }
        }
    }

}