package com.daccvo.repository

import com.daccvo.domain.response.Result
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.lang.ProcessBuilder


class DetiaRepositoryImpl : DetiaRepository {
    override suspend fun extractTextFromPDF(bytes: ByteArray): String {
        PDDocument.load(bytes.inputStream()).use { document ->
            return PDFTextStripper().getText(document)
        }
    }

    override suspend fun analyseImage(imageBytes: ByteArray) : Result  {
        // 1. Sauvegarder l'image dans un fichier temporaire
        val tempFile = File.createTempFile("upload_", ".jpg")
        tempFile.writeBytes(imageBytes)

        try {
            // 2. Lancer le script Python
            val process = ProcessBuilder(
                "./venv/bin/python3", "/app/analyze.py", tempFile.absolutePath
            )
                .redirectErrorStream(true) // merge stderr dans stdout pour tout lire
                .start()

            // 3. Lire la sortie (stdout)
            val output = process.inputStream.bufferedReader().readText().trim()
            val exitCode = process.waitFor()

            if (exitCode != 0) {
                Result.Error("Erreur script Python: $output")
            }

            // 4. Interpréter la sortie
            val confidence = output.split("\n")
                .lastOrNull() // Prend la dernière ligne de sortie (qui devrait être le score)
                ?.trim()
                ?.toDoubleOrNull()

            // Si la conversion échoue, lance une exception
            if (confidence == null) {
                throw RuntimeException("Sortie invalide: $output")
            }
            // 5. Retourner les résultats
            val result = if (confidence > 0.5) "IA_GENERER" else "REEL"


            return Result.Success(result = result, pourcentage = (confidence * 100))

        } finally {
            // 6. Nettoyer le fichier temporaire
            tempFile.delete()
        }
    }

    override suspend fun analyseText(text: String): Result {
        try {
            val process = ProcessBuilder("./venv/bin/python3", "/app/analyzetext.py")
                .redirectErrorStream(true)
                .start()

            process.outputStream.bufferedWriter().use { writer ->
                writer.write(text)
                writer.flush()
            }

            val output = process.inputStream.bufferedReader().readText().trim()
            val exitCode = process.waitFor()

            if (exitCode != 0) {
                Result.Error("Erreur script Python: $output")
            }

            val confidence = output.split("\n")
                .lastOrNull() // Prend la dernière ligne de sortie (qui devrait être le score)
                ?.trim()
                ?.toDoubleOrNull()

            if (confidence == null) {
                throw RuntimeException("Sortie invalide: $output")
            }
            val result = if (confidence > 0.5) "IA_GENERER" else "REEL"


            return Result.Success(result = result, pourcentage = (confidence * 100))

        } catch (e : Exception){
            return Result.Error(e.message.toString())
        }

    }

    override suspend fun telechargerModel() {
        try {
            val process = ProcessBuilder("./venv/bin/python3", "/app/telemodel.py")
                .redirectErrorStream(true)
                .start()
        } catch (e : Exception){

        }

    }
}