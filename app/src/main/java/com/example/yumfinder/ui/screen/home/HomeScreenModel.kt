package com.example.yumfinder.ui.screen.home

import androidx.lifecycle.ViewModel
import com.example.yumfinder.data.RestaurantDAO
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
    fun getAllRestaurants() = restaurantDAO.getAllReviews()

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

    fun getMarkerColor(rating: String): BitmapDescriptor {
        val ratingValue = rating.toFloatOrNull()
        return if (ratingValue != null && ratingValue > 9) {
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)
        } else {
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        }
    }
}