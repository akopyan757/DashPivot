# Используем slim-образ для Java 18
FROM openjdk:18-slim

# Создаем директорию для приложения
WORKDIR /app

# Копируем jar-файл сервера в контейнер
COPY server/build/libs/server-all.jar /app/server.jar

# Запуск сервера
ENTRYPOINT ["java", "-jar", "/app/server.jar"]