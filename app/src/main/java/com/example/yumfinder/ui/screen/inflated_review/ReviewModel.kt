package com.example.yumfinder.ui.screen.inflated_review

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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ReviewModel @Inject constructor(
    val restaurantDAO: RestaurantDAO, private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var restaurantToEdit by mutableStateOf<RestaurantItem?>(null)


    fun getRestaurantToEdit() {
        viewModelScope.launch {
            val id = savedStateHandle.get<String>("itemId")?.toIntOrNull()
            if (id != null) {
                restaurantToEdit = restaurantDAO.getRestaurantById(id)
            }
        }
    }



    fun confirmEdit(oldRestaurant: RestaurantItem, editedRestaurant: RestaurantItem) {

    }
}
