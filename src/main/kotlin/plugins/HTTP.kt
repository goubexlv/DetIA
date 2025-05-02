package com.daccvo.plugins

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cachingheaders.*

fun Application.configureHTTP() {
    install(CachingHeaders) {

    }
}
