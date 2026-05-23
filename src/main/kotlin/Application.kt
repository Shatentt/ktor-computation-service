package com.example

import io.ktor.server.application.*

fun Application.module() {
    val engine = ComputationEngine()

    val taskManager = TaskManager(engine)

    configureSerialization()

    configureRouting(taskManager)
}

fun Application.rootModule() {
    module()
}