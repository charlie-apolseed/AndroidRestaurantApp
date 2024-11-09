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
    @ColumnInfo(name = "restaurant_rating") val restaurantRating: String,
    @ColumnInfo(name = "restaurant_reviewer") val restaurantReviewer: String,
    @ColumnInfo(name = "restaurant_notes") val restaurantNotes: String,
    @ColumnInfo(name = "restaurant_favorite") val restaurantFavorite: Boolean,
    @ColumnInfo(name = "restaurant_image") val restaurantImage: Int,
    @ColumnInfo(name = "created_date") val createdDate: String,
    @ColumnInfo(name = "visited_date") val visitedDate: String
    ) : Serializable


