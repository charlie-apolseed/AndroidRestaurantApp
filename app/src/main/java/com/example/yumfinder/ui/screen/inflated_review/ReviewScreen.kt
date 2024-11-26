package com.example.yumfinder.ui.screen.inflated_review

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.example.yumfinder.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    modifier: Modifier = Modifier,
    viewModel: ReviewModel = hiltViewModel(),
    onBackAction: () -> Unit,
    onHomeAction: () -> Unit
) {

    LaunchedEffect(Unit) {
        viewModel.getRestaurantToEdit()
    }

    if (viewModel.restaurantToEdit == null) {
        Text(text = "Loading...", modifier = modifier.padding(16.dp))
    } else {
        val editedRestaurant = viewModel.restaurantToEdit!!.copy()


        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(.9f)
                        ) {
                            IconButton(
                                onClick = {
                                    onBackAction()
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    modifier = Modifier
                                        .size(32.dp)
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
                                    text = "Edit Review"
                                )
                            }

                            IconButton(
                                onClick = {
                                    onHomeAction()
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Home,
                                    contentDescription = "Home",
                                    modifier = Modifier
                                        .size(32.dp)
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
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .border(2.dp, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10.dp))
                        .fillMaxWidth(.9f)
                        .height(400.dp)
                        .background(color = Color(0xFFD9D9D9)), //TODO extract color resource
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp),
                            text = editedRestaurant.restaurantName,
                            color = Color.Black,
                            fontSize = 30.sp,
                            fontWeight = FontWeight(700)
                        )
                        IconButton(onClick = {}, modifier = Modifier.padding(end = 10.dp, top = 5.dp)) { //TODO Implement on edit feature
                            Icon(
                                Icons.Filled.Edit,
                                tint = Color.Black,
                                contentDescription = "Edit",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    Image(
                        painter = painterResource(R.drawable.logo), //TODO implement picture adding
                        contentDescription = "Restaurant Image",
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    }
}
