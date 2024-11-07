package com.example.yumfinder.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.example.yumfinder.data.RestaurantItem

@Dao
interface RestaurantDAO {
    @Query("SELECT * FROM restaurant_table ORDER BY restaurant_date")
    fun getAllRestaurants() : Flow<List<RestaurantItem>>

    @Query("SELECT * from restaurant_table WHERE id = :id")
    fun getRestaurant(id: Int): Flow<RestaurantItem>

    @Query("SELECT COUNT(*) from restaurant_table")
    suspend fun getRestaurantsNum(): Int

    @Query("SELECT COUNT(*) from restaurant_table WHERE restaurant_favorite = 1")
    suspend fun getFavoriteRestaurantsNum(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(restaurant: RestaurantItem)

    @Update
    suspend fun update(restaurant: RestaurantItem)

    @Delete
    suspend fun delete(restaurant: RestaurantItem)

    @Query("DELETE from restaurant_table")
    suspend fun deleteAll()
}