package com.example.yumfinder.ui.screen.login


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Button
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yumfinder.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier,
    viewModel: LoginViewModel = viewModel(),
    onSuccessfulLogin: () -> Unit
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
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
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Background Images
            Column(modifier = Modifier.fillMaxSize()) {
                repeat(4) {
                    Image(
                        painter = painterResource(id = R.drawable.food_background),
                        contentDescription = "Background Image",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Overlay Login Form
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.Center)
                    .background(
                        color = Color.White.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to ApolEats",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "E-mail") },
                    value = email,
                    isError = viewModel.loginUiState is LoginUiState.Error,
                    onValueChange = {
                        email = it
                        viewModel.loginUiState = LoginUiState.Init
                    },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Email, null) }
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Password") },
                    value = password,
                    isError = viewModel.loginUiState is LoginUiState.Error,
                    onValueChange = {
                        password = it
                        viewModel.loginUiState = LoginUiState.Init
                    },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                painter = painterResource(
                                    id = if (showPassword) R.drawable.visible else R.drawable.invisible
                                ),
                                contentDescription = if (showPassword) "Hide Password " else "Show Password",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                )

                if (viewModel.loginUiState is LoginUiState.Error) {
                    (viewModel.loginUiState as LoginUiState.Error).errorMessage?.let {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = it,
                            color = Color.Red
                        )
                    }
                } else {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "",
                        color = Color.Red
                    )
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    onClick = { viewModel.loginUser(email, password, onSuccessfulLogin) },
                    shape = RoundedCornerShape(8.dp)
                )
                {
                    Text("Login", style = MaterialTheme.typography.labelMedium)
                }
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(.6f),
                    onClick = {
                        viewModel.registerUser(email, password)
                    }
                ) {
                    Text(
                        fontSize = 20.sp,
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline,
                        text = "Register"
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (val uiState = viewModel.loginUiState) {
                    is LoginUiState.Init -> {}
                    is LoginUiState.Loading -> CircularProgressIndicator()
                    is LoginUiState.RegisterSuccess -> Text("Register OK", color = Color.Green)
                    is LoginUiState.LoginSuccess -> {}
                    is LoginUiState.Error -> { }
                }
            }
        }
    }
}

