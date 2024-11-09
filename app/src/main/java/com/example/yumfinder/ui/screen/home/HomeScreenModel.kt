package com.example.yumfinder.ui.screen.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.yumfinder.data.RestaurantDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@HiltViewModel
class HomeScreenModel @Inject constructor(
    val restaurantDAO: RestaurantDAO
) : ViewModel() {
    fun getAllRestaurants() = restaurantDAO.getAllRestaurants()

    fun getTimeElapsed(uploadDate: String): String {
        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)

        return try {
            // Parse the upload date
            val uploadedTime = dateFormat.parse(uploadDate) ?: return "Invalid date"

            val currentTime = Date()

            val timeDifference = currentTime.time - uploadedTime.time

            val days = TimeUnit.MILLISECONDS.toDays(timeDifference)
            val hours = TimeUnit.MILLISECONDS.toHours(timeDifference) % 24
            val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifference) % 60

            when {
                days > 0 ->
                    if (days == 1L) {
                        "$days day ago"
                    } else {
                        "$days days ago"
                    }
                hours > 0 ->
                    if (hours == 1L) {
                        "$hours hour ago"
                    } else {
                        "$hours hours ago"
                    }
                minutes > 0 -> "$minutes minutes ago"
                else -> "$seconds seconds ago"
            }
        } catch (e: Exception) {
            "Invalid date format"
        }
    }
}