package com.example.dto // <-- ЗАМЕНИТЕ на ваше_имя_пакета.dto

import kotlinx.serialization.Serializable

// Статус выполнения вычислительной задачи
enum class TaskStatus {
    PENDING,   // Задача в очереди или выполняется
    COMPLETED, // Задача успешно завершена
    FAILED     // В процессе вычислений произошла ошибка
}

// Запрос на вычисление определенного интеграла f(x) = x^2 на [a, b] методом трапеций с заданным числом шагов (steps)
@Serializable
data class ComputeRequest(
    val a: Double,       // Начало интервала
    val b: Double,       // Конец интервала
    val steps: Long      // Количество интервалов разбиения (чем больше, тем дольше расчет)
)

// Быстрый ответ сервера на запрос вычисления
@Serializable
data class ComputeResponse(
    val id: String
)

// Ответ сервера на запрос статуса/результата задачи (GET /result/{id}).
@Serializable
data class ResultResponse(
    val id: String,
    val status: TaskStatus,
    val result: Double?,              // null, если статус PENDING или FAILED
    val durationMs: Long?,            // Сколько мс выполнялся расчет
    val errorMessage: String? = null  // Описание ошибки, если статус FAILED
)