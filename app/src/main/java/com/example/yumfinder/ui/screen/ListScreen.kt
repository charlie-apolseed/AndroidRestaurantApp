package com.example.yumfinder.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yumfinder.R
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    viewModel: ListModel = viewModel(),
    onHomeAction: () -> Unit
) {

    var visitedRestaurants = viewModel.visitedRestaurants

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
                            modifier = Modifier.size(32.dp) // Size of the top action icon
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
            val pairedRestaurants = visitedRestaurants.chunked(2)

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
                                .padding(end = 4.dp)
                        )

                        if (pair.size > 1) {
                            RestaurantCard(
                                restaurant = pair[1],
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 4.dp)
                            )
                        } else {
                            // Optional: Add an empty Spacer to keep alignment if only one item
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
                onClick = { viewModel.toggleAddDialog() },
                shape = RoundedCornerShape(16.dp)
            )
            {
                Text("Add Restaurant", style = MaterialTheme.typography.labelMedium)
            }


            // Open dialog for adding new restaurant
            if (viewModel.addDialog) {
                AddRestaurantDialog(viewModel) {
                    viewModel.toggleAddDialog()
                }
            }
        }
    }


}


@Composable
fun RestaurantCard(restaurant: Restaurant, modifier: Modifier) {
    val name = if (restaurant.name.length > 20) {
        restaurant.name.substring(0, 17) + "..."
    } else {
        restaurant.name
    }

    val location = if (restaurant.location.length > 20) {
        restaurant.location.substring(0, 17) + "..."
    } else {
        restaurant.location
    }
    val formatter = SimpleDateFormat("d MMMM, yyyy", Locale.getDefault())
    val formattedDate = formatter.format(restaurant.date)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = restaurant.image),
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
                        text = formattedDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = restaurant.rating,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}


@Composable
fun AddRestaurantDialog(
    viewModel: ListModel,
    onCancel: () -> Unit
) {
    var newRestaurantName by remember { mutableStateOf("") }
    var newRestaurantLocation by remember { mutableStateOf("") }
    var newRestaurantRating by remember { mutableStateOf("") }
    var newRestaurantNotes by remember { mutableStateOf("") }
    var dialogErrorText by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onCancel) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 6.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Restaurant Review",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )
                OutlinedTextField(
                    value = newRestaurantName,
                    onValueChange = { newRestaurantName = it },
                    label = { Text("Name") }
                )
                OutlinedTextField(
                    value = newRestaurantLocation,
                    onValueChange = { newRestaurantLocation = it },
                    label = { Text("Location") }
                )
                OutlinedTextField(
                    value = newRestaurantRating,
                    onValueChange = { newRestaurantRating = it },
                    label = { Text("Rating (1-10)") }
                )
                OutlinedTextField(
                    value = newRestaurantNotes,
                    onValueChange = { newRestaurantNotes = it },
                    label = { Text("Notes") }
                )
                if (dialogErrorText) {
                    Text(
                        text = "Please fill in all fields",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    ,
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        if (newRestaurantName.isNotBlank() && newRestaurantLocation.isNotBlank() && newRestaurantRating.isNotBlank()) {
                            viewModel.addRestaurant(
                                newRestaurantName,
                                newRestaurantLocation,
                                newRestaurantRating,
                                newRestaurantNotes
                            )
                            onCancel()
                        } else {
                            dialogErrorText = true
                        }
                    }
                ) {
                    Text(text = "Confirm", style = MaterialTheme.typography.labelMedium)
                }
            }

        }
    }
}