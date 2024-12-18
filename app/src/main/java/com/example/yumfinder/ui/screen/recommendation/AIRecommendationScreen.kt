package com.example.yumfinder.ui.screen.recommendation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.yumfinder.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIRecommendationScreen(
    modifier: Modifier = Modifier,
    onHomeAction: () -> Unit,
    onBackAction: () -> Unit,
    viewmodel: AIRecommendationModel = hiltViewModel()
) {
    val generatedText = viewmodel.textGenerationResult.collectAsState().value


    Scaffold(topBar = {
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
                            text = "AI Restaurant Recommendation"
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
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        )
    }) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                modifier = Modifier.padding(vertical = 15.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight(700),
                text = viewmodel.headerText,
                color = Color.Black
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.7f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    if (generatedText != null && generatedText != "Generating...") {
                        Text(
                            text = generatedText,
                            modifier = Modifier
                                .padding(top = 16.dp, start = 8.dp, end = 8.dp)
                                .fillMaxWidth(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight(600)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Loading",
                            modifier = Modifier
                                .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                                .size(425.dp),
                            alpha = .3f
                        )
                        if (generatedText == "Generating...") {
                            Text(
                                text = generatedText,
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .wrapContentWidth(),
                                fontSize = 25.sp,
                                fontWeight = FontWeight(600),
                                color = Color.Black
                            )
                        } else {
                            Text(
                                text = "How can I help you today?",
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .wrapContentWidth(),
                                fontSize = 25.sp,
                                fontWeight = FontWeight(600),
                                color = Color.Black
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                    onClick = { viewmodel.getAIRecommendation("Recommendation") }
                ) {
                    Text(text = viewmodel.button1Text, fontSize = 20.sp)
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                    onClick = { viewmodel.getAIRecommendation("Reviews") }
                ) {
                    Text(text = viewmodel.button2Text, fontSize = 20.sp)
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                    onClick = { viewmodel.getAIRecommendation("Best Nearby") }
                ) {
                    Text(text = viewmodel.button3Text, fontSize = 20.sp)
                }
            }

        }
    }
}
