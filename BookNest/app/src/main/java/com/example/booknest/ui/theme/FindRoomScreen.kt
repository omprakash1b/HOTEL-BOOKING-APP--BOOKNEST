package com.example.booknest.ui.theme

import android.app.DatePickerDialog
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.booknest.data.BestPlaces
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindRoomScreen(
    appViewModel: AppViewModel,
    navHostController: NavHostController
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }
    val options = listOf("Agra", "Bengaluru", "Chennai")
    val checkInDate by appViewModel.checkInDate.collectAsState()
    val checkOutDate by appViewModel.checkOutDate.collectAsState()
    val noOfRooms by appViewModel.noOfRooms.collectAsState()
    val bedFlag by appViewModel.bedFlag.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(250,250,250,255))
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange =
            {
                expanded = !expanded
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
            TextField(value = selectedOptionText,
                onValueChange = {selectedOptionText = it},
                label = {
                    Text(text = "Where do want to go?", color = Color.Black)
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Where do want to go",
                        tint = Color(
                            66,
                            165,
                            245,
                            255
                        )
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                colors = TextFieldDefaults.textFieldColors(
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground,
                    disabledTextColor = Color.Black,
                    containerColor = Color.White,
                    disabledIndicatorColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                enabled = false
            )

            ExposedDropdownMenu(
                expanded = expanded,
                modifier = Modifier.background(Color(225,225,225,225)),
                onDismissRequest = {
                    expanded = false
                }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        { Text(text = selectionOption, color = Color.Black) },
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                        })
                }
            }
        }

        MyDatePicker("Check-in Date", checkInDate) { appViewModel.setCheckIn(it) }
        MyDatePicker("Check-out Date", checkOutDate) { appViewModel.setCheckOut(it) }


        TextField(
            value = if (noOfRooms==0)"" else noOfRooms.toString(),
            onValueChange = {
                appViewModel.setNoOfRooms(it.toInt())
            },
            enabled = false,
            label = {
                Text(text = "Number of Rooms", color = Color.Black)
            },
            colors = TextFieldDefaults.textFieldColors(
                disabledLabelColor = MaterialTheme.colorScheme.onBackground,
                disabledTextColor = Color.Black,
                containerColor = Color.White,
                disabledIndicatorColor = MaterialTheme.colorScheme.secondaryContainer
            ),

            modifier = Modifier
                .fillMaxWidth()
                .clickable { appViewModel.setBedFlag(true) },
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Home, contentDescription = "Select number of rooms",
                    tint = Color(
                        66,
                        165,
                        245,
                        255
                    )
                )
            },


        )
        if(bedFlag){
            AlertDialogBoxWithCounter(
                appViewModel = appViewModel,
                noOfRooms=noOfRooms
            )
        }



        //Search Button
        val gradient = Brush.horizontalGradient(
            colors = listOf(
                Color(66, 165, 245, 255),
                Color(38, 198, 218, 255)
            ),
            startX = 0f,
            endX = 1000f
        )

        Button(
            onClick = {  appViewModel.updateUiState(
                clickStatus = true,
                selectedObject = selectedOptionText
            )
                appViewModel.countNoOfDays()

                navHostController.navigate(BookNestScreen.SelectHotel.name) },
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            shape = RectangleShape,
            enabled =(selectedOptionText!="")and (checkInDate!="")and (checkOutDate!="")and(noOfRooms!=0)
        ) {
            Text(text = "Search")
        }

        //Best Places

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Best Places" , fontSize = 16.sp, color = Color.Gray, fontFamily = FontFamily.Serif)
            Text(
                text = "View All",
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                color = Color(0,0,120),
//                modifier = Modifier
//                    .clickable {
//                        navHostController.navigate(route = BookNestScreen.ErrorScreen.name)
//                    }
            )
        }


        val bestPlaces by appViewModel.bestPlacesInternet.collectAsState()

        LazyRow (
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),

        ){
            items(bestPlaces){
                GridCard(items = it,
                    onClickMethod = {
                        appViewModel.updateUiState(
                            clickStatus = true,
                            selectedObject = it.name
                        )
                        navHostController.navigate(BookNestScreen.SelectHotel.name)
                    }

                )
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    label: String,
    check: String,
    dateValue: (String) -> Unit,
) {
    val mContext = LocalContext.current
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    val mCalendar = Calendar.getInstance()

    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            dateValue("$mDayOfMonth/${mMonth + 1}/$mYear")
        }, mYear, mMonth, mDay
    )

    TextField(
        value = check,
        onValueChange = { dateValue(it)
        },
        label = { Text(text = label, color = Color.Black) },
        enabled = false,
        modifier = Modifier
            .clickable { mDatePickerDialog.show() }
            .fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            disabledLabelColor = MaterialTheme.colorScheme.onBackground,
            disabledTextColor = Color.Black,
            containerColor = Color.White,
            disabledIndicatorColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.DateRange, contentDescription = "date", tint = Color(
                    66,
                    165,
                    245,
                    255
                )
            )
        }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlertDialogBoxWithCounter(appViewModel: AppViewModel
                              ,noOfRooms:Int){
    
    AlertDialog(
        onDismissRequest = {
            appViewModel.setBedFlag(false)
        },
        confirmButton = { /*TODO*/ },
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Number of Rooms", fontSize = 18.sp, color = Color.Black)
            }
        },
        modifier = Modifier.size(height = 150.dp, width = 300.dp),
        containerColor = Color(223,223,225,255),
        text = {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                val enable = noOfRooms >= 1
                IconButton(
                    onClick = {
                        appViewModel.setNoOfRooms(noOfRooms-1)
                    },
                    enabled = enable
                ) {
                   Icon(imageVector = Icons.Outlined.KeyboardArrowLeft, contentDescription = "Decrease by One", tint = Color.Black)
                }

                Text(text = noOfRooms.toString(), fontSize = 18.sp, color = Color.Black)

                IconButton(
                    onClick = {
                        appViewModel.setNoOfRooms(noOfRooms+1)
                    }
                ) {
                    Icon(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = "Increase by One", tint = Color.Black)
                }
            }
        }
        
    )
    
}


@Composable
fun GridCard(
    items : BestPlaces,
    onClickMethod : ()-> Unit
){
    Card(
        modifier = Modifier
            .size(width = 200.dp, height = 165.dp)
            .padding(5.dp)
            .clickable {
               // onClickMethod()
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            AsyncImage(model = items.imageUrl, contentDescription = items.name)
            Text(text = items.name, fontSize = 18.sp, color = Color.Black)
        }


    }
}


@Composable
fun Exception(value:String){
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        confirmButton = { /*TODO*/ },
        text = {
            Text(text = value)
        }
    )
}

