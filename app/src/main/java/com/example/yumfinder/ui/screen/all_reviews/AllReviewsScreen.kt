package com.example.yumfinder.ui.screen.all_reviews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text

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

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.yumfinder.R
import com.example.yumfinder.data.RestaurantItem
import com.example.yumfinder.ui.screen.your_visits.ListModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllReviewsScreen(
    modifier: Modifier = Modifier,
    viewModel: ListModel = hiltViewModel(),
    onHomeAction: () -> Unit,
    onEditAction: (Int) -> Unit,
    onAddAction: () -> Unit
) {

    val visitedRestaurants by viewModel.getAllRestaurants().collectAsState(initial = emptyList())
    var sortedRestaurants by remember { mutableStateOf(visitedRestaurants) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(visitedRestaurants) {
        sortedRestaurants = visitedRestaurants
    }

    fun sortRestaurants() {
        sortedRestaurants = when (viewModel.selectedFilter) {
            "Rating" -> {
                if (viewModel.selectedFilterDescending) {
                    visitedRestaurants.sortedBy { it.restaurantRating.toFloat() }
                } else {
                    visitedRestaurants.sortedByDescending { it.restaurantRating.toFloat() }
                }
            }

            "Date" -> {
                if (viewModel.selectedFilterDescending) {
                    visitedRestaurants.sortedByDescending { it.visitedDate }
                } else {
                    visitedRestaurants.sortedBy { it.visitedDate }
                }
            }

            "Location" -> {
                if (viewModel.selectedFilterDescending) {
                    visitedRestaurants.sortedByDescending { it.restaurantAddress }
                } else {
                    visitedRestaurants.sortedBy { it.restaurantAddress }
                }
            }

            else -> visitedRestaurants
        }
    }

    fun searchRestaurants() {
        sortedRestaurants = visitedRestaurants.filter { restaurant ->
            restaurant.restaurantName.contains(searchQuery, ignoreCase = true) ||
                    restaurant.restaurantAddress.contains(searchQuery, ignoreCase = true) ||
                    restaurant.restaurantRating.contains(searchQuery, ignoreCase = true)
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
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 10.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(end = 10.dp)
                            )
                            Text(
                                text = stringResource(R.string.apoleats),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                        Text(
                            modifier = Modifier.padding(bottom = 10.dp),
                            style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                            text = "All Reviews"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                actions = {
                    IconButton(
                        onClick = {
                            onHomeAction()
                        }
                    ) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Home",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier
                .padding(innerPadding)
                .padding(top = 10.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .width(150.dp),
                    expanded = viewModel.showFilterDialog,
                    onExpandedChange = { viewModel.toggleFilterDialog() }
                ) {
                    OutlinedTextField(
                        value = viewModel.selectedFilter,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = viewModel.showFilterDialog) },
                        modifier = Modifier.menuAnchor(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = viewModel.showFilterDialog,
                        onDismissRequest = { viewModel.showFilterDialog = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    style = MaterialTheme.typography.titleLarge,
                                    text = "Rating"
                                )
                            },
                            onClick = {
                                viewModel.selectedFilter = "Rating"
                                sortRestaurants() // Sort based on Rating
                                viewModel.showFilterDialog = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    style = MaterialTheme.typography.titleLarge,
                                    text = "Date"
                                )
                            },
                            onClick = {
                                viewModel.selectedFilter = "Date"
                                sortRestaurants()
                                viewModel.showFilterDialog = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    style = MaterialTheme.typography.titleLarge,
                                    text = "Location"
                                )
                            },
                            onClick = {
                                viewModel.selectedFilter = "Location"
                                sortRestaurants()
                                viewModel.showFilterDialog = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight(500),
                                    text = if (viewModel.selectedFilterDescending) "Descending" else "Ascending"
                                )
                            },
                            onClick = {
                                viewModel.selectedFilterDescending =
                                    !viewModel.selectedFilterDescending
                                sortRestaurants() // Toggle sorting order and sort
                            }
                        )
                    }
                }

                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .weight(1f)
                        .height(55.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    placeholder = { Text("Search") },
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        searchRestaurants()
                    }
                )
            }

            val pairedRestaurants = sortedRestaurants.chunked(2)

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(pairedRestaurants) { pair ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(.95f)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RestaurantCard(
                            restaurant = pair[0],
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp),
                            onEditAction = onEditAction
                        )

                        if (pair.size > 1) {
                            RestaurantCard(
                                restaurant = pair[1],
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 4.dp),
                                onEditAction = onEditAction
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Button(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(0.9f)
                    .height(62.dp),
                onClick = { onAddAction() },
                shape = RoundedCornerShape(16.dp)
            )
            {
                Text("Add Restaurant", style = MaterialTheme.typography.labelMedium)
            }


            // Open dialog for adding new restaurant
        }
    }


}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RestaurantCard(restaurant: RestaurantItem, modifier: Modifier, onEditAction: (Int) -> Unit) {
    val name = if (restaurant.restaurantName.length > 20) {
        restaurant.restaurantName.substring(0, 17) + "..."
    } else {
        restaurant.restaurantName
    }

    val location = if (restaurant.restaurantAddress.length > 20) {
        restaurant.restaurantAddress.substring(0, 17) + "..."
    } else {
        restaurant.restaurantAddress
    }
    val rawDate = restaurant.visitedDate


    val inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
    var dateFormatted = ""
    try {
        val parsedDate = ZonedDateTime.parse(rawDate, inputFormatter)

        // Format to "Dec 01, 2024"
        val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
        val formattedDate = outputFormatter.format(parsedDate)
        dateFormatted = formattedDate // Use this in your UI

    } catch (e: Exception) {
        dateFormatted = rawDate
    }

    val backgroundColor = if (restaurant.restaurantRating.toFloat() > 9) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        modifier = modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .clickable { onEditAction(restaurant.id) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo), //TODO implement picture adding
                contentDescription = "Restaurant Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = location,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = dateFormatted,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = restaurant.restaurantRating,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}


