package com.daccvo.domain

sealed class Endpoint (val path : String) {
    object Root: Endpoint(path = "/")
    object AnalyzeImage : Endpoint(path = "/analyzeimage")
    object AnalyzeText : Endpoint(path = "/analyzetext")
}