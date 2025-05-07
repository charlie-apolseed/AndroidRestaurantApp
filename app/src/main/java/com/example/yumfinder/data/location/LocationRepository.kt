package com.example.yumfinder.data.location

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocationRepository {
    fun getCurrentLocation(): Flow<LatLng>
}

class LocationRepositoryImpl @Inject constructor(
    private val locationDataSource: LocationDataSource
) : LocationRepository {
    override fun getCurrentLocation(): Flow<LatLng> {
        return locationDataSource.getLocationUpdates()
    }
}