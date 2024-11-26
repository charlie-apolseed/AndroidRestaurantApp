package com.example.yumfinder.ui.screen.add_review

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.yumfinder.data.RestaurantDAO
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddReviewModel @Inject constructor(
    val restaurantDAO: RestaurantDAO
    , savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun getMostRecentRestaurant() = restaurantDAO.getMostRecentRestaurant()
}
