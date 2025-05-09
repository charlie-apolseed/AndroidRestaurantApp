package com.example.yumfinder.ui.screen.recommendation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.yumfinder.R
import com.example.yumfinder.ui.theme.gray

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
                    modifier = Modifier.fillMaxWidth(0.9f).padding(top = 10.dp)
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
                                text = stringResource(R.string.apoleats),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                        Text(
                            modifier = Modifier.padding(bottom = 10.dp),
                            style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                            text = stringResource(R.string.ai_restaurant_recommendation)
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.73f)
                    .padding(top = 15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE1DAC5),
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
                    Text(
                        modifier = Modifier.padding(
                            top = 20.dp,
                            bottom = 10.dp,
                            start = 5.dp,
                            end = 5.dp
                        ),
                        fontSize = 22.sp,
                        fontWeight = FontWeight(700),
                        text = viewmodel.headerText,
                        color = gray
                    )
                    if (generatedText != null && generatedText != stringResource(R.string.generating)) {
                        Text(
                            text = generatedText,
                            modifier = Modifier
                                .padding(vertical = 16.dp, horizontal = 8.dp)
                                .fillMaxWidth(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight(600),
                            color = gray
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = stringResource(R.string.loading),
                            modifier = Modifier
                                .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                                .size(425.dp)
                            ,
                            alpha = .3f,
                        )
                        if (generatedText == stringResource(R.string.generating)) {
                            Text(
                                text = generatedText,
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .wrapContentWidth(),
                                fontSize = 25.sp,
                                fontWeight = FontWeight(600),
                                color = gray
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.how_can_i_help_you_today),
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .wrapContentWidth(),
                                fontSize = 25.sp,
                                fontWeight = FontWeight(600),
                                color = gray
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                if (viewmodel.headerText == "Restaurant recommendation") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Button(modifier = Modifier
                            .width(115.dp)
                            .fillMaxHeight(),
                            contentPadding = PaddingValues(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                            onClick = {
                                viewmodel.recLength--
                                viewmodel.getAIRecommendation("Recommendation")
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    fontSize = 20.sp,
                                    text = "Less"
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.minus),
                                    contentDescription = "Less",
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                        Button(modifier = Modifier
                            .width(115.dp)
                            .fillMaxHeight(),
                            contentPadding = PaddingValues(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                            onClick = {
                                viewmodel.getAIRecommendation("Recommendation")
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    fontSize = 20.sp,
                                    text = "Retry"
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.reload),
                                    contentDescription = "Retry",
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                        Button(modifier = Modifier
                            .width(115.dp)
                            .fillMaxHeight(),
                            contentPadding = PaddingValues(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                            onClick = {
                                viewmodel.recLength++
                                viewmodel.getAIRecommendation("Recommendation")
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    fontSize = 20.sp,
                                    text = "More"
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.add),
                                    contentDescription = "More",
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                    }
                } else {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                        onClick = {
                            viewmodel.recLength = 3
                            viewmodel.getAIRecommendation("Recommendation")
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                fontSize = 20.sp,
                                text = viewmodel.button1Text
                            )
                            Image(
                                painter = painterResource(id = R.drawable.dining),
                                contentDescription = "Recommendation",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                }

                if (viewmodel.headerText == "Review Summary") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .height(50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Button(modifier = Modifier
                            .width(115.dp)
                            .fillMaxHeight(),
                            contentPadding = PaddingValues(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                            onClick = {
                                viewmodel.recLength--
                                viewmodel.getAIRecommendation("Reviews")
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    fontSize = 20.sp,
                                    text = "Less"
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.minus),
                                    contentDescription = "Less",
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                        Button(modifier = Modifier
                            .width(115.dp)
                            .fillMaxHeight(),
                            contentPadding = PaddingValues(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                            onClick = {
                                viewmodel.getAIRecommendation("Reviews")
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    fontSize = 20.sp,
                                    text = "Retry"
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.reload),
                                    contentDescription = "Retry",
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                        Button(modifier = Modifier
                            .width(115.dp)
                            .fillMaxHeight(),
                            contentPadding = PaddingValues(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                            onClick = {
                                viewmodel.recLength++
                                viewmodel.getAIRecommendation("Reviews")
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    fontSize = 20.sp,
                                    text = "More"
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.add),
                                    contentDescription = "More",
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                    }
                } else {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                        onClick = {
                            viewmodel.recLength = 3
                            viewmodel.getAIRecommendation("Reviews")
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                fontSize = 20.sp,
                                text = viewmodel.button2Text
                            )
                            Image(
                                painter = painterResource(id = R.drawable.thumb),
                                contentDescription = "Reviews",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                }

                if (viewmodel.headerText == "Best Nearby") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Button(modifier = Modifier
                            .width(115.dp)
                            .fillMaxHeight(),
                            contentPadding = PaddingValues(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                            onClick = {
                                viewmodel.recLength--
                                viewmodel.getAIRecommendation("Best Nearby")
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    fontSize = 20.sp,
                                    text = "Less"
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.minus),
                                    contentDescription = "Less",
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                        Button(modifier = Modifier
                            .width(115.dp)
                            .fillMaxHeight(),
                            contentPadding = PaddingValues(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                            onClick = {
                                viewmodel.getAIRecommendation("Best Nearby")
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    fontSize = 20.sp,
                                    text = "Retry"
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.reload),
                                    contentDescription = "Retry",
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                        Button(modifier = Modifier
                            .width(115.dp)
                            .fillMaxHeight(),
                            contentPadding = PaddingValues(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                            onClick = {
                                viewmodel.recLength++
                                viewmodel.getAIRecommendation("Best Nearby")
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    fontSize = 20.sp,
                                    text = "More"
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.add),
                                    contentDescription = "More",
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                    }
                } else {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                        onClick = {
                            viewmodel.recLength = 3
                            viewmodel.getAIRecommendation("Best Nearby")
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                fontSize = 20.sp,
                                text = viewmodel.button3Text
                            )
                            Image(
                                painter = painterResource(id = R.drawable.nearby),
                                contentDescription = "Nearby",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                }


            }

        }
    }
}
