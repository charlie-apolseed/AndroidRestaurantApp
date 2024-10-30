package com.example.yumfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.yumfinder.ui.screen.ListScreen
import com.example.yumfinder.ui.theme.YumFinderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            YumFinderTheme {
                ListScreen(Modifier.fillMaxSize())
            }
        }
    }
}

