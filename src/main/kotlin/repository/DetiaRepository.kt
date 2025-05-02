package com.daccvo.repository

import com.daccvo.domain.response.Result

interface DetiaRepository {
    suspend fun extractTextFromPDF(bytes: ByteArray) : String
    suspend fun analyseImage(imageBytes: ByteArray) : Result
    suspend fun analyseText(text: String) : Result

    suspend fun telechargerModel()
}