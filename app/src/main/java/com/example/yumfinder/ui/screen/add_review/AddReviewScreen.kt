package com.example.yumfinder.ui.screen.add_review

import android.Manifest
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.yumfinder.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddReviewScreen(
    modifier: Modifier = Modifier,
    onHomeAction: () -> Unit,
    onBackAction: () -> Unit,
    viewmodel: AddReviewModel = hiltViewModel()
) {
    var newTitle by rememberSaveable { mutableStateOf("") }
    var newTasteRating by rememberSaveable { mutableStateOf(0) }
    var newVibesRating by rememberSaveable { mutableStateOf(0) }
    var newStaffRating by rememberSaveable { mutableStateOf(0) }
    var newPriceRating by rememberSaveable { mutableStateOf(0) }
    var newNotes by rememberSaveable { mutableStateOf("") }
    //Map states
    val context = LocalContext.current

    val visitedRestaurant by viewmodel.getMostRecentRestaurant().collectAsState(initial = null)
    val startLocation by remember {
        mutableStateOf(
            if (visitedRestaurant != null) {
                LatLng(visitedRestaurant!!.restaurantLatitude, visitedRestaurant!!.restaurantLongitude)
            } else {
                LatLng(41.3878, 2.1532) // Default location
            }
        )
    }



    var cameraState = rememberCameraPositionState {
        CameraPosition.fromLatLngZoom(
          startLocation, 18f
        )
    }
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

    LaunchedEffect(Unit) {
        cameraState.animate(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition(startLocation, 15f, 0f, 0f)
            )
        )
    }



    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        IconButton(onClick = { onBackAction() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier.size(32.dp)
                            )
                        }

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
                                text = "Add New Review"
                            )
                        }

                        IconButton(onClick = { onHomeAction() }) {
                            Icon(
                                Icons.Filled.Home,
                                contentDescription = "Home",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // TextField for Title
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                value = newTitle,
                onValueChange = { newTitle = it },
                label = { Text("Name") }
            )
            GoogleMap(
                modifier = Modifier
                    .height(400.dp)
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                cameraPositionState = cameraState,
                uiSettings= uiSettings,
                properties = mapProperties
            )

            // LazyColumn for Ratings and Notes
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Taste
                item {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,
                        modifier = modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Taste:",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (i in 1..10) {
                                TextButton(
                                    onClick = {
                                        newTasteRating = i
                                    },
                                    contentPadding =
                                    if (i == 10) {
                                        PaddingValues(
                                            start = 2.dp,
                                            end = 2.dp,
                                            top = 12.dp,
                                            bottom = 12.dp
                                        )
                                    } else {
                                        PaddingValues(
                                            start = 8.dp,
                                            end = 8.dp,
                                            top = 12.dp,
                                            bottom = 12.dp
                                        )
                                    },


                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (i == newTasteRating) {
                                            MaterialTheme.colorScheme.onTertiary // Highlighted color
                                        } else {
                                            MaterialTheme.colorScheme.tertiary // Default color
                                        },
                                        contentColor = Color.Black // Ensure text is always visible
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(64.dp),
                                    shape =
                                    when (i) {
                                        1 -> RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 0.dp,
                                            bottomStart = 16.dp,
                                            bottomEnd = 0.dp
                                        )

                                        10 -> RoundedCornerShape(
                                            topStart = 0.dp,
                                            topEnd = 16.dp,
                                            bottomStart = 0.dp,
                                            bottomEnd = 16.dp
                                        )

                                        else ->
                                            RectangleShape
                                    }
                                ) {
                                    Column() {
                                        Text(text = "$i", color = Color.Black, fontSize = 16.sp)
                                    }
                                }
                            }

                        }
                    }

                }


                // Vibes
                item {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Vibes:",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (i in 1..10) {
                                TextButton(
                                    onClick = {
                                        newVibesRating = i
                                    },
                                    contentPadding =
                                    if (i == 10) {
                                        PaddingValues(
                                            start = 2.dp,
                                            end = 2.dp,
                                            top = 12.dp,
                                            bottom = 12.dp
                                        )
                                    } else {
                                        PaddingValues(
                                            start = 8.dp,
                                            end = 8.dp,
                                            top = 12.dp,
                                            bottom = 12.dp
                                        )
                                    },


                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (i == newVibesRating) {
                                            MaterialTheme.colorScheme.onTertiary // Highlighted color
                                        } else {
                                            MaterialTheme.colorScheme.tertiary // Default color
                                        },
                                        contentColor = Color.Black // Ensure text is always visible
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(64.dp),
                                    shape =
                                    when (i) {
                                        1 -> RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 0.dp,
                                            bottomStart = 16.dp,
                                            bottomEnd = 0.dp
                                        )

                                        10 -> RoundedCornerShape(
                                            topStart = 0.dp,
                                            topEnd = 16.dp,
                                            bottomStart = 0.dp,
                                            bottomEnd = 16.dp
                                        )

                                        else ->
                                            RectangleShape
                                    }
                                ) {
                                    Column() {
                                        Text(text = "$i", color = Color.Black, fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // Staff
                item {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Staff:",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (i in 1..10) {
                                TextButton(
                                    onClick = {
                                        newStaffRating = i
                                    },
                                    contentPadding =
                                    if (i == 10) {
                                        PaddingValues(
                                            start = 2.dp,
                                            end = 2.dp,
                                            top = 12.dp,
                                            bottom = 12.dp
                                        )
                                    } else {
                                        PaddingValues(
                                            start = 8.dp,
                                            end = 8.dp,
                                            top = 12.dp,
                                            bottom = 12.dp
                                        )
                                    },


                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (i == newStaffRating) {
                                            MaterialTheme.colorScheme.onTertiary // Highlighted color
                                        } else {
                                            MaterialTheme.colorScheme.tertiary // Default color
                                        },
                                        contentColor = Color.Black // Ensure text is always visible
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(64.dp),
                                    shape =
                                    when (i) {
                                        1 -> RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 0.dp,
                                            bottomStart = 16.dp,
                                            bottomEnd = 0.dp
                                        )

                                        10 -> RoundedCornerShape(
                                            topStart = 0.dp,
                                            topEnd = 16.dp,
                                            bottomStart = 0.dp,
                                            bottomEnd = 16.dp
                                        )

                                        else ->
                                            RectangleShape
                                    }
                                ) {
                                    Column() {
                                        Text(text = "$i", color = Color.Black, fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // Price
                item {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Price:",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (i in 1..5) {
                                TextButton(
                                    onClick = {
                                        newPriceRating = i
                                    },
                                    contentPadding =
                                    if (i == 5) {
                                        PaddingValues(
                                            start = 2.dp,
                                            end = 2.dp,
                                            top = 12.dp,
                                            bottom = 12.dp
                                        )
                                    } else {
                                        PaddingValues(
                                            start = 8.dp,
                                            end = 8.dp,
                                            top = 12.dp,
                                            bottom = 12.dp
                                        )
                                    },


                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (i == newPriceRating) {
                                            MaterialTheme.colorScheme.onTertiary // Highlighted color
                                        } else {
                                            MaterialTheme.colorScheme.tertiary // Default color
                                        },
                                        contentColor = Color.Black // Ensure text is always visible
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(96.dp),
                                    shape =
                                    when (i) {
                                        1 -> RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 0.dp,
                                            bottomStart = 16.dp,
                                            bottomEnd = 0.dp
                                        )

                                        5 -> RoundedCornerShape(
                                            topStart = 0.dp,
                                            topEnd = 16.dp,
                                            bottomStart = 0.dp,
                                            bottomEnd = 16.dp
                                        )

                                        else ->
                                            RectangleShape
                                    }
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    )
                                    {
                                        when (i) {
                                            1 -> {
                                                Image(
                                                    painter = painterResource(id = R.drawable.bread),
                                                    contentDescription = "$",
                                                    modifier = Modifier
                                                        .size(50.dp)
                                                        .padding(bottom = 1.dp)
                                                )
                                                Text(fontSize = 12.sp, text = "$")
                                            }

                                            2 -> {
                                                Image(
                                                    painter = painterResource(id = R.drawable.soup),
                                                    contentDescription = "$$",
                                                    modifier = Modifier
                                                        .size(50.dp)
                                                        .padding(bottom = 1.dp)
                                                )
                                                Text(fontSize = 12.sp, text = "$$")
                                            }

                                            3 -> {
                                                Image(
                                                    painter = painterResource(id = R.drawable.burger),
                                                    contentDescription = "$$$",
                                                    modifier = Modifier
                                                        .size(50.dp)
                                                        .padding(bottom = 1.dp)
                                                )
                                                Text(fontSize = 12.sp, text = "$$$")
                                            }

                                            4 -> {
                                                Image(
                                                    painter = painterResource(id = R.drawable.steak),
                                                    contentDescription = "$$$$",
                                                    modifier = Modifier
                                                        .size(50.dp)
                                                        .padding(bottom = 1.dp)
                                                )
                                                Text(fontSize = 12.sp, text = "$$$$")
                                            }

                                            5 -> {
                                                Image(
                                                    painter = painterResource(id = R.drawable.caviar),
                                                    contentDescription = "$$$$$",
                                                    modifier = Modifier
                                                        .size(50.dp)
                                                        .padding(bottom = 1.dp)
                                                )
                                                Text(fontSize = 12.sp, text = "$$$$$")
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

                // Notes
                item {
                    Text(
                        text = "Notes:",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                        modifier = modifier.fillMaxWidth()
                    )
                }
                item {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.onTertiary, // Background when focused
                            unfocusedContainerColor = MaterialTheme.colorScheme.tertiary, // Background when unfocused
                            focusedIndicatorColor = Color.Transparent, // Transparent line when focused
                            unfocusedIndicatorColor = Color.Transparent, // Transparent line when unfocused
                            errorIndicatorColor = Color.Transparent // Transparent line in error state
                        ),
                        shape = RoundedCornerShape(16.dp), // Adds rounded corners
                        value = newNotes,
                        onValueChange = { newNotes = it },
                        placeholder = { Text("Type to enter...") } // Placeholder text
                    )
                }
                item {
                    Button(
                        modifier = modifier.fillMaxWidth().padding(bottom = 20.dp),
                        shape = RoundedCornerShape(16.dp),
                        onClick = { /*Submit review*/ }
                    ) {
                        Text("Submit Review")
                    }
                }
            }
        }
    }
}

