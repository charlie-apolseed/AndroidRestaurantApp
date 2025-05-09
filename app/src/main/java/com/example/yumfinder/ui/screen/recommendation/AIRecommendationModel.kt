package com.example.yumfinder.ui.screen.recommendation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yumfinder.data.RestaurantDAO
import com.example.yumfinder.data.RestaurantItem
import com.example.yumfinder.data.location.LocationRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class AIRecommendation(
    val name: String = "",
    val location: String = "",
    val summary: String = ""
)

@HiltViewModel
class AIRecommendationModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val restaurantDAO: RestaurantDAO
) : ViewModel() {

    private val _visitedRestaurants = MutableStateFlow<List<RestaurantItem>>(emptyList())

    private val _textGenerationResult = MutableStateFlow<String?>(null)
    val textGenerationResult: StateFlow<String?> = _textGenerationResult.asStateFlow()

    private val _parsedRecommendations = MutableStateFlow<List<AIRecommendation>>(emptyList())
    val parsedRecommendations: StateFlow<List<AIRecommendation>> = _parsedRecommendations.asStateFlow()

    var recLength by mutableStateOf(3)
    var headerText by mutableStateOf("Good morning, User")
    var button1Text by mutableStateOf("AI recommendations")
    var button2Text by mutableStateOf("Summarize my reviews")
    var button3Text by mutableStateOf("Best places near me")

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    init {
        generateHeader()
        fetchRestaurants()
        viewModelScope.launch {
            locationRepository.getCurrentLocation().collect { location ->
                _currentLocation.value = location
            }
        }
    }

    private fun generateHeader() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        headerText = when (hour) {
            in 5..11 -> "Good morning, "
            in 12..16 -> "Good afternoon, "
            else -> "Good evening, "
        }
        headerText += Firebase.auth.currentUser?.email?.split("@")?.get(0) ?: "User"
    }

    private fun fetchRestaurants() {
        viewModelScope.launch {
            try {
                _visitedRestaurants.value =
                    restaurantDAO.getAllUserRestaurants(Firebase.auth.currentUser?.email ?: "Unknown").first()
            } catch (e: Exception) {
                _visitedRestaurants.value = emptyList()
                Log.e("AIRecommendationModel", "Error fetching restaurants", e)
            }
        }
    }

    private fun recommendationPromptHeader(): String {
        val location = _currentLocation.value ?: return "Please wait for location data..."

        return """
            I need exactly $recLength restaurant recommendations in JSON array format.
            Here's the format I need for each item:
            
            [
              {
                "name": "Restaurant Name",
                "location": "City, State/Country",
                "summary": "A brief 2-3 sentence summary about the restaurant."
              }
            ]
            
            Please give me $recLength restaurant recommendations for places near my current location 
            (${location.latitude}, ${location.longitude}). 
            
            Here are some of the restaurants I have been to and my reviews of them:
            
        """.trimIndent()
    }

    private fun reviewsPromptHeader(): String {
        return """
            Write a short $recLength paragraph summary of the reviews of the restaurants I have been to. 
            Format your response as a JSON array like this:
            
            [
              {
                "name": "Summary",
                "location": "",
                "summary": "Your summary paragraph here"
              }
            ]
            
            Here are my reviews:
            
        """.trimIndent()
    }

    private fun nearbyPromptHeader(): String {
        val location = _currentLocation.value ?: return "Please wait for location data..."

        return """
            Give me $recLength restaurant recommendations for places near my current location 
            (${location.latitude}, ${location.longitude}) that I have not been to.
            
            Format your response as a JSON array like this:
            
            [
              {
                "name": "Restaurant Name", 
                "location": "City, State/Country", 
                "summary": "A brief description of what makes this place special."
              }
            ]
            
            Do not include any of the following places:
            
        """.trimIndent()
    }

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-pro-exp-02-05",
        apiKey = "AIzaSyCPa_DY0AqC1ilZJiL2mr845kleSSiZbLI"
    )

    fun getAIRecommendation(promptType: String) {
        val promptBuilder = when (promptType) {
            "Recommendation" -> {
                headerText = "Restaurant Recommendation"
                ::buildRecommendationPrompt
            }
            "Reviews" -> {
                headerText = "Review Summary"
                ::buildReviewsPrompt
            }
            else -> {
                headerText = "Best Nearby"
                ::buildBestNearbyPrompt
            }
        }

        _textGenerationResult.value = "Generating..."
        _parsedRecommendations.value = emptyList()

        val prompt = promptBuilder()
        if (prompt.startsWith("Please wait")) {
            _textGenerationResult.value = prompt
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                val rawResponse = result.text ?: ""

                _textGenerationResult.value = rawResponse
                Log.d("AIRecommendationModel", "Raw response: $rawResponse")
                // Try to parse the JSON array directly
                parseRecommendationsFromText(rawResponse)

            } catch (e: Exception) {
                Log.e("AIRecommendationModel", "AI generation failed", e)
                _textGenerationResult.value = "Error: ${e.message}"
                _parsedRecommendations.value = emptyList()
            }
        }
    }

    private fun parseRecommendationsFromText(text: String) {
        try {
            // Extract JSON from the text, handling various markdown or text formats
            val jsonPattern = """\[[\s\S]*?\]""".toRegex()
            val jsonMatch = jsonPattern.find(text)

            if (jsonMatch != null) {
                val jsonString = jsonMatch.value

                val gson = Gson()
                val type = object : TypeToken<List<AIRecommendation>>() {}.type

                try {
                    val recommendations: List<AIRecommendation> = gson.fromJson(jsonString, type)
                    _parsedRecommendations.value = recommendations
                    Log.d("AIRecommendationModel", "Parsed recommendations: $recommendations")
                } catch (e: JsonSyntaxException) {
                    Log.e("AIRecommendationModel", "JSON parsing failed: ${e.message}", e)
                    // Try handling recommendations as a simple text response
                    _parsedRecommendations.value = listOf(AIRecommendation("AI Response", "", text))
                }
            } else {
                // Fallback if no JSON found
                _parsedRecommendations.value = listOf(AIRecommendation("AI Response", "", text))
            }
        } catch (e: Exception) {
            Log.e("AIRecommendationModel", "Parsing failed", e)
            _parsedRecommendations.value = listOf(AIRecommendation("Error", "", "Could not parse response"))
        }
    }

    private fun buildRecommendationPrompt(): String {
        return buildString {
            append(recommendationPromptHeader())
            _visitedRestaurants.value.forEach { append(it.toString()) }
        }
    }

    private fun buildReviewsPrompt(): String {
        return buildString {
            append(reviewsPromptHeader())
            _visitedRestaurants.value.forEach { append(it.toString()) }
        }
    }

    private fun buildBestNearbyPrompt(): String {
        return buildString {
            append(nearbyPromptHeader())
            _visitedRestaurants.value.forEach { append(it.toString()) }
        }
    }
}

