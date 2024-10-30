package com.example.yumfinder.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen (modifier: Modifier = Modifier, viewModel: ListModel = ListModel()) {
    var visitedRestaurants = viewModel.visitedRestaurants
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = "Yum Finder") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            actions = {
                IconButton(
                    onClick = {
                        showAddDialog = true
                    }
                ) {
                    Icon(
                        Icons.Filled.AddCircle, contentDescription = "Add"
                    )
                }
            }
        )
    }) {
            innerPadding ->
        Text(text = "Yumber One", modifier = Modifier.padding(innerPadding))

        if (showAddDialog) {
            AddRestaurantDialog(viewModel,
                onCancel = { showAddDialog = false }
            )
        }
    }

}

@Composable
fun RestaurantCard (restaurant: Restaurant) {
    Card (
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ), shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ), modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        var expanded by remember { mutableStateOf(false) }

        Column (modifier = Modifier
            .padding(20.dp)
            .animateContentSize()) {
            Row (verticalAlignment = Alignment.CenterVertically ) {
                Column {
                    Text(text = restaurant.name, style = MaterialTheme.typography.headlineMedium)
                    Text(text = restaurant.location, style = MaterialTheme.typography.bodyMedium)
                }
                Text(text = restaurant.rating, style = MaterialTheme.typography.headlineMedium)

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp
                        else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) {
                            "Less"
                        } else {
                            "More"
                        }
                    )
                }
            }
            if (expanded) {
                Text(text = restaurant.notes, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}


@Composable
fun AddRestaurantDialog (
    viewModel: ListModel,
    onCancel: () -> Unit
) {
    var newRestaurantName by remember { mutableStateOf("") }
    var newRestaurantLocation by remember { mutableStateOf("") }
    var newRestaurantRating by remember { mutableStateOf("") }
    var newRestaurantNotes by remember { mutableStateOf("") }
    var dialogErrorText by remember { mutableStateOf(false) }

    Dialog (onDismissRequest = onCancel) {
        Surface(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            shape = RoundedCornerShape(size = 6.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text( text = "Add Restaurant Review", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.headlineMedium)
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
                Button( modifier = Modifier.fillMaxWidth().padding(16.dp),
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
                    Text(text = "Add Restaurants")
                }
            }

        }
    }
}