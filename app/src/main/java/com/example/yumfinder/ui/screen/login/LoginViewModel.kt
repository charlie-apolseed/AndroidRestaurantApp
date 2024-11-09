package com.example.yumfinder.ui.screen.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth

class LoginViewModel : ViewModel() {
    var loginUiState: LoginUiState by mutableStateOf(LoginUiState.Init)


    private lateinit var auth : FirebaseAuth

    init {
        auth = Firebase.auth
    }

    fun registerUser(email: String, password: String) {
        loginUiState = LoginUiState.Loading
        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    loginUiState = LoginUiState.RegisterSuccess
                }
                .addOnFailureListener { exception ->
                    if (exception is FirebaseAuthException) { // Handle Firebase-specific exceptions
                        val errorCode = exception.errorCode
                        val errorMessage = exception.message

                        when (errorCode) {
                            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                                Log.d("LoginViewModel", "Registration failed: Email already in use")
                                loginUiState = LoginUiState.Error("This email is already in use. Please try a different one.")
                            }
                            "ERROR_INVALID_CREDENTIAL" -> {
                                Log.d("LoginViewModel", "Registration failed: Invalid credential")
                                loginUiState = LoginUiState.Error("Invalid credential. Please try again.")
                            }
                            else -> {
                                Log.d("LoginViewModel", "Registration failed: $errorMessage")
                                loginUiState = LoginUiState.Error("An error occurred: $errorMessage")
                            }
                        }
                    } else {
                        // Handle any other types of exceptions here
                        Log.d("LoginViewModel", "Registration failed: ${exception.message}")
                        loginUiState = LoginUiState.Error(exception.message ?: "Unknown error")
                    }
                }
        } catch (e: Exception) {
            Log.d("LoginViewModel", "Registration exception: ${e.message}")
            loginUiState = LoginUiState.Error(e.message ?: "An unknown error occurred.")
        }
    }


    fun loginUser(email: String, password: String, onSuccessfulLogin: () -> Unit) {
        loginUiState = LoginUiState.Loading
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    loginUiState = LoginUiState.LoginSuccess
                    onSuccessfulLogin()
                }
                .addOnFailureListener { exception ->
                    if (exception is FirebaseAuthException) { // Replace with the specific exception type if known
                        val errorCode = exception.errorCode
                        val errorMessage = exception.message

                        if (errorCode == "ERROR_INVALID_CREDENTIAL" ||
                            errorMessage?.contains("The supplied auth credential is incorrect, malformed or has expired") == true) {
                            loginUiState = LoginUiState.Error("Invalid credentials. Please try again.")
                        } else {
                            Log.d("LoginViewModel", "Login failed: $errorMessage")
                            loginUiState = LoginUiState.Error("An error occurred: $errorMessage")
                        }
                    } else {
                        // Handle any other types of exceptions here
                        Log.d("LoginViewModel", "Login failed: ${exception.message}")
                        loginUiState = LoginUiState.Error(exception.message)
                    }
                }
        } catch (e: Exception) {
            loginUiState = LoginUiState.Error(e.message)
            // Handle the exception
            }
        }
}

sealed interface LoginUiState {
    object Init : LoginUiState
    object Loading : LoginUiState
    object RegisterSuccess : LoginUiState
    object LoginSuccess : LoginUiState
    data class Error(val errorMessage: String?) : LoginUiState

}