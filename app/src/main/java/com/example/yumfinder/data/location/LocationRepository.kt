package com.example.yumfinder.data.location

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getCurrentLocation(): Flow<LatLng>
}

class LocationRepositoryImpl(
    private val locationDataSource: LocationDataSource
) : LocationRepository {
    override fun getCurrentLocation(): Flow<LatLng> {
        return locationDataSource.getLocationUpdates()
    }
}