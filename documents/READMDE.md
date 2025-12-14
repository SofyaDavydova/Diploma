# Процедура запуска автотестов.

## 1. Подготовительный этап
1. Установить IntelliJ IDEA.
2. Клонировать репозиторий с Github [по ссылке](https://github.com/SofyaDavydova/Diploma).
3. Запустить IntelliJ IDEA и открыть клонированный проект.
4. Установить и запустить Docker Desktop;

## 2. Запуск приложения
1. Запустить контейнеры в терминале IntelliJ Idea с помощью команды:
```
docker compose up 
```
2. Запустить приложение с помощью команды в терминале:

 ```
   - для mySQL:
        java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar 

   - для postgresgl:
        java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar
 ```
3. Убедиться в том, что сервис доступен по адресу:
```
        http://localhost:8080/
```
## 3. Запуск автотестов
1. В новой вкладке терминала запустить тесты командой
```
    - для mySQL:
        ./gradlew clean test -DurlDB="jdbc:mysql://localhost:3306/app"

    - для postgresgl: 
        ./gradlew clean test -DurlDB="jdbc:postgresql://localhost:5432/app"
```
## 4. Формирование отчета о прогоне автотестов
1. Для формирования отчётности с помощью Allure в новой вкладке терминала ввести команду
```
./gradlew allureServe
```
2. Для завершения работы allureServe выполнить команду:

```
    Ctrl+C
```


