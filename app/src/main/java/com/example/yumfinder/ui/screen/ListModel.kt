package com.example.yumfinder.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yumfinder.R
import com.example.yumfinder.data.RestaurantDAO
import com.example.yumfinder.data.RestaurantItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ListModel @Inject constructor(
    val restaurantDAO: RestaurantDAO
    , val savedStateHandle: SavedStateHandle
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




    fun getAllRestaurants() = restaurantDAO.getAllRestaurants()
    fun getRestaurant(id: Int) = restaurantDAO.getRestaurant(id)

    fun addRestaurant(name: String, location: String, rating: String, notes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newRestaurant = RestaurantItem(
                restaurantName = name,
                restaurantAddress = location,
                restaurantRating = rating,
                restaurantNotes = notes,
                restaurantFavorite = false,
                restaurantImage = R.drawable.logo,
                restaurantDate = Date(System.currentTimeMillis()).toString()
            )
            Log.d("ListModel", "Adding restaurant: $newRestaurant")
            restaurantDAO.insert(newRestaurant)
        }
    }

    fun deleteRestaurant(restaurant: RestaurantItem) {
        viewModelScope.launch(Dispatchers.IO) {
            restaurantDAO.delete(restaurant)
        }
    }

    fun updateRestaurant(restaurant: RestaurantItem) {
        viewModelScope.launch(Dispatchers.IO) {
            restaurantDAO.update(restaurant)
        }
    }

    fun toggleFavorite(restaurant: RestaurantItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedRestaurant =
                restaurant.copy(restaurantFavorite = !restaurant.restaurantFavorite)
            restaurantDAO.update(updatedRestaurant)
        }
    }

    fun deleteAllRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            restaurantDAO.deleteAll()
        }
    }
}

