package com.example.yumfinder.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "restaurant_table")
data class RestaurantItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "restaurant_name") val restaurantName: String,
    @ColumnInfo(name = "restaurant_address") val restaurantAddress: String,
    @ColumnInfo(name = "restaurant_food_rating") val restaurantFoodRating: Float,
    @ColumnInfo(name = "restaurant_vibes_rating") val restaurantVibesRating: Float,
    @ColumnInfo(name = "restaurant_staff_rating") val restaurantStaffRating: Float,
    @ColumnInfo(name = "restaurant_rating") val restaurantRating: String,
    @ColumnInfo(name = "restaurant_latitude") val restaurantLatitude: Double,
    @ColumnInfo(name = "restaurant_longitude") val restaurantLongitude: Double,
    @ColumnInfo(name = "restaurant_reviewer") val restaurantReviewer: String,
    @ColumnInfo(name = "restaurant_notes") val restaurantNotes: String,
    @ColumnInfo(name = "restaurant_image") val restaurantImage: Int,
    @ColumnInfo(name = "created_date") val createdDate: String,
    @ColumnInfo(name = "visited_date") val visitedDate: String
    ) : Serializable


