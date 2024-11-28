package com.example.yumfinder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF3F1F3F),
    onPrimary = Color.White,
    secondary = Color(0xFFAF99AF),
    tertiary = Color(0xFFD7D7D7),
    onTertiary = Color(0xFFA59EA6),

    surface = Color(0xFFFFFBFE),
    onSurface = Color.Black,
    secondaryContainer = Color(0xFFD9BCD9),
    onSecondaryContainer = Color.Black,

    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFFCBC0C5),
    surfaceVariant = Color(0xFFEFE3E6),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF3F1F3F),
    onPrimary = Color.White,
    secondary = Color(0xFFAF99AF),

    tertiary = Color(0xFFD7D7D7),
    onTertiary = Color(0xffc0c0c0),

    surface = Color(0xFFFFFBFE),
    onSurface = Color.Black,
    secondaryContainer = Color(0xFFD9BCD9),
    onSecondaryContainer = Color.Black,
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFFE7DDE2),
    surfaceVariant = Color(0xFFF1EAEB),


    )


@Composable
fun YumFinderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}