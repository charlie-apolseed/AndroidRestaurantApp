package com.example.yumfinder.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


data class Restaurant(
    val name: String,
    val location: String,
    val rating: String,
    val notes: String,
    var favorite: Boolean = false
)

class ListModel : ViewModel() {
    var visitedRestaurants by mutableStateOf<List<Restaurant>>(emptyList())


    fun addRestaurant(name: String, location: String, rating: String, notes: String) {
        val newRestaurant = Restaurant(name = name, location = location, rating = rating, notes = notes)
        visitedRestaurants = visitedRestaurants + newRestaurant
    }

    fun toggleFavorite(restaurant: Restaurant) {
        visitedRestaurants = visitedRestaurants.map {
            if (it == restaurant) it.copy(favorite = !it.favorite) else it
        }
    }
}