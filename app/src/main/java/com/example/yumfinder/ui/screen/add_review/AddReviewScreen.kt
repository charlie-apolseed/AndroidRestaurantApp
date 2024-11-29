package com.example.yumfinder.ui.screen.add_review


import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.yumfinder.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddReviewScreen(
    modifier: Modifier = Modifier,
    onHomeAction: () -> Unit,
    onBackAction: () -> Unit,
    viewmodel: AddReviewModel = hiltViewModel()
) {
    //Map states
    val context = LocalContext.current
    val newLocation by viewmodel.newLocation.collectAsState()
    var geocodeText by rememberSaveable {
        mutableStateOf("Click to set address")
    }


    var cameraState = rememberCameraPositionState {
        CameraPosition.fromLatLngZoom(
            newLocation, 18f
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
    val mapProperties by remember {
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
                CameraPosition(newLocation, 15f, 0f, 0f)
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
                value = viewmodel.newTitle,
                onValueChange = { viewmodel.newTitle = it },
                label = { Text("Name") }
            )
            GoogleMap(
                modifier = Modifier
                    .height(350.dp)
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                cameraPositionState = cameraState,
                uiSettings = uiSettings,
                properties = mapProperties,
                onMapClick = {
                    viewmodel.locationConfirmed = false
                    viewmodel.markerPosition = it
                    val geocoder = Geocoder(context, Locale.getDefault())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        geocoder.getFromLocation(
                            it.latitude,
                            it.longitude,
                            3,
                            object : Geocoder.GeocodeListener {
                                override fun onGeocode(addrs: MutableList<Address>) {
                                    val addr =
                                        addrs[0].getAddressLine(0)

                                    geocodeText = addr
                                }

                                override fun onError(errorMessage: String?) {
                                    geocodeText = errorMessage!!
                                    super.onError(errorMessage)

                                }
                            }
                        )
                    }
                }
            ) {
                Marker(
                    state = MarkerState(position = viewmodel.markerPosition),
                    title = viewmodel.newTitle,
                    alpha = 1f,
                )
                if (!viewmodel.locationConfirmed) {
                    Marker(
                        state = MarkerState(position = newLocation),
                        title = viewmodel.newTitle,
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA),
                        alpha = 1f,
                    )
                } else {
                    Marker(
                        state = MarkerState(position = newLocation),
                        title = "Current location",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA),
                        alpha = 1f,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .height(70.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = geocodeText, color = Color.Black,
                    fontWeight = if (viewmodel.locationConfirmed) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth(.6f)
                )
                Button(
                    onClick = {
                        viewmodel.confirmNewLocation()
                    },
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                        .padding(vertical = 10.dp)
                        .fillMaxHeight()
                    ,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 5.dp
                    )
                ) {
                    Text(
                        fontSize = 18.sp,
                        fontWeight = FontWeight(600),
                        text = "Confirm"
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(top = 6.dp),
                thickness = 1.dp
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
                                        viewmodel.newTasteRating = i
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
                                        containerColor = if (i == viewmodel.newTasteRating) {
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
                                        viewmodel.newVibesRating = i
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
                                        containerColor = if (i == viewmodel.newVibesRating) {
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
                                        viewmodel.newStaffRating = i
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
                                        containerColor = if (i == viewmodel.newStaffRating) {
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
                                        viewmodel.newPriceRating = i
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
                                        containerColor = if (i == viewmodel.newPriceRating) {
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
                        value = viewmodel.newNotes,
                        onValueChange = { viewmodel.newNotes = it },
                        placeholder = { Text("Type to enter...") } // Placeholder text
                    )
                }
                item {
                    Button(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 5.dp
                        ),
                        onClick = { /*Submit review*/ }
                    ) {
                        Text("Submit Review")
                    }
                }
            }
        }
    }
}

