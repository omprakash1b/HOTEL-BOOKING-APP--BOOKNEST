package com.example.booknest.ui.theme

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@Composable
fun SignUp(appViewModel: AppViewModel,
    callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks
){

    val uname by appViewModel.uname.collectAsState()
    val email by appViewModel.email.collectAsState()
    val phoneNumber by appViewModel.phoneNumber.collectAsState()
    val context = LocalContext.current

    val enable = !(uname.isEmpty() or  email.isEmpty() or phoneNumber.isEmpty())


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Blue),
        //horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

    ) {
        Column(
            modifier = Modifier.padding(top=80.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Text(
                text = "SignUp",
                fontSize = 24.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Card(
                modifier = Modifier
                    .fillMaxSize().padding(top = 10.dp)

            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {


                    OutlinedTextField(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = "Full Name Field",
                            )
                        },
                        shape = OutlinedTextFieldDefaults.shape,
                        value = uname,
                        onValueChange = { appViewModel.setUname(it) },
                        singleLine = true,
                        label = {
                            Text(
                                text = "Full Name",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Serif
                            )
                        }
                        )




                    OutlinedTextField(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = "Email Field"
                            )
                        },
                        value = email,
                        onValueChange = { appViewModel.setEmail(it) },
                        singleLine = true,
                        label = {
                            Text(
                                text = "Email",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Serif
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        )
                        )




                    OutlinedTextField(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Phone,
                                contentDescription = "Phone Number Field"
                            )
                        },
                        value = phoneNumber,
                        onValueChange = { appViewModel.setPhoneNumber(it) },
                        singleLine = true,
                        label = {
                            Text(
                                text = "Phone Number",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Serif
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        )
                        )


                    Button(
                        onClick = {
                            appViewModel.setLoading(true)

                            val options = PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber("+91${phoneNumber}") // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(context as Activity) // Activity (for callback binding)
                                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                                .build()
                            PhoneAuthProvider.verifyPhoneNumber(options)


                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = enable
                    )
                    {
                        Text(text = "Send OTP")
                    }

                }
            }
        }
    }
}