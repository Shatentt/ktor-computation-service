package com.example

import com.example.dto.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(taskManager: TaskManager) {
    routing {

        post("/compute") {
            try {
                val request = call.receive<ComputeRequest>()
                val taskId = taskManager.submitTask(request)
                call.respond(HttpStatusCode.Accepted, ComputeResponse(taskId))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to (e.message ?: "Invalid request data"))
                )
            }
        }

        get("/result/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing task ID"))
                return@get
            }

            val taskResult = taskManager.getTaskResult(id)
            if (taskResult == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    mapOf("error" to "Task with ID $id not found")
                )
            } else {
                call.respond(HttpStatusCode.OK, taskResult)
            }
        }
    }
}