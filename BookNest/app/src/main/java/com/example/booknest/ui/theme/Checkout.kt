package com.example.booknest.ui.theme

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CheckOutScreen(appViewModel: AppViewModel,navHostController: NavHostController){

    val userHotelBooked by appViewModel.userHotelBooked.collectAsState()
    val checkin by appViewModel.checkInDate.collectAsState()
    val checkout by appViewModel.checkOutDate.collectAsState()
    val bookingPrice by appViewModel.bookingPrice.collectAsState()
    val noOfDays by appViewModel.noOfDays.collectAsState()

    val context = LocalContext.current

    if (userHotelBooked.roomsBooked.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.background(Color(245,245,245,255))
        ) {
                item{
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 2.dp),
                        //colors = CardDefaults.cardColors(Color.White),
                        shape = RoundedCornerShape(topStart = 10.dp,
                                topEnd = 10.dp
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier.width(200.dp),
                                    verticalArrangement = Arrangement.SpaceEvenly,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = userHotelBooked.hotelName + ",",
                                        color = Color.Black,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = userHotelBooked.city,
                                        color = Color.Black,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        repeat(5){
                                            val color = if (it<4) Color.Black else Color(223,223,225,210)
                                                Icon(imageVector = Icons.Filled.Star,
                                                    contentDescription = "",
                                                    tint = color
                                                    )

                                        }
                                        Text(text = "4.3",
                                            modifier = Modifier.padding(start = 4.dp),
                                            color = Color.Black
                                            )
                                    }
                                }
                                Card(
                                    modifier = Modifier.width(135.dp)
                                ) {
                                    AsyncImage(
                                        model = userHotelBooked.image,
                                        contentDescription = userHotelBooked.hotelName + "image"
                                    )
                                }
                            }
                        }
                    }
                }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 10.dp),
                    shape = RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp)
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = "Booking Details", fontSize = 24.sp,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(46,167,240,255))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(text = "CHECK-IN", color = Color.Black)
                                Text(text = checkin, fontWeight = FontWeight.Bold, color = Color.Black)
                            }

                            Column(
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.Bottom,
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Card(
                                        modifier = Modifier.padding(5.dp),
                                        colors = CardDefaults.cardColors(Color(223,223,225,210))
                                    ) {
                                        Text(
                                            text = "$noOfDays " + if (noOfDays > 1) "Days" else "Day",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(3.dp)
                                        )
                                    }
                                }
                            }

                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(text = "CHECK-OUT", color = Color.Black)
                                Text(text = checkout, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }
                    }
                }
            }

            items(userHotelBooked.roomsBooked){
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(model = it.imageUrl, contentDescription = it.name+" image")
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                        ,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Column(
                            modifier = Modifier
                                .width(200.dp)
                                .padding(5.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = it.name, color = Color.Black, fontWeight = FontWeight.Bold)
                            Text(text = it.price.toString(), color = Color.Black)
                        }
                        Button(onClick = {
                            appViewModel.removeRoom(it)
                            appViewModel.userHotelBooked(userHotelBooked.hotelName,
                                userHotelBooked.city,
                                userHotelBooked.image
                                )
                        },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonColors(
                                containerColor = Color(223,223,225,210),
                                contentColor = Color.Black,
                                disabledContentColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            )

                            ) {
                           Text(text = "Remove")
                        }
                    }
                }
            }

            item {

                Card (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),

                    colors = CardDefaults.cardColors(Color(223,223,225,210))
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(text = "Price Breakup", fontSize = 24.sp,
                            color = Color(46,167,240,255),
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold
                            )
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(text = "Room Price", color = Color.Black)
                            Text(text = "Rs. $bookingPrice", color = Color.Black)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "GST", color = Color.Black)
                            Text(text = "Rs. ${0.18*bookingPrice}", color = Color.Black)
                        }
                        Box (
                            modifier = Modifier.padding( top = 5.dp, bottom = 5.dp)
                        ){
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .border(width = 1.dp, color = Color.Gray)

                            ) {}
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "To Pay", color = Color.Black, fontWeight = FontWeight.Bold)
                            Text(text = "Rs. ${1.18*bookingPrice}", color = Color.Black)
                        }
                    }
                }
                ShowUserDetails()

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            appViewModel.uploadBookingDetailsToDataBase()
                            appViewModel.clearRoomsBooked()
                            appViewModel.userHotelBooked("","","")
                            Toast.makeText(context,"Booking Confirmed",Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Text(text = "Confirm")
                    }
                }

            }

        }
    }else{
        navHostController.navigateUp()

        appViewModel.userHotelBooked(hotelName = "", city = "", image = "")
    }

}

@Composable
fun ShowUserDetails() {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        colors = CardDefaults.cardColors(Color(223,223,225,210))
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = "User Details", fontSize = 24.sp,
                color = Color(46,167,240,255),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "Name", fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = auth.currentUser?.displayName.toString(), fontWeight = FontWeight.Bold, color = Color.Black)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Email Id", color = Color.Black)
                Text(text = auth.currentUser?.email.toString(), color = Color.Black)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Phone Number", color = Color.Black)
                Text(text = auth.currentUser?.phoneNumber.toString(), color = Color.Black)
            }
        }
    }
}