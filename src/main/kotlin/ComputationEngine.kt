package com.example

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class ComputationEngine {
    // CoroutineScope для фоновых задач движка
    private val engineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // Запускает асинхронное вычисление интеграла в фоне
    fun startComputation(
        a: Double,
        b: Double,
        steps: Long,
        onComplete: (result: Double, durationMs: Long) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        // Запускаем корутину в собственном Scope движка
        engineScope.launch {
            try {
                var result = 0.0
                // Замеряем время работы тяжелой функции
                val duration = measureTimeMillis {
                    // Переключаем контекст на Dispatchers.Default, т.к. это чисто процессорная (CPU-bound) задача
                    result = withContext(Dispatchers.Default) {
                        calculateIntegral(a, b, steps)
                    }
                }
                // Возвращаем результат через колбэк
                onComplete(result, duration)
            } catch (e: Exception) {
                // Если что-то пошло не так, возвращаем ошибку
                onError(e)
            }
        }
    }

    // Тяжелое вычисление: Численное интегрирование функции f(x) = x^2 методом трапеций
    private fun calculateIntegral(a: Double, b: Double, steps: Long): Double {
        if (steps <= 0) throw IllegalArgumentException("Количество шагов должно быть больше нуля")

        val h = (b - a) / steps
        var sum = 0.5 * (f(a) + f(b))

        for (i in 1 until steps) {
            val x = a + i * h
            sum += f(x)
        }

        return sum * h
    }

    // Интегрируемая функция
    private fun f(x: Double): Double = x * x
}