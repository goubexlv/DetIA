package com.daccvo.domain.response

import kotlinx.serialization.Serializable

@Serializable
sealed class Result {
    @Serializable
    data class Success(
        val result: String,
        val pourcentage: Double
    ) : Result()

    @Serializable
    data class Error(
        val message : String
    ): Result()
}