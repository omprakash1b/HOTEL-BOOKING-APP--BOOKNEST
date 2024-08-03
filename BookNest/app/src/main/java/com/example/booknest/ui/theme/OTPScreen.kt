package com.example.booknest.ui.theme

import android.app.Activity
import android.content.Context
import android.text.format.DateUtils
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import java.util.concurrent.TimeUnit

@Composable
fun OTPScreen(otp: String,appViewModel: AppViewModel,
    callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks
){
    val phoneNumber by appViewModel.phoneNumber.collectAsState()
    val context = LocalContext.current
    val verificationId by appViewModel.verificationId.collectAsState()
    val uname by appViewModel.uname.collectAsState()
    val email by appViewModel.email.collectAsState()
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
                    .fillMaxSize()
                    .padding(top = 10.dp)

            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp),
                        horizontalAlignment = Alignment.Start
                    ){
                        Text(
                            text = "Verify Mobile Number",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(5.dp),
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "OTP has been sent to your mobile number, please enter it below",
                            fontWeight = FontWeight.SemiBold, fontSize = 16.sp, modifier = Modifier.padding(5.dp), color = Color.Gray)
                    }

                    OtpBoxField(otp = otp, appViewModel = appViewModel)

                    Button(
                        onClick = {
                            if (otp.isEmpty()){
                                Toast.makeText(context, "Enter Otp", Toast.LENGTH_SHORT).show()
                            }else{
                                val credential = PhoneAuthProvider.getCredential(verificationId, otp)
                                signInWithPhoneAuthCredential(credential,context,appViewModel,uname,email)
                            }

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                        colors = ButtonDefaults.buttonColors(Color(56,182,255,100)),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Verify OTP", color = Color.Black)
                        }
                    }

                    val ticks by appViewModel.ticks.collectAsState()

                    val text = if(ticks==0L)"Resend OTP" else "Resend OTP (${DateUtils.formatElapsedTime(ticks)})"

                    Button(
                        onClick = {

                            val options = PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber("+91${phoneNumber}") // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(context as Activity) // Activity (for callback binding)
                                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                                .build()
                            PhoneAuthProvider.verifyPhoneNumber(options)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        colors = ButtonDefaults.buttonColors(Color(56,182,255,100)),
                        shape = RoundedCornerShape(5.dp),
                        enabled = ticks==0L
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = text, color = Color.Black)
                        }
                    }

                    Button(
                        onClick = {
                            appViewModel.setVerificationId("")
                            appViewModel.setPhoneNumber("")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        colors = ButtonDefaults.buttonColors(Color.Blue),
                        shape = RoundedCornerShape(5.dp),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Edit Phone Number", color = Color.White)
                        }
                    }


                }
            }
        }
    }

}

@Composable
fun OtpBoxField(otp: String,appViewModel: AppViewModel){
    /*      BasicTextField:
            It is a text field similar to TextField but with limited customised option.
     */
    BasicTextField(
        value = otp,
        onValueChange = {
            appViewModel.setOtp(it)
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    ){
        Row (
            horizontalArrangement = Arrangement.Center,
        ){
            repeat(6){index->
                val number = when{
                    index >=otp.length -> ""
                    else -> otp[index].toString()
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(4.dp)
                        .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
                        .size(width = 40.dp, height = 50.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = number, fontSize = 32.sp)

                }
            }

        }
    }

}



private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential,
                                          context:Context,appViewModel: AppViewModel,
                                            uname:String,
                                          email:String
) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener(context as Activity) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = task.result?.user
                if (user!=null){
                    appViewModel.setUser(user)


                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(uname)
                        .build()
                    user.updateProfile(profileUpdates)
                    @Suppress("DEPRECATION")
                    user.updateEmail(email).addOnCompleteListener {
                        if (task.isSuccessful){
                            Toast.makeText(context,"email added successfully",Toast.LENGTH_SHORT).show()
                        } else{
                            Toast.makeText(context,"email addition unsuccessful",Toast.LENGTH_SHORT).show()
                        }
                    }

                }

            } else {
                // Sign in failed, display a message and update the UI

                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                    Toast.makeText(context,"Invalid OTP",Toast.LENGTH_SHORT).show()
                }
                // Update UI
            }
        }
}