package com.example.data.response

data class PredictionRequest(
    val seed_text: String
)

data class PredictionResponse(
    val predicted_text: String
)