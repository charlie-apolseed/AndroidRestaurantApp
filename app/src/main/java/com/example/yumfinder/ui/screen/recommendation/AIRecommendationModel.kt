package com.example.yumfinder.ui.screen.recommendation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yumfinder.data.RestaurantDAO
import com.example.yumfinder.data.RestaurantItem
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AIRecommendationModel @Inject constructor(
    private val restaurantDAO: RestaurantDAO
) : ViewModel() {
    private val _visitedRestaurants = MutableStateFlow<List<RestaurantItem>>(emptyList())


    private val _textGenerationResult = MutableStateFlow<String?>(null)
    val textGenerationResult = _textGenerationResult.asStateFlow()

    var headerText by mutableStateOf("Good morning, User") //TODO get the accurate time of day and add user name
    var button1Text by mutableStateOf("Recommendations")
    var button2Text by mutableStateOf("Reviews Summary")
    var button3Text by mutableStateOf("Best Nearby")

    init {
        fetchRestaurants()
    }

    private fun fetchRestaurants() {
        viewModelScope.launch {
            try {
                _visitedRestaurants.value = restaurantDAO.getAllRestaurants().first()
            } catch (e: Exception) {
                // Handle any potential errors
                _visitedRestaurants.value = emptyList()
                // Optionally log the error
                Log.e("AIRecommendationModel", "Error fetching restaurants", e)
            }
        }
    }

    private val promptHeader =
        "Give me 2-3 new restaurant recommendations and provide a short description for each. " +
                "These are some of the restaurants I've been to. Make sure the restaurants " +
                "you recommend are in the city that the most recent places I have been are in"

    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = "AIzaSyCPa_DY0AqC1ilZJiL2mr845kleSSiZbLI"
    )

    fun getAIRecommendation() {
        val prompt = buildPrompt()
        _textGenerationResult.value = "Generating..."
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                val generatedText = result.text
                _textGenerationResult.value = generatedText
            } catch (e: Exception) {
                _textGenerationResult.value = "Error: ${e.message}"
            }
        }
    }

    private fun buildPrompt(): String {
        return buildString {
            append(promptHeader)
            _visitedRestaurants.value.forEach { review ->
                append(review)
            }
        }
    }
}