# Проект по тестированию API на Java

Этот проект содержит автоматизированные тесты для REST API сервиса [Яндекс.Самокат](https://qa-scooter.praktikum-services.ru/). Тесты покрывают основные сценарии создания, удаления, авторизации курьеров, а также создание и получение списка заказов. Для удобного анализа результатов используется Allure-отчет.

## 🛠 Стек технологий

* Java 11 (Corretto)

* Maven — управление зависимостями и сборка

* JUnit 4 — фреймворк для тестирования

* REST Assured — библиотека для выполнения HTTP-запросов

* Allure — фреймворк для генерации наглядных отчётов о выполнении тестов

* AspectJ — для работы Allure с аннотациями и перехватом вызовов

## 📁 Структура проекта

```text
src/
├── main/
│   └── java/
│       └── models/           # POJO-классы (Courier, Order, LoginCourier)
└── test/
    └── java/
        ├── clients/          # Клиенты для отправки запросов к API
        │   ├── CourierClient.java
        │   └── OrderClient.java
        ├── helpers/          # Утилиты для создания и очистки тестовых данных
        │   ├── CourierTestHelper.java
        │   └── CourierCreationResult.java
        └── tests/            # Тестовые классы
           ├── NewCourierTest.java
           ├── DeleteCourierTest.java
           ├── LoginCourierTest.java
           ├── OrderTest.java
           └── OrdersListTest.java
pom.xml
README.md
.gitignore
```

## ⚙️ Установка и запуск тестов

1. Клонируйте репозиторий: 

```bash
git clone https://github.com/SENPAILOVEGOD/Sprint_7.git
cd Sprint_7
```

2. Запустите тесты:

```bash
mvn clean test
```
3. Сгенерируйте Allure-отчет:

```bash
mvn allure:serve
```

> Примечание: для генерации отчета в Java 11 используется Allure Commandline. Нужную версию необходимо предварительно скачать [здесь](https://repo.maven.apache.org/maven2/io/qameta/allure/allure-commandline/)

После выполнения `mvn allure:serve` в браузере откроется файл с детальным отчетом по тестам.

## ✍️ Авторы

* Ева Коновалова – студент курса по автоматизации тестирования на Java.

## 📄 Лицензия
Проект создан в учебных целях