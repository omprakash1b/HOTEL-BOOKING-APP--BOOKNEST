package com.example.booknest.ui.theme

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import coil.compose.AsyncImage


@Composable
fun HotelOwner(appViewModel: AppViewModel){
    val otpScreen by appViewModel.otpScreen.collectAsState()
    val addAmenity by appViewModel.addAmenity.collectAsState()
    val amenities by appViewModel.amenities.collectAsState()
    val roomType by appViewModel.roomTypes.collectAsState()
    val addRoom by appViewModel.addRoom.collectAsState()
    val doneClicked by appViewModel.doneClicked.collectAsState()
    val context = LocalContext.current


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
       item {
           Card (
               modifier = Modifier
                   .fillMaxSize()
                   .padding(10.dp)
           ) {
               if (!otpScreen) {
                   Button(onClick = {
                       appViewModel.setOTPScreen(true)
                   }) {
                       Text(text = "Register your Hotel")
                   }
               }else{
                   Hotel_Card(appViewModel)

                   Text(text = "Amenities : ", modifier = Modifier
                       .fillMaxWidth()
                       .border(width = 1.dp, color = Color.Black), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                   for (amenity in amenities){
                       Text(text = amenity, modifier =Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                   }

                   Text(text = "Room Types : ", modifier = Modifier
                       .fillMaxWidth()
                       .border(width = 1.dp, color = Color.Black), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                   for (room in roomType){
                       Column (
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(10.dp),
                           verticalArrangement = Arrangement.SpaceEvenly,
                           horizontalAlignment = Alignment.CenterHorizontally
                       ){
                           Text(
                               text = "Room Type: "+room.name,
                               modifier = Modifier.fillMaxWidth(),
                               textAlign = TextAlign.Center
                           )

                           Text(
                               text = "Price: "+room.price.toString(),
                               modifier = Modifier.fillMaxWidth(),
                               textAlign = TextAlign.Center
                           )

                           Text(
                               text = "No of Rooms"+room.noOfRooms.toString(),
                               modifier = Modifier.fillMaxWidth(),
                               textAlign = TextAlign.Center
                           )
                       }
                   }

                   if (addAmenity){
                       AddAmenity(appViewModel = appViewModel)
                   }

                   if (!doneClicked) {
                       Button(onClick = {
                           appViewModel.toggleAddAmenity()
                       }
                       ) {
                           Text(text = "ADD Amenities")
                       }
                   }

                   if (addRoom){
                       Column (
                           modifier = Modifier.fillMaxWidth(),
                           horizontalAlignment = Alignment.CenterHorizontally,
                           verticalArrangement = Arrangement.SpaceEvenly
                       ){
                           AddRoom(appViewModel = appViewModel)
                           Row (
                               Modifier.fillMaxWidth(),
                               horizontalArrangement = Arrangement.Center,
                               verticalAlignment = Alignment.CenterVertically
                           ){
                               IconButton(onClick = {
                                   appViewModel.addroom()
                                   appViewModel.toggleAddRoom()
                               },
                                   modifier = Modifier.padding(5.dp)
                                   ) {
                                   Icon(
                                       imageVector = Icons.Filled.Check,
                                       contentDescription = "Add",
                                       tint = Color.Green
                                   )
                               }


                               IconButton(onClick = { appViewModel.toggleAddRoom() }, modifier = Modifier.padding(5.dp)) {
                                   Icon(
                                       imageVector = Icons.Filled.Close,
                                       contentDescription = "Cancel",
                                       tint = Color.Red
                                   )
                               }
                           }
                       }
                   }

                   if (!doneClicked) {
                       Button(onClick = {
                           appViewModel.toggleAddRoom()
                       }
                       ) {
                           Text(text = "ADD Rooms")
                       }
                   }

                   Row(
                       modifier = Modifier.fillMaxWidth(),
                       horizontalArrangement = Arrangement.SpaceBetween,
                       verticalAlignment = Alignment.CenterVertically
                   ) {
                       if (!doneClicked) {
                           Button(onClick = { appViewModel.doneClicked()
                                    appViewModel.setHotelData()
                           }) {
                               Text(text = "Done")
                           }
                       }
                       else {
                           Button(onClick = { appViewModel.edit() }) {
                               Text(text = "Edit")
                           }
                       }
                   }
                   
                   if (doneClicked){
                       ShowHotelData(appViewModel = appViewModel)
                   }

                   Button(onClick = {
                       appViewModel.uploadHotelDataToDatabase()
                   }) {
                      Text(text = "Submit")
                       Toast.makeText(context,"Hotel Data Uploaded",Toast.LENGTH_SHORT).show()
                   }
               }
           }
       } 

    }
}
@Composable
fun Hotel_Card(appViewModel: AppViewModel){

    val am by appViewModel.am.collectAsState()
    val hotelname by appViewModel.hotelname.collectAsState()
    val city by appViewModel.city.collectAsState()
    val hotelabout by appViewModel.hotelabout.collectAsState()
    val imageUrl by appViewModel.imageUrl.collectAsState()
    val contact by appViewModel.contact.collectAsState()
    val amenities by appViewModel.amenities.collectAsState()
    val roomType by appViewModel.roomTypes.collectAsState()
    val hotelnoOfRooms by appViewModel.hotelnoOfRooms.collectAsState()
    val roomimageUrl by appViewModel.roomimageUrl.collectAsState()
    val price by appViewModel.price.collectAsState()
    val roomname by appViewModel.roomname.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TextField(value = hotelname,
            onValueChange = { appViewModel.sethotelname(it) },
            label = {
                Text(text = "Hotel Name", fontSize = 14.sp)
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        TextField(value = city, onValueChange = { appViewModel.setcity(it) },
            label = {
                Text(text = "City", fontSize = 14.sp)
            },
                    modifier = Modifier.fillMaxWidth(),
            singleLine = true)

        TextField(value = hotelabout, onValueChange = { appViewModel.sethotelabout(it) },
            label = {
                Text(text = "Description", fontSize = 14.sp)
            },

            modifier = Modifier.fillMaxWidth(),
           
            )

        TextField(value = imageUrl, onValueChange = { appViewModel.setimageUrl(it) },
            label = {
                Text(text = "Image Url", fontSize = 14.sp)
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        TextField(value = contact, onValueChange = { appViewModel.setcontact(it) },
            label = {
                Text(text = "Phone no.", fontSize = 14.sp)
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        
        )
    }


}

@Composable
fun AddRoom(appViewModel: AppViewModel) {

    val roomType by appViewModel.roomTypes.collectAsState()
    val hotelnoOfRooms by appViewModel.hotelnoOfRooms.collectAsState()
    val roomimageUrl by appViewModel.roomimageUrl.collectAsState()
    val price by appViewModel.price.collectAsState()
    val roomname by appViewModel.roomname.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            TextField(
                value = roomname, onValueChange = {
                    appViewModel.setroomname(it)
                },
                label = {
                    Text(text = "Room Type")
                },
                modifier = Modifier.width(300.dp)
            )

            TextField(
                value = hotelnoOfRooms, onValueChange = {
                    appViewModel.sethotelnoOfRooms(it)
                },
                label = {
                    Text(text = "No. of Rooms")
                },
                modifier = Modifier.width(300.dp)
            )

            TextField(
                value = price, onValueChange = {
                    appViewModel.setprice(it)
                },
                label = {
                    Text(text = "Price")
                },
                modifier = Modifier.width(300.dp)
            )

            TextField(
                value = roomimageUrl, onValueChange = {
                    appViewModel.setroomimageurl(it)
                },
                label = {
                    Text(text = "Room Image")
                },
                modifier = Modifier.width(300.dp)
            )
        }



    }

}



@Composable
fun  AddAmenity(appViewModel: AppViewModel){

    val amenities by appViewModel.amenities.collectAsState()
    val am by appViewModel.am.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TextField(value = am, onValueChange = {
            appViewModel.setam(it)
        },
            label = {
                Text(text = "Amenity")
            },
            modifier = Modifier.width(280.dp)
        )

        IconButton(onClick = {
            appViewModel.addamenities()
            appViewModel.toggleAddAmenity()
            appViewModel.setam("")
        }) {
            Icon(imageVector = Icons.Filled.Check, contentDescription = "Add", tint = Color.Green)
        }


        IconButton(onClick = {appViewModel.toggleAddAmenity()}) {
            Icon(imageVector = Icons.Filled.Close, contentDescription = "Cancel", tint = Color.Red)
        }

    }

}

@Composable
fun ShowHotelData(appViewModel: AppViewModel){
    val hotelData by appViewModel.hotelData.collectAsState()
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = hotelData.name)
            Text(text = hotelData.city)
            Text(text = hotelData.about)
            if (hotelData.imageUrl=="null"||hotelData.imageUrl=="") {
                Text(text = hotelData.imageUrl)
            }else{
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    AsyncImage(model = hotelData.imageUrl, contentDescription = hotelData.name)
                }
            }
            Text(text = hotelData.contact)
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Amenities")
                    for (amenity in hotelData.amenities){
                        Text(text = amenity)
                    }
                }
            }
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Room Types")
                    for (rooms in hotelData.roomTypes){
                        Text(text = rooms.name)
                        Text(text = rooms.noOfRooms.toString())
                        if (rooms.imageUrl=="null" || rooms.imageUrl=="") {
                            Text(text = rooms.imageUrl)
                        }else{
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    AsyncImage(
                                        model = rooms.imageUrl.trim(),
                                        contentDescription = rooms.name
                                    )
                                    Text(text = rooms.imageUrl)
                                }
                            }
                        }
                        Text(text = rooms.price.toString())
                    }
                }
            }
            
        }
    }
}