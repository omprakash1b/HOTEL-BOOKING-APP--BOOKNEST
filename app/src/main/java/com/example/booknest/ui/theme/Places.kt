package com.example.booknest.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.booknest.data.Place

@Composable
fun PlaceScreen(placesUiState: AppViewModel.PlacesUiState
                ){
    when(placesUiState){
        is AppViewModel.PlacesUiState.Success->{
            LazyColumn (
                modifier = Modifier.background(Color(250,250,250,255))
            ){
                item {
                    PlaceCard(places = placesUiState.place)
                }
            }
        }
        else->{
            Loading()
        }
    }
}

@Composable
fun PlaceCard(places:Place){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card (
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomEnd = 50.dp)
        ){
            AsyncImage(model = places.image, contentDescription = places.image+" Image")
        }
        Text(
                text = places.city,
                textAlign = TextAlign.Start,
                fontSize = 30.sp,
                color = Color(46,167,245,190),
                fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 15.dp, end = 10.dp, start = 10.dp, bottom = 10.dp)
                .fillMaxWidth()
            )
        Text(text = places.about,
                fontSize = 15.sp,
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            color = Color.Black,
            )
    }
}