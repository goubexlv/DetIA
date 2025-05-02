package com.daccvo.plugins

import com.daccvo.repository.DetiaRepository
import com.daccvo.routes.analiseImageRoute
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    routing {

        val detiaRepository : DetiaRepository by application.inject()

        analiseImageRoute(detiaRepository)
        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")
    }
}
