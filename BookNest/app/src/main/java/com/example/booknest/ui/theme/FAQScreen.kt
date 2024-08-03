package com.example.booknest.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FAQScreen(appViewModel: AppViewModel){
    val user by appViewModel.user.collectAsState()
    val noOfDays by appViewModel.noOfDays.collectAsState()
        Column {
            Text(text = "User Id : "+auth.currentUser?.uid.toString())
            Text(text = "User: "+user?.displayName)
            Text(text = "Email ${user?.email}")
            Text(text = noOfDays.toString())
        }
}