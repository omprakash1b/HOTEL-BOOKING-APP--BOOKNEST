package com.example.booknest.ui.theme

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booknest.data.BestPlaces
import com.example.booknest.data.HotelData
import com.example.booknest.data.Place
import com.example.booknest.data.RoomType
import com.example.booknest.data.UserDetails
import com.example.booknest.data.UserHotelData
import com.example.booknest.network.AppApi
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class AppViewModel :ViewModel()
{

    private val _otpScreen = MutableStateFlow(false)
    val otpScreen : MutableStateFlow<Boolean>get() = _otpScreen

    private val _uname = MutableStateFlow("")
    val uname:MutableStateFlow<String>get() = _uname

    private val _otp = MutableStateFlow("")
    val otp : MutableStateFlow<String>get() = _otp

    private val _email = MutableStateFlow("")
    val email:MutableStateFlow<String>get() = _email

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber:MutableStateFlow<String>get() = _phoneNumber

    private val _isVisible = MutableStateFlow(true)
    val isVisible:MutableStateFlow<Boolean>get() = _isVisible

    private val _place = MutableStateFlow("")
    val place : MutableStateFlow<String>get() = _place

    private val _ticks = MutableStateFlow(60L)
    val ticks : MutableStateFlow<Long>get() = _ticks

    private val _checkInDate = MutableStateFlow("")
    val checkInDate : MutableStateFlow <String>get() = _checkInDate

    private val _checkOutDate = MutableStateFlow("")
    val checkOutDate : MutableStateFlow <String>get() = _checkOutDate

    private val _noOfRooms = MutableStateFlow(0)
    val noOfRooms : MutableStateFlow<Int>get() = _noOfRooms

    private val _bedFlag = MutableStateFlow(false)
    val bedFlag :MutableStateFlow<Boolean>get() = _bedFlag

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState : StateFlow<AppUiState> = _uiState.asStateFlow()

    private val _bestPlacesInternet = MutableStateFlow(emptyList<BestPlaces>())
    val bestPlacesInternet : MutableStateFlow<List<BestPlaces>>get() = _bestPlacesInternet

    private val _exception = MutableStateFlow("")

    private val _etag = MutableStateFlow(false)

    private lateinit var internetJob :Job
    private lateinit var timerJob : Job

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user : MutableStateFlow<FirebaseUser?>get() = _user

    private val _verificationId = MutableStateFlow("")
    val verificationId : MutableStateFlow<String> get() = _verificationId

    private val _loading = MutableStateFlow(false)
    val loading : MutableStateFlow<Boolean>get() = _loading

    private val _addAmenity = MutableStateFlow(false)
    val addAmenity :MutableStateFlow<Boolean>get() = _addAmenity

    private val _dialogue =MutableStateFlow(false)
    val dialog : MutableStateFlow<Boolean>get() = _dialogue

    //Variables for hotel database
    private val _hotelname = MutableStateFlow("")
    private val _city = MutableStateFlow("")
    private val _hotelabout = MutableStateFlow("")
    private val _imageUrl = MutableStateFlow("")
    private val _contact = MutableStateFlow("")
    private val _roomname = MutableStateFlow("")
    private val _hotelnoOfRooms = MutableStateFlow("")
    private val _roomimageUrl = MutableStateFlow("")
    private val _price= MutableStateFlow("")
    private val _amenities = MutableStateFlow<MutableList<String>>(mutableListOf())
    private val _roomTypes = MutableStateFlow<MutableList<RoomType>>(mutableListOf())
    private val _am = MutableStateFlow("")

    private val _addRoom = MutableStateFlow(false)
    private val _hotelData = MutableStateFlow(HotelData())


    val hotelname:MutableStateFlow<String>get() = _hotelname
    val city :MutableStateFlow<String>get() = _city
    val hotelabout :MutableStateFlow<String>get() = _hotelabout
    val imageUrl:MutableStateFlow<String>get() = _imageUrl
    val contact :MutableStateFlow<String>get() = _contact
    val roomname:MutableStateFlow<String>get() = _roomname
    val hotelnoOfRooms:MutableStateFlow<String>get() = _hotelnoOfRooms
    val roomimageUrl:MutableStateFlow<String>get() = _roomimageUrl
    val price:MutableStateFlow<String>get() = _price
    val amenities:MutableStateFlow<MutableList<String>>get() = _amenities
    val roomTypes:MutableStateFlow<MutableList<RoomType>>get() = _roomTypes
    val am :MutableStateFlow<String>get() = _am
    val addRoom :MutableStateFlow<Boolean>get() = _addRoom
    val hotelData : MutableStateFlow<HotelData>get() = _hotelData

    private val _doneClicked = MutableStateFlow(false)
    val doneClicked : MutableStateFlow<Boolean>get() = _doneClicked

    private val _logOutClicked = MutableStateFlow(false)
    val logOutClicked : MutableStateFlow<Boolean>get() = _logOutClicked
//
//    private val _userId = MutableStateFlow(_user.value!!.uid)
//    val userId : MutableStateFlow<String>get() = _userId

    //Firebase Database
    private val database = Firebase.database
    private val hotelRef = database.getReference("hotel")
    //private val userBookingRef = database.getReference("Users/"+ auth.currentUser?.uid.toString()+"/bookingDetails")

    private val _hotelList = MutableStateFlow<List<HotelData>>(emptyList())
    val hotelList:MutableStateFlow<List<HotelData>>get() = _hotelList

    sealed interface HotelUiState{
        data class Success(val hotelList: List<HotelData>):HotelUiState
        data object Loading :HotelUiState
    }

    var hotelUiState:HotelUiState by mutableStateOf(HotelUiState.Loading)
        private set

    sealed interface RoomUiState{
        data class Success(val hotelDetails : HotelData):RoomUiState
        data object Loading :RoomUiState
    }

    var roomUiState:RoomUiState by mutableStateOf(RoomUiState.Loading)
        private set

    sealed interface Where2GoUiState{
        data class Success(val places : List<Place>):Where2GoUiState
        data object Loading : Where2GoUiState
        data object Error : Where2GoUiState
    }

    var where2GoUiState : Where2GoUiState by mutableStateOf(Where2GoUiState.Loading)
        private set

    sealed interface PlacesUiState{
        data class Success(val place : Place):PlacesUiState
        data object Loading : PlacesUiState
    }

    var placesUiState : PlacesUiState by mutableStateOf(PlacesUiState.Loading)

    private val _placesList = MutableStateFlow(emptyList<Place>())
    val placesList : MutableStateFlow<List<Place>>get() = _placesList

    private val _seeMoreClicked = MutableStateFlow(false)
    val seeMoreClicked :MutableStateFlow<Boolean>get() = _seeMoreClicked

    private val _userHotelBooked = MutableStateFlow(UserHotelData())
    val userHotelBooked : MutableStateFlow<UserHotelData>get() = _userHotelBooked

    private val _userRoomsBooked = MutableStateFlow<List<RoomType>>(emptyList())
    val userRoomsBooked :MutableStateFlow<List<RoomType>>get() = _userRoomsBooked

    private val _bookingPrice = MutableStateFlow(0)
    val bookingPrice : MutableStateFlow<Int>get() = _bookingPrice

    private val _userDetails = MutableStateFlow(UserDetails())
    val userDetails:MutableStateFlow<UserDetails>get() = _userDetails

    private val _noOfDays = MutableStateFlow(0)
    val  noOfDays : MutableStateFlow<Int>get() = _noOfDays

    //Declaring the setter functions

    fun setUname(name:String){
        _uname.value = name
    }
    fun setEmail(email:String){
        _email.value = email
    }
    fun setPhoneNumber(phoneNumber:String){
        _phoneNumber.value = phoneNumber
    }

    fun setPlace(place:String){
        _place.value = place
    }

    fun setCheckIn(date : String){
        _checkInDate.value =date
    }

    fun setCheckOut(date : String){
        _checkOutDate.value =date
    }

    fun setNoOfRooms(rooms:Int){
        _noOfRooms.value = rooms
    }

    fun setBedFlag(flag :Boolean){
        _bedFlag.value = flag
    }

    private fun toggleDialogue(){
        _dialogue.value = !_dialogue.value
    }

    fun addPlaces(place: Place){
        _placesList.value+=place
    }

    fun updateUiState(clickStatus:Boolean,selectedObject : String){
        _uiState.update {
            it.copy(
                clickStatus = clickStatus,
                selectedObject = selectedObject
            )
        }
    }

    fun runTimer(){
        timerJob = viewModelScope.launch {
            while (_ticks.value>0L){
                delay(1000)
                _ticks.value -=1L
            }
        }
    }
    fun resetTimer(){
        try {
            timerJob.cancel()
        }
        catch (_: Exception){

        }
        finally {
            _ticks.value = 60L
        }
    }

    fun setOtp(otp :String){
        _otp.value = otp
    }

    fun setVerificationId(id:String){
        _verificationId.value = id
    }

    fun setLoading(loadingFlag:Boolean){
        _loading.value = loadingFlag
    }

    fun setUser(user:FirebaseUser){
        _user.value = user
    }

    fun setOTPScreen(flag: Boolean){
        _otpScreen.value = flag
    }

    fun toggleAddAmenity(){
        _addAmenity.value= !_addAmenity.value
    }

    fun sethotelname(name: String)  {
        _hotelname.value=name
    }

    fun sethotelabout(name: String)  {
        _hotelabout.value=name
    }

    fun sethotelnoOfRooms(rooms:String){
        _hotelnoOfRooms.value = rooms
    }

    fun setcontact(name: String)  {
        _contact.value=name
    }

    fun setcity(name: String)  {
        _city.value=name
    }

    fun setroomname(name: String){
        _roomname.value =name
    }

    fun setroomimageurl(name: String)  {
        _roomimageUrl.value=name
    }


    fun setprice(name: String)  {
        _price.value=name
    }
    fun setam(name: String)  {
        _am.value=name
    }

    fun setimageUrl(url:String){
        _imageUrl.value = url
    }


    fun addamenities(){
        _amenities.value += _am.value
    }

    fun addroom(){
        _roomTypes.value += RoomType(_roomname.value,_hotelnoOfRooms.value.toInt(),_roomimageUrl.value,_price.value.toInt())
    }

    fun toggleAddRoom(){
        _addRoom.value =! _addRoom.value
    }

    fun doneClicked(){
        _doneClicked.value = true
    }
    fun edit(){
        _doneClicked.value = false
    }

    fun setHotelData(){
        _hotelData.value = HotelData(name = _hotelname.value, city = _city.value, about = _hotelabout.value, imageUrl = _imageUrl.value,
            amenities = _amenities.value, roomTypes = _roomTypes.value, contact = _contact.value
            )
    }

    fun logOut(){
        _user.value = null
        _verificationId.value=""
        _otp.value=""
        _phoneNumber.value=""
        _email.value=""
        _uname.value=""
        resetTimer()
    }

    fun setLogOutClicked(flag: Boolean){
        _logOutClicked.value = flag
    }

    fun uploadHotelDataToDatabase(){
        hotelRef.push().setValue(_hotelData.value)
    }

    fun addHotelDataInList(data: HotelData){
        _hotelList.value += data
    }

    fun setHotelForRoomScreen(data : HotelData){
        roomUiState = RoomUiState.Success(data)
    }

    fun toggleSeeMore(){
        _seeMoreClicked.value = !_seeMoreClicked.value
    }

    fun selectRoom(room:RoomType){
        _userRoomsBooked.value +=room
        changeBookingPrice()
    }

    fun removeRoom(room: RoomType){
        _userRoomsBooked.value-=room
        changeBookingPrice()
    }

    fun userHotelBooked(hotelName: String,city:String,image:String){
        _userHotelBooked.value = UserHotelData(hotelName,city,image,_userRoomsBooked.value)
    }

    fun changeBookingPrice(){
        _bookingPrice.value=0
       for (room in _userRoomsBooked.value)
           _bookingPrice.value+=room.price
    }
    fun clearRoomsBooked(){
        _userRoomsBooked.value = emptyList()
    }

    fun uploadBookingDetailsToDataBase(){

        val userBookingRef=database.getReference("Users")
            val userId = auth.currentUser?.uid

            userBookingRef.child(userId.toString()).child("BookingDetails").push().setValue(_userHotelBooked.value)
    }

    fun uploadPlaces(data:Place){
        val ref = database.getReference("Places")
        ref.push().setValue(data)
    }

    fun downloadPlaces(){
        val ref = database.getReference("Places")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                _placesList.value = emptyList()
                snapshot.children.forEach { child->
                    val place = child.getValue(Place::class.java)
                    place?.let {
                        addPlaces(it)
                    }
                }
                where2GoUiState = Where2GoUiState.Success(places = _placesList.value)
                _placesList.value = emptyList()
            }

            override fun onCancelled(error: DatabaseError) {
                where2GoUiState = Where2GoUiState.Error
            }

        })
    }

    private fun downloadHotelData(){
        // Read from the database
        toggleDialogue()
        hotelRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                _hotelList.value = emptyList()
                for (child in dataSnapshot.children){
                    val hotel = child.getValue(HotelData::class.java)
                    hotel?.let {
                        addHotelDataInList(it)

                    }
                }
                hotelUiState = HotelUiState.Success(_hotelList.value)

            }

            override fun onCancelled(error: DatabaseError) {

                Log.w("Download Error", "Failed to read value.", error.toException())
            }
        })
    }

    fun selectedPlace(place: Place){
        placesUiState = PlacesUiState.Success(place = place)
    }

    fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    fun daysInMonth(month: Int, year: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(year)) 29 else 28
            else -> throw IllegalArgumentException("Invalid month")
        }
    }

    fun daysSinceYearStart(day: Int, month: Int, year: Int): Int {
        var days = day
        for (m in 1 until month) {
            days += daysInMonth(m, year)
        }
        return days
    }

    fun parseDate(date: String): Triple<Int, Int, Int> {
        val parts = date.split("/")
        if (parts.size != 3) throw IllegalArgumentException("Invalid date format")
        val year = parts[2].toInt()
        val month = parts[1].toInt()
        val day = parts[0].toInt()
        return Triple(year, month, day)
    }


    fun countNoOfDays() {
        val startDateString = _checkInDate.value
        val endDateString = _checkOutDate.value

        val startDateParts = startDateString.split("/")
        val endDateParts = endDateString.split("/")

        val startYear = startDateParts[2].toInt()
        val startMonth = startDateParts[1].toInt()
        val startDay = startDateParts[0].toInt()

        val endYear = endDateParts[2].toInt()
        val endMonth = endDateParts[1].toInt()
        val endDay = endDateParts[0].toInt()

        val dateStr = checkInDate.value
        val dateend = checkOutDate.value
        val (year, month, day) = parseDate(dateStr)
        val (y,m,d) = parseDate(dateend)
        val days = daysSinceYearStart(d,m,y)-daysSinceYearStart(day, month, year)

        //var totalDays =0

//        for (year in startYear..endYear) {
//            for (month in 1..12) {
//                if (year == startYear && month < startMonth) continue
//                if (year == endYear && month > endMonth) break
//
//                val daysInMonth = when (month) {
//                    2 -> if (isLeapYear(year)) 29 else 28
//                    4, 6, 9, 11 -> 30
//                    else -> 31
//                }
//
//                if (year == startYear && month == startMonth) {
//                    totalDays += daysInMonth - startDay + 1
//                } else if (year == endYear && month == endMonth) {
//                    totalDays += endDay
//                } else {
//                    totalDays += daysInMonth
//                }
//            }
//        }
        //totalDays = daysSinceYearStart(endDay,endMonth,endYear) - daysSinceYearStart(startDay,startMonth,startYear)

        _noOfDays.value = days
        //println("The difference between $startDateString and $endDateString is $totalDays days")
    }


    private fun getBestPlacesFromInternet(){
        try {
                internetJob = viewModelScope.launch{
                val bestPlaces = AppApi.retrofitService.getBestPlaces()
                    _bestPlacesInternet.value = bestPlaces
                    }


        } catch (exception: Exception) {
            Log.e("my-tag",exception.message.toString(),exception)
            _exception.value = exception.toString()
            _etag.value =true
        }
    }


    init {
        viewModelScope.launch {
            getBestPlacesFromInternet()
            downloadHotelData()
            delay(3000)
            _isVisible.value =false
            delay(5000)
            //countNoOfDays()
            downloadPlaces()
        }

    }
}