package com.example.yumfinder.ui.screen.add_review

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yumfinder.data.RestaurantDAO
import com.example.yumfinder.data.RestaurantItem
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddReviewModel @Inject constructor(
    val restaurantDAO: RestaurantDAO
    , savedStateHandle: SavedStateHandle
) : ViewModel() {
    init {
        //Add logic to check if there was a review being edited. If so, do not execute this
        //and instead set the newLocation to the existing review's location
        viewModelScope.launch {
            getMostRecentRestaurant()
                .collect { restaurant ->
                    _mostRecentRestaurant.value = restaurant
                    _newLocation.value = restaurant?.let {
                        LatLng(it.restaurantLatitude, it.restaurantLongitude)
                    } ?: defaultLocation
                }
        }
    }

    private val _mostRecentRestaurant = MutableStateFlow<RestaurantItem?>(null)
    val mostRecentRestaurant: StateFlow<RestaurantItem?> = _mostRecentRestaurant

    private val defaultLocation = LatLng(41.3878, 2.1532) // Default location
    private val _newLocation = MutableStateFlow(defaultLocation)
    var newLocation: StateFlow<LatLng> = _newLocation

    var newTitle by  mutableStateOf("")
    var newTasteRating by mutableStateOf(0)
    var newVibesRating by  mutableStateOf(0)
    var newStaffRating by  mutableStateOf(0)
    var newPriceRating by  mutableStateOf(0)
    var newNotes by  mutableStateOf("")

    //Set new location to the existing review if it exists
    var markerPosition by mutableStateOf(LatLng(0.0, 0.0))


    fun updateLocation() {
        _newLocation.update{ markerPosition}
    }

    fun getMostRecentRestaurant() = restaurantDAO.getMostRecentRestaurant()


}
