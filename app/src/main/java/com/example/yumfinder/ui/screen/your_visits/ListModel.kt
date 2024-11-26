package com.example.yumfinder.ui.screen.your_visits

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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ListModel @Inject constructor(
    val restaurantDAO: RestaurantDAO
    , savedStateHandle: SavedStateHandle
) : ViewModel() {
    var showAddDialog by mutableStateOf(false)
    var showFilterDialog by mutableStateOf(false)
    var selectedFilter by mutableStateOf("Filter")
    var selectedFilterDescending by mutableStateOf(false)

    init {
        showAddDialog = savedStateHandle.get<String>("addDialog").toBoolean()
    }

    fun toggleAddDialog() {
        showAddDialog = !showAddDialog
    }

    fun toggleFilterDialog() {
        showFilterDialog = !showFilterDialog
    }

    fun getAllUserRestaurants(reviewer: String) = restaurantDAO.getAllUserRestaurants(reviewer)

    fun getAllRestaurants() = restaurantDAO.getAllRestaurants()


    fun addRestaurant(name: String, location: String, rating: String, notes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newRestaurant = RestaurantItem(
                restaurantName = name,
                restaurantAddress = location,
                restaurantRating = rating,
                restaurantFoodRating = rating.toFloat(),
                restaurantVibesRating = rating.toFloat(),
                restaurantStaffRating = rating.toFloat(),
                restaurantLatitude = 0.0,
                restaurantLongitude = 0.0,
                restaurantNotes = notes,
                restaurantImage = R.drawable.logo,
                restaurantReviewer = Firebase.auth.currentUser?.email ?: "Unknown",
                createdDate = Date(System.currentTimeMillis()).toString(),
                visitedDate = Date(System.currentTimeMillis()).toString()
            )
            Log.d("ListModel", "Adding restaurant: $newRestaurant")
            restaurantDAO.insert(newRestaurant)
        }
    }
}

