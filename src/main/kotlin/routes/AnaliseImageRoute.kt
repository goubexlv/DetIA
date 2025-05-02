package com.daccvo.routes

import com.daccvo.domain.Endpoint
import com.daccvo.domain.response.Result
import com.daccvo.repository.DetiaRepository
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Route.analiseImageRoute(detiaRepository : DetiaRepository){

    post(Endpoint.AnalyzeImage.path) {
        val multipart = call.receiveMultipart()
        var imageBytes: ByteArray? = null

        multipart.forEachPart { part ->
            if (part is PartData.FileItem) {
                imageBytes = part.streamProvider().readBytes()
            }
            part.dispose()
        }

        if (imageBytes == null) {
            call.respond(
                status = HttpStatusCode.BadRequest ,
                message = "fichier non trouver"
            )
            return@post
        }

        val result = detiaRepository.analyseImage(imageBytes!!)

        when (result){
            is Result.Success -> call.respond(status = HttpStatusCode.OK , message = result)
            is Result.Error -> call.respond(status = HttpStatusCode.BadRequest , message = result.message)
        }

    }

    post(Endpoint.AnalyzeText.path) {
        val multipart = call.receiveMultipart()
        var textContent = ""

        multipart.forEachPart { part ->
            if (part is PartData.FileItem) {
                val name = part.originalFileName ?: "file"
                val ext = File(name).extension.lowercase()

                val bytes = part.streamProvider().readBytes()

                textContent = when (ext) {
                    "txt" -> bytes.toString(Charsets.UTF_8)
                    "pdf" -> detiaRepository.extractTextFromPDF(bytes)
                    else -> return@forEachPart call.respond(HttpStatusCode.UnsupportedMediaType, "Only PDF or TXT files supported")
                }
            }
            part.dispose()
        }
        print(textContent)
        if (textContent.isBlank()) return@post call.respond(HttpStatusCode.BadRequest, "No valid text found")

        val result = detiaRepository.analyseText(textContent)
        when (result){
            is Result.Success -> call.respond(status = HttpStatusCode.OK , message = result)
            is Result.Error -> call.respond(status = HttpStatusCode.BadRequest , message = result.message)
        }
    }

    get(Endpoint.telechargerModel.path) {
        detiaRepository.telechargerModel()
    }

    get(Endpoint.Root.path){
        call.respondText("Bienvenu sur API")
    }


}