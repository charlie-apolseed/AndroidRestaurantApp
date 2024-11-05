package com.example.yumfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yumfinder.ui.screen.ListScreen
import com.example.yumfinder.ui.screen.home.HomeScreen
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
    startDestination: String = "HomeScreen"
){
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
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
