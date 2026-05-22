PORT ?= 8082

.PHONY: compute result help

help:
	@echo "Доступные команды:"
	@echo "  make compute       - Отправить задачу на расчет (POST)"
	@echo "  make result id=ID  - Получить результат по ID (GET). Пример: make result id=123-abc"

# Отправка POST-запроса на вычисление
compute:
	@curl -s -X POST http://localhost:$(PORT)/compute \
		-H "Content-Type: application/json" \
		-d '{"a": 0.0, "b": 10.0, "steps": 100000000}' | jq

# Получение GET-запроса по ID задачи
result:
	@if [ -z "$(id)" ]; then \
		echo "Ошибка: Необходимо указать id задачи. Пример использования:"; \
		echo "  make result id=<UUID>"; \
		exit 1; \
	fi
	@curl -s -X GET http://localhost:$(PORT)/result/$(id) | jq