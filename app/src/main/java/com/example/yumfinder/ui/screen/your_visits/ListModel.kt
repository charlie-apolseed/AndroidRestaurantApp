package com.example.yumfinder.ui.screen.your_visits


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.yumfinder.data.RestaurantDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListModel @Inject constructor(
    val restaurantDAO: RestaurantDAO
) : ViewModel() {
    var showFilterDialog by mutableStateOf(false)
    var selectedFilter by mutableStateOf("Filter")
    var selectedFilterDescending by mutableStateOf(false)




    fun toggleFilterDialog() {
        showFilterDialog = !showFilterDialog
    }

    fun getAllUserRestaurants(reviewer: String) = restaurantDAO.getUserReviews(reviewer)

    fun getAllRestaurants() = restaurantDAO.getAllReviews()



}

