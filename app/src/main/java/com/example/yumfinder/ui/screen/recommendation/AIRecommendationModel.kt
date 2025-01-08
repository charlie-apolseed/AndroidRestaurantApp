package com.example.yumfinder.ui.screen.recommendation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yumfinder.data.RestaurantDAO
import com.example.yumfinder.data.RestaurantItem
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
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

    var recLength by mutableStateOf(3)
    var headerText by mutableStateOf("Good morning, User")
    var button1Text by mutableStateOf("AI recommendations")
    var button2Text by mutableStateOf("Summarize my reviews")
    var button3Text by mutableStateOf("Best places near me")

    init {
        generateHeader()
        fetchRestaurants()
    }

    private fun generateHeader() {
        val currentTime = System.currentTimeMillis()
        Log.d("AIRecommendationModel", "Current time: $currentTime")
        val hours = (currentTime / (1000 * 60 * 60)) % 24
        when (hours) {
            in 5..11 -> {
                headerText = "Good morning, "
            }

            in 12..16 -> {
                headerText = "Good afternoon, "
            }

            else -> {
                headerText = "Good evening, "
            }
        }
        headerText += Firebase.auth.currentUser?.email?.split(".")?.get(0) ?: "User" //TODO Switch this to username
    }

    private fun fetchRestaurants() {
        viewModelScope.launch {
            try {
                _visitedRestaurants.value = restaurantDAO.getAllUserRestaurants(Firebase.auth.currentUser?.email ?: "Unknown").first()
            } catch (e: Exception) {
                // Handle any potential errors
                _visitedRestaurants.value = emptyList()
                // Optionally log the error
                Log.e("AIRecommendationModel", "Error fetching restaurants", e)
            }
        }
    }

    private fun recommendationPromptHeader(): String {
        return "Give me $recLength new restaurant recommendations and provide a 3 sentence description for each. " +
                " Make sure the restaurants " +
                "you recommend are in the city that the most recent places I have been are in. Here is an example of the format: " +
                "\nArany Kaviár - District I, Budapest: This Michelin-starred restaurant is known for its contemporary hungarian " +
                "cuisine and innovative dishes. Luxurious but expensive, Arany Kaviár would be perfect for a special occasion." +
                "\n\n" +
                "Do not include any bold text! Here are some of the restaurants I have been to and my reviews of them:\n\n"
    }
    private fun reviewsPromptHeader(): String {
        return "Write a short $recLength paragraph summary of my the reviews of the restaurants I have been to. This " +
                "is an example of what I am looking for: Mazel Tov and David's Kitchen received exceptional " +
                "ratings, with high scores for food, vibes, and staff, earning an impressive 9.3 rating overall. " +
                "Reviewers praised the excellent cuisine and stunning interior of Mazel Tov, while David's Kitchen " +
                "was lauded for its delectable pastries and cozy atmosphere.\n" +
                "Hari Kebab, while still scoring highly for food, fell slightly short in the vibes and staff categories, " +
                "resulting in a lower overall rating of 7. Despite this, reviewers acknowledged the establishment as serving " +
                "the best kebabs in Budapest." +
                "\n\nDo not include any bold text! These are my reviews:\n\n"
    }
    private fun nearbyPromptHeader(): String {
        return "Give me $recLength restaurant recommendations for a nearby restaurants that I have not been to and provide a 3 sentence description for each. " +
                "Make sure the restaurants are nearby, and base it off the following format: \n\n" +
                "1. **Tranzit Etterem** - This cozy and relaxed restaurant offers a blend of Hungarian " +
                "and international cuisine, with a focus on fresh, seasonal ingredients. The menu changes " +
                "regularly, but you can expect to find dishes like roasted duck breast with plum sauce or " +
                "grilled salmon with saffron risotto.\n\n" +
                "2. **Bestia** - This trendy and vibrant spot serves up modern Italian dishes with a twist. " +
                "The open kitchen allows you to watch the chefs in action as they prepare mouthwatering " +
                "creations like homemade pasta with wild boar ragu or sea bass with roasted vegetables." +
                "\n\nDo not include any bold text! Make sure none of the following places are included: \n"

    }



    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = "AIzaSyCPa_DY0AqC1ilZJiL2mr845kleSSiZbLI"
    )

    fun getAIRecommendation(promptType: String) {
        val prompt: String
        if (promptType == "Recommendation") {
            headerText = "Restaurant recommendation"
            prompt = buildRecommendationPrompt()
        } else if (promptType == "Reviews") {
            headerText = "Review Summary"
            prompt = buildReviewsPrompt()
        } else {
            headerText = "Best Nearby"
            prompt = buildBestNearbyPrompt()
        }
        _textGenerationResult.value = "Generating..."
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                val generatedText = result.text
                _textGenerationResult.value = generatedText
            } catch (e: Exception) {
                _textGenerationResult.value = "Error:  ${e.message}"
            }
        }
    }

    private fun buildRecommendationPrompt(): String {
        return buildString {
            append(recommendationPromptHeader())
            _visitedRestaurants.value.forEach { review ->
                append(review)
            }
        }
    }

    private fun buildReviewsPrompt(): String {
        return buildString {
            append(reviewsPromptHeader())
            _visitedRestaurants.value.forEach { review ->
                append(review)
            }
        }
    }

    private fun buildBestNearbyPrompt(): String {
        return buildString {
            append(nearbyPromptHeader())
            _visitedRestaurants.value.forEach { review ->
                append(review)
            }
        }
    }

}