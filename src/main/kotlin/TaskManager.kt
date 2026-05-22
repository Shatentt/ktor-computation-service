package com.example

import com.example.dto.ComputeRequest
import com.example.dto.ResultResponse
import com.example.dto.TaskStatus
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class TaskManager(private val engine: ComputationEngine) {

    // Потокобезопасная коллекция для хранения задач в оперативной памяти
    private val tasks = ConcurrentHashMap<String, ResultResponse>()

    // Принимает запрос на вычисление, регистрирует задачу, асинхронно запускает расчет и возвращает ID задачи
    fun submitTask(request: ComputeRequest): String {
        val taskId = UUID.randomUUID().toString()

        tasks[taskId] = ResultResponse(
            id = taskId,
            status = TaskStatus.PENDING,
            result = null,
            durationMs = null
        )

        // Асинхронно отправляем задачу в вычислительный движок
        engine.startComputation(
            a = request.a,
            b = request.b,
            steps = request.steps,
            onComplete = { result, durationMs ->
                // Если корутина успешно завершит расчет, обновляем запись в мапе
                tasks[taskId] = ResultResponse(
                    id = taskId,
                    status = TaskStatus.COMPLETED,
                    result = result,
                    durationMs = durationMs
                )
            },
            onError = { throwable ->
                // Если расчет завершился ошибкой
                tasks[taskId] = ResultResponse(
                    id = taskId,
                    status = TaskStatus.FAILED,
                    result = null,
                    durationMs = null,
                    errorMessage = throwable.message ?: "Unknown error"
                )
            }
        )

        return taskId
    }

    // Получить текущее состояние задачи по её ID
    fun getTaskResult(id: String): ResultResponse? {
        return tasks[id]
    }
}