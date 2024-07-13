package com.example.booknest.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

enum class BookNestScreen(val title:String){
    Where2Go(title = "Where2Go"),
    FindRoom(title = "Find Room"),
    SelectHotel(title = "Select Hotel"),
    HotelOwner(title = "Register Hotel"),
    SelectRoomScreen(title = "Select Room"),
    ErrorScreen(title = "Error Screen"),
    Checkout(title = "Checkout"),
    Places(title = "Places")
}

var canNavigateBack :Boolean =false
val auth : FirebaseAuth = FirebaseAuth.getInstance()

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookNest(
    appViewModel: AppViewModel = viewModel(),
    navHostController: NavHostController = rememberNavController()
){

    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val currentScreen = BookNestScreen.valueOf(backStackEntry?.destination?.route?:BookNestScreen.FindRoom.name)
    val otpScreen by appViewModel.otpScreen.collectAsState()
    val uiState by appViewModel.uiState.collectAsState()
    val uname by appViewModel.uname.collectAsState()
    val email by appViewModel.email.collectAsState()
    val otp by appViewModel.otp.collectAsState()


    auth.currentUser?.let { appViewModel.setUser(it) }

    canNavigateBack = navHostController.previousBackStackEntry!=null

    val ticks by appViewModel.ticks.collectAsState()
    val user by appViewModel.user.collectAsState()
    val logOutClicked by appViewModel.logOutClicked.collectAsState()


    val isVisible by appViewModel.isVisible.collectAsState()
    if (isVisible) {
        MySplashScreen()
    }

    else if(user != null) {
        Scaffold (
            topBar = {
                TopAppBar(title = {
                        TopBar(title = currentScreen.title,navHostController= navHostController,appViewModel=appViewModel,currentScreen = currentScreen)
                    },

                    navigationIcon = {
                        if (canNavigateBack) {
                            Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back Button",
                                modifier = Modifier.clickable {
                                        navHostController.navigateUp()
                                    }
                                )
                        }
                    },
                    //modifier = Modifier.background(Color(250,250,250,255)),
                    colors = TopAppBarColors(
                        containerColor =Color(250,250,250,255),
                        navigationIconContentColor =Color.Black,
                        actionIconContentColor = Color(250,250,250,255),
                        titleContentColor = Color.Black,
                        scrolledContainerColor = Color.Black
                        )
                )
            },

            bottomBar = {
                    BottomBar(navHostController= navHostController, currentScreen = currentScreen)
            },

            modifier = Modifier.background(color = Color.White)

        ){

            NavHost(navController = navHostController, startDestination = BookNestScreen.FindRoom.name, modifier = Modifier.padding(it)) {
                composable(route = BookNestScreen.Where2Go.name){
                    Where2GoScreen(appViewModel=appViewModel, where2GoUiState = appViewModel.where2GoUiState,navHostController= navHostController)
                }
                composable(route= BookNestScreen.FindRoom.name){
                    FindRoomScreen(appViewModel=appViewModel, navHostController = navHostController)
                }

                composable(route =BookNestScreen.SelectHotel.name){
                    SelectHotel(appViewModel=appViewModel, navHostController = navHostController)
                }
                composable(route = BookNestScreen.HotelOwner.name){
                    HotelOwner(appViewModel = appViewModel)
                }
                composable(route = BookNestScreen.SelectRoomScreen.name){
                    SelectRoomScreen(appViewModel = appViewModel, roomUiState = appViewModel.roomUiState, navHostController = navHostController)
                }
                composable(route = BookNestScreen.ErrorScreen.name){
                    Error()
                }
                composable(route = BookNestScreen.Checkout.name){
                    CheckOutScreen(appViewModel=appViewModel,navHostController=navHostController)
                }
                composable(route = BookNestScreen.Places.name){
                    PlaceScreen(placesUiState = appViewModel.placesUiState)
                }
            }
        }

        if (logOutClicked){
            Alert( onYesButtonClicked = {
                auth.signOut()
                appViewModel.logOut()
                appViewModel.setLogOutClicked(false)
                                        },
                onNoButtonClicked = {
                    appViewModel.setLogOutClicked(false)
                }
                )
        }
    }

    else {
        LoginScreen(appViewModel = appViewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBar(title:String,navHostController: NavHostController,appViewModel: AppViewModel,currentScreen: BookNestScreen){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = title)

//        IconButton(onClick = {
//            if (currentScreen!=BookNestScreen.HotelOwner) {
//                navHostController.navigate(BookNestScreen.HotelOwner.name)
//            }
//        },
//            modifier = Modifier.size(80.dp)
//            ) {
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Hotel Registration", tint = Color.Cyan)
//                Text(text = "Add Hotel", fontSize = 12.sp)
//            }
//        }
        IconButton(
            modifier = Modifier.height(50.dp).width(120.dp),
            onClick = {
            appViewModel.setLogOutClicked(true)
        }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                    contentDescription = "Logout"
                )
                Text(text = "Logout", fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun BottomBar(
    navHostController: NavHostController,
    currentScreen:BookNestScreen
){
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            Color(66, 165, 245, 255),
            Color(38, 198, 218, 255)
        ),
        startX = 0f,
        endX = 1000f
    )

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(gradient),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(onClick = {
            if (currentScreen!=BookNestScreen.FindRoom) {
                navHostController.navigate(BookNestScreen.FindRoom.name){
                    popUpTo(0)
                }
            }
        },
                modifier = Modifier.size(50.dp)
            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Icon(imageVector = Icons.Outlined.Home,
                    tint = Color.White,
                    contentDescription = "Home")
                Text(text = "Home", fontSize = 12.sp, color = Color.White)
            }
        }
        IconButton(onClick = {
            if (currentScreen!=BookNestScreen.Where2Go) {
                navHostController.navigate(BookNestScreen.Where2Go.name)
            }
        },
            modifier = Modifier.size(70.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "WheretoGo",
                    tint = Color.White
                    )
                Text(text = "Where2Go", fontSize = 12.sp, color = Color.White)
            }
        }
//        IconButton(onClick = {
//            if (currentScreen!=BookNestScreen.FAQ) {
//                navHostController.navigate(BookNestScreen.FAQ.name)
//            }
//        },
//            modifier = Modifier.size(50.dp)) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Icon(imageVector = Icons.Outlined.Info, contentDescription = "FAQs")
//                Text(text = "FAQs", fontSize = 12.sp)
//            }
//        }
    }
}

@Composable
fun Alert(
    onYesButtonClicked:()->Unit,
    onNoButtonClicked:()->Unit
){
    AlertDialog(onDismissRequest = {
        onNoButtonClicked()
    },
        confirmButton = {
            IconButton(onClick = {onYesButtonClicked() }) {
                Icon(imageVector = Icons.Outlined.Check, contentDescription = "Confirm", tint = Color.Cyan)
            }
        },
        title = {
            Text(text = "LogOut?", fontWeight = FontWeight.Bold)
        },
        text = {
            Text(text = "Are you sure want to Log out?", fontSize = 14.sp)
        },
        dismissButton = {
            IconButton(onClick = { onNoButtonClicked() }) {
                Icon(imageVector = Icons.Outlined.Close, contentDescription ="Cancel", tint = Color.Red )
            }
        }
    )
}