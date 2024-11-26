package com.example.yumfinder.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface RestaurantDAO {
    @Query("SELECT * FROM restaurant_table ORDER BY visited_date")
    fun getAllRestaurants() : Flow<List<RestaurantItem>>

    @Query("SELECT * FROM restaurant_table ORDER BY visited_date DESC LIMIT 1")
    fun getMostRecentRestaurant(): Flow<RestaurantItem?>

    @Query("SELECT * FROM restaurant_table WHERE restaurant_reviewer = :reviewer ORDER BY visited_date")
    fun getAllUserRestaurants(reviewer: String) : Flow<List<RestaurantItem>>

    @Query("SELECT * from restaurant_table WHERE id = :id")
    suspend fun getRestaurantById(id: Int): RestaurantItem


    @Query("SELECT COUNT(*) from restaurant_table")
    suspend fun getRestaurantsNum(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(restaurant: RestaurantItem)

    @Update
    suspend fun update(restaurant: RestaurantItem)

    @Delete
    suspend fun delete(restaurant: RestaurantItem)

    @Query("DELETE from restaurant_table")
    suspend fun deleteAll()
}