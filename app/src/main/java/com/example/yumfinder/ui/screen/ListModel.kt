package com.example.yumfinder.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


data class Restaurant(
    val name: String,
    val location: String,
    val rating: String,
    val notes: String,
    var favorite: Boolean = false
)

class ListModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    var addDialog by mutableStateOf(false)
    var visitedRestaurants = mutableStateListOf<Restaurant>()

    init {
        addDialog = savedStateHandle.get<String>("addDialog").toBoolean() ?: false
    }

    fun toggleAddDialog() {
        addDialog = !addDialog
    }


    fun addRestaurant(name: String, location: String, rating: String, notes: String) {
        val newRestaurant = Restaurant(name = name, location = location, rating = rating, notes = notes)
        visitedRestaurants.add(newRestaurant) // Add directly to the state list
        Log.d("ListModel", "Added restaurant: $newRestaurant")
        Log.d("ListModel", "All Restaurants: $visitedRestaurants")
    }

    fun toggleFavorite(restaurant: Restaurant) {
        val index = visitedRestaurants.indexOf(restaurant)
        if (index >= 0) {
            visitedRestaurants[index] = visitedRestaurants[index].copy(favorite = !restaurant.favorite)
        }
    }
}