package com.example.yumfinder.ui.screen.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.yumfinder.R
import com.example.yumfinder.data.RestaurantItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenModel = hiltViewModel(),
    onListAction: () -> Unit,
    onAddAction: () -> Unit,
    onAllReviewsAction: () -> Unit,
    onAIRecommendationAction: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = true,
                zoomGesturesEnabled = true,
            )
        )
    }
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isTrafficEnabled = false,
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                    context, R.raw.mymapstyle
                )
            )
        )
    }

    val visitedRestaurants by viewModel.getAllRestaurants().collectAsState(initial = emptyList())

    val initialCameraPosition = remember(visitedRestaurants) {
        if (visitedRestaurants.isNotEmpty()) {
            Log.d("HomeScreen", "Initial camera position set to last location: ${visitedRestaurants.last()}")
            CameraPosition.fromLatLngZoom(
                LatLng(
                    visitedRestaurants.last().restaurantLatitude,
                    visitedRestaurants.last().restaurantLongitude
                ), 8f // Adjust initial zoom as needed
            )
        } else {
            Log.d("HomeScreen", "Initial camera position set to default location")
            CameraPosition.fromLatLngZoom(LatLng(43.739599157808485, -70.04142321646214), 8f) // Default location
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = initialCameraPosition
    }

    // Use LaunchedEffect to trigger animation when the screen is resumed
    LaunchedEffect(lifecycleOwner.lifecycle.currentState) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            if (visitedRestaurants.isNotEmpty()) {
                val lastLocation = LatLng(
                    visitedRestaurants.last().restaurantLatitude,
                    visitedRestaurants.last().restaurantLongitude
                )
                Log.d("HomeScreen", "Animating camera to last location on resume: $lastLocation")
                cameraPositionState.animate(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition(lastLocation, 8f, 0f, 0f) // Adjust zoom as needed
                    )
                )
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(end = 10.dp)
                            )
                            Text(
                                text = "| ApolEats",
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                        Text(
                            modifier = Modifier.padding(bottom = 10.dp),
                            style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                            text = "Find your next favorite restaurant"
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            GoogleMap(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(.6f),
                cameraPositionState = cameraPositionState,
                uiSettings = uiSettings,
                properties = mapProperties
            ) {
                for (review in visitedRestaurants) {
                    Marker(
                        state = MarkerState(
                            position = LatLng(
                                review.restaurantLatitude,
                                review.restaurantLongitude
                            )
                        ),
                        title = review.restaurantName,
                        snippet = review.restaurantRating,
                        icon = viewModel.getMarkerColor(
                            review.restaurantRating)
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Button(
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth(0.55f) // Width for "Restaurant List" button
                        .height(50.dp), // Same height
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 5.dp
                    ),
                    onClick = {
                        onListAction()
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            fontSize = 18.sp,
                            fontWeight = FontWeight(600),
                            text = "Your Visits"
                        )
                        Image(
                            painter = painterResource(id = R.drawable.list),
                            contentDescription = "List",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(15.dp))

                Button(
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth()
                        .height(50.dp), // Same height for consistency
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 5.dp
                    ),
                    onClick = {
                        onAddAction()
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            fontSize = 18.sp,
                            fontWeight = FontWeight(600),
                            text = "New"
                        )
                        Image(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = "Add",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }

            Button(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth(.9f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 5.dp
                ),
                onClick = {
                    onAIRecommendationAction()
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        fontSize = 18.sp,
                        fontWeight = FontWeight(600),
                        text = "AI Recommendation"
                    )
                    Image(
                        painter = painterResource(id = R.drawable.suggestion),
                        contentDescription = "Recommendation",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 15.dp)
                    .heightIn(200.dp)
                    .clickable { onAllReviewsAction() },
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(5.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight(700),
                    text = "Recent Activity"
                )

                for (restaurant in visitedRestaurants.take(3).reversed()) {
                    RestaurantCard(
                        restaurant = restaurant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 1.dp),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun RestaurantCard(
    restaurant: RestaurantItem,
    modifier: Modifier,
    viewModel: HomeScreenModel
) {
    val timeElapsed = viewModel.getTimeElapsed(restaurant.visitedDate)
    val formattedName = if (restaurant.restaurantName.length > 25) {
        restaurant.restaurantName.substring(0, 23) + "..."
    } else {
        restaurant.restaurantName
    }


    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    fontWeight = FontWeight(700),
                    fontSize = 20.sp,
                    text = restaurant.restaurantRating
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    fontWeight = FontWeight(700),
                    fontSize = 15.sp,
                    text = formattedName
                )
            }
            Text(
                text = timeElapsed,
                style = TextStyle(color = Color.Gray),
                fontSize = 12.sp,
                fontWeight = FontWeight(600)
            )
        }
    }
}