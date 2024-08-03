package com.example.booknest.ui.theme

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

@Composable
fun LoginScreen(appViewModel: AppViewModel){


    val verificationId by appViewModel.verificationId.collectAsState()
    val otp by appViewModel.otp.collectAsState()
    val context = LocalContext.current

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

           // signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {


            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            Toast.makeText(context,"OTP Sent",Toast.LENGTH_SHORT).show()
            appViewModel.setVerificationId(id = verificationId)
            appViewModel.resetTimer()
            appViewModel.runTimer()
            appViewModel.setLoading(false)

        }
    }

    if (verificationId==""){
        SignUp(appViewModel = appViewModel,callbacks = callbacks)
    }
    else{
        OTPScreen(otp = otp, appViewModel = appViewModel,callbacks= callbacks)
    }

    val loading by appViewModel.loading.collectAsState()
    if (loading){
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            CircularProgressIndicator()
            Text(text = "Loading")
        }
    }

}