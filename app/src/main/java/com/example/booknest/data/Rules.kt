package com.example.booknest.data

object Rules {
    fun getRules():List<String>{
        return listOf(
            "Check-In time on or after 12 P.M.",
            "Check-Out time before 11 A.M.",
            "No Destruction of Property",
            "Only Government authorised ID proof such as Aadhar Card, Driving License, Voter Id, Passport, etc will be accepted",
            "Pets are not allowed."
        )
    }
}