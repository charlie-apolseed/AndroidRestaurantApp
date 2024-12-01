package com.example.yumfinder.ui.screen.add_review

import android.util.Log
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yumfinder.R
import com.example.yumfinder.data.RestaurantDAO
import com.example.yumfinder.data.RestaurantItem
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddReviewModel @Inject constructor(
    val restaurantDAO: RestaurantDAO, savedStateHandle: SavedStateHandle
) : ViewModel() {


    init {
        viewModelScope.launch(Dispatchers.IO) {
            val id = savedStateHandle.get<String>("itemId")?.toIntOrNull()
            if (id != null) {
                existingRestaurant = restaurantDAO.getRestaurantById(id)
                _newLocation.value = LatLng(
                    existingRestaurant!!.restaurantLatitude, existingRestaurant!!.restaurantLongitude
                )
                newTitle = existingRestaurant!!.restaurantName
                newNotes = existingRestaurant!!.restaurantNotes
                newPriceRating = existingRestaurant!!.restaurantPriceRating
                newStaffRating = existingRestaurant!!.restaurantStaffRating
                newVibesRating = existingRestaurant!!.restaurantVibesRating
                newTasteRating = existingRestaurant!!.restaurantFoodRating
            } else {
                Log.d("AddReviewModel", "No ID found")
                val mostRecentRestaurant = restaurantDAO.getMostRecentRestaurant()
                _newLocation.value = LatLng(
                    mostRecentRestaurant.restaurantLatitude, mostRecentRestaurant.restaurantLongitude
                )
            }
        }
    }


    var existingRestaurant by mutableStateOf<RestaurantItem?>(null)
    private val _mostRecentRestaurant = MutableStateFlow<RestaurantItem?>(null)
    val mostRecentRestaurant: StateFlow<RestaurantItem?> = _mostRecentRestaurant

    private val defaultLocation = LatLng(41.3878, 2.1532) // Default location
    private val _newLocation = MutableStateFlow(defaultLocation)
    var newLocation: StateFlow<LatLng> = _newLocation
    var locationConfirmed by mutableStateOf(false)

    var geocodeText by mutableStateOf("Click to set new address")


    var newTitle by mutableStateOf("")
    var newTasteRating by mutableStateOf(0)
    var newVibesRating by mutableStateOf(0)
    var newStaffRating by mutableStateOf(0)
    var newPriceRating by mutableStateOf(0)
    var newNotes by mutableStateOf("")

    //Set new location to the existing review if it exists
    var markerPosition by mutableStateOf(LatLng(0.0, 0.0))


    fun confirmNewLocation() {
        locationConfirmed = true
        _newLocation.update { markerPosition }
    }

    fun deleteReview() {
        viewModelScope.launch(Dispatchers.IO) {
            restaurantDAO.delete(existingRestaurant!!)
        }
    }

    fun addReview() {
        if (geocodeText == "Click to set address") {
            geocodeText = mostRecentRestaurant.value?.restaurantAddress ?: "Unknown Address"
        }
        val overallRating =
            (newTasteRating.toFloat() + newVibesRating.toFloat() + newStaffRating.toFloat()) / 3
        val formattedRating = if ((newTasteRating + newVibesRating + newStaffRating) % 3 == 0) {
            overallRating.toInt().toString() // Convert to an integer string if it's a whole number
        } else {
            String.format("%.1f", overallRating) // Format to 1 decimal place otherwise
        }

        viewModelScope.launch(Dispatchers.IO) {
            val newRestaurant = RestaurantItem(
                id = existingRestaurant?.id ?: 0, // Use existing ID if present, else 0 for a new entry
                restaurantName = newTitle,
                restaurantAddress = geocodeText,
                restaurantLatitude = newLocation.value.latitude,
                restaurantLongitude = newLocation.value.longitude,
                restaurantFoodRating = newTasteRating,
                restaurantVibesRating = newVibesRating,
                restaurantStaffRating = newStaffRating,
                restaurantPriceRating = newPriceRating,
                restaurantRating = formattedRating,
                restaurantNotes = newNotes,
                restaurantImage = R.drawable.logo,
                restaurantReviewer = Firebase.auth.currentUser?.email ?: "Unknown",
                createdDate = existingRestaurant?.createdDate ?: Date(System.currentTimeMillis()).toString(),
                visitedDate = Date(System.currentTimeMillis()).toString()
            )

            // Determine whether to update or insert
            if (existingRestaurant != null) {
                restaurantDAO.update(newRestaurant) // Update existing restaurant
                Log.d("AddReviewModel", "Restaurant updated: $newRestaurant")
            } else {
                restaurantDAO.insert(newRestaurant) // Insert new restaurant
                Log.d("AddReviewModel", "New restaurant added: $newRestaurant")
            }
        }
    }
}
