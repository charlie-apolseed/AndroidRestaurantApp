package com.example.yumfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yumfinder.ui.screen.your_visits.ListScreen
import com.example.yumfinder.ui.screen.home.HomeScreen
import com.example.yumfinder.ui.screen.login.LoginScreen
import com.example.yumfinder.ui.theme.YumFinderTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            YumFinderTheme {
                MainNavigation(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "LoginScreen"
){
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("LoginScreen") {
            LoginScreen(
                modifier = modifier,
                onSuccessfulLogin = {
                    navController.navigate("HomeScreen")
                }
            )
        }
        composable("HomeScreen") {
            HomeScreen(
                onListAction = {
                    navController.navigate("ListScreen?addDialog=false") },
                onAddAction = {
                    navController.navigate("ListScreen?addDialog=true") }
            )
        }
        composable("ListScreen?addDialog={addDialog}") {
            ListScreen(onHomeAction = {
                navController.navigate("HomeScreen")
            })
        }


    }
}

