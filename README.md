# Telegram WebApp Auth

## 📋 Описание

Spring Boot-приложение с авторизацией через Telegram WebApp и валидацией `initData`.  
Сохраняет данные пользователя в базу и проверяет их подлинность по HMAC-алгоритму.

---

## 🚀 Запуск проекта

### 1. Получите токен бота
Создайте Telegram-бота с помощью [@BotFather](https://t.me/BotFather) и получите токен.
Добавьте его в переменные окружения: 
`TELEGRAM_WEBAPP_BOT_TOKEN=<ваш_токен_бота>`

### 2. Подготовьте базу данных
Создайте PostgreSQL-базу данных и добавьте параметры подключения:
  - `DATASOURCE_URL=jdbc:postgresql://<host>:<port>/<dbname>`
  - `DATASOURCE_USERNAME=<имя_пользователя>`
  - `DATASOURCE_PASSWORD=<пароль>`

### 3. Запустите приложение
  `./gradlew bootRun`
