package com.example.booknest.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.booknest.data.Place

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Where2GoScreen(
    appViewModel: AppViewModel,
    where2GoUiState: AppViewModel.Where2GoUiState,
    navHostController: NavHostController
){
    when(where2GoUiState){
        is AppViewModel.Where2GoUiState.Success->{

            LazyColumn(
                modifier = Modifier.background(Color(250,250,250,255)),
                contentPadding = PaddingValues(5.dp)
            ) {
                items(where2GoUiState.places){
                    Where2GoCard(appViewModel = appViewModel, places = it,navHostController = navHostController)
                }
            }
        }
        is AppViewModel.Where2GoUiState.Error->{
            Error(appViewModel = appViewModel)
        }
        else ->{
            Loading()
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Where2GoCard(
        appViewModel: AppViewModel,
        places:Place,
        navHostController: NavHostController
){
    Card(
        modifier = Modifier.padding(10.dp)
            .clickable{
            appViewModel.selectedPlace(place = places)
            navHostController.navigate(route = BookNestScreen.Places.name)
        },
    ) {
        Column (
            modifier = Modifier.background(Color.LightGray)
        ){
            AsyncImage(model = places.image, contentDescription = places.city+" Image")
            Row (
                modifier = Modifier.fillMaxWidth().height(45.dp).padding(10.dp)

            ){
                Text(text = places.city, fontWeight = FontWeight.SemiBold, color = Color.Gray)
            }
        }
    }
}

@Composable
fun Loading(){

    Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ){
            CircularProgressIndicator()
            Text(text = "Loading")
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Error(appViewModel: AppViewModel){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(id = R.drawable.error), contentDescription = "error")
            Text(text = "Something went wrong please try again later.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp)
            )

            Button(
                onClick = { appViewModel.downloadPlaces() },
                shape = RoundedCornerShape(5.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Retry", fontSize = 20.sp, fontFamily = FontFamily.Serif)
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Retry",
                        tint = Color.Cyan
                    )
                }
            }
        }
    }
}