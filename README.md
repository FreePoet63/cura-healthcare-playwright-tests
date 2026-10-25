# CURA Healthcare: Playwright Java Tests

UI test automation framework for the [CURA Healthcare Service](https://katalon-demo-cura.herokuapp.com) demo web app.

**Stack:** Java 17, Playwright, JUnit 5, AssertJ, Allure, Maven.

## Project structure

```
src/
├─ main/
│  ├─ java/com/cura/
│  │  ├─ components/   # Reusable UI components (e.g. SideMenu)
│  │  ├─ config/       # TestConfig: reads config.properties, supports -D overrides
│  │  ├─ core/         # PlaywrightManager: per-thread browser lifecycle, tracing
│  │  ├─ models/       # Test data models (User)
│  │  └─ pages/        # Page Objects (Home, Login, Appointment)
│  └─ resources/config.properties
└─ test/
   └─ java/com/cura/
      ├─ base/         # BaseTest: setup / teardown
      ├─ extensions/   # JUnit extensions (screenshot on failure)
      └─ tests/        # Test classes grouped by feature (login, ...)
```

## Prerequisites

- JDK 17+
- Maven 3.9+

Playwright downloads the required browsers automatically on the first run.

## Running tests

```bash
# Run all tests (headless Chromium)
mvn clean test

# Run a single test class
mvn test -Dtest=LoginTest

# Firefox, headed mode, slowed down by 300 ms
mvn test -Dbrowser=firefox -Dheadless=false -DslowMo=300
```

## Configuration

Defaults live in `src/main/resources/config.properties`. Any value can be overridden from the command line with `-Dkey=value`.

| Property    | Default                                     | Description                              |
|-------------|---------------------------------------------|------------------------------------------|
| `baseUrl`   | `https://katalon-demo-cura.herokuapp.com`   | Application under test                   |
| `browser`   | `chromium`                                  | `chromium`, `firefox` or `webkit`        |
| `headless`  | `true`                                      | Run without a visible browser window     |
| `slowMo`    | `0`                                         | Delay between actions, ms                |
| `timeoutMs` | `10000`                                     | Default Playwright timeout, ms           |
| `username`  | `John Doe`                                  | Public demo account                      |
| `password`  | `ThisIsNotAPassword`                        | Public demo account                      |

## Reports and debugging

- **Allure report:** `mvn allure:serve`
- **Screenshot on failure:** attached to the Allure report automatically.
- **Playwright trace:** saved for failed tests to `target/traces/*.zip`. Open it with:

```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI \
  -D exec.args="show-trace target/traces/<file>.zip"
```

## Current coverage

- Authentication: valid login, invalid credentials (parameterized), login from the home page, logout.

---

# CURA Healthcare: автотесты на Playwright (Java)

Фреймворк для UI-автоматизации демо-приложения [CURA Healthcare Service](https://katalon-demo-cura.herokuapp.com).

**Стек:** Java 17, Playwright, JUnit 5, AssertJ, Allure, Maven.

## Структура проекта

```
src/
├─ main/
│  ├─ java/com/cura/
│  │  ├─ components/   # Переиспользуемые UI-компоненты (например, SideMenu)
│  │  ├─ config/       # TestConfig: читает config.properties, поддерживает -D
│  │  ├─ core/         # PlaywrightManager: жизненный цикл браузера на поток, трассировка
│  │  ├─ models/       # Модели тестовых данных (User)
│  │  └─ pages/        # Page Object'ы (Home, Login, Appointment)
│  └─ resources/config.properties
└─ test/
   └─ java/com/cura/
      ├─ base/         # BaseTest: подготовка и завершение теста
      ├─ extensions/   # JUnit-расширения (скриншот при падении)
      └─ tests/        # Тесты, сгруппированные по функциональности (login, ...)
```

## Требования

- JDK 17+
- Maven 3.9+

Браузеры Playwright скачает автоматически при первом запуске.

## Запуск тестов

```bash
# Все тесты (headless Chromium)
mvn clean test

# Один тестовый класс
mvn test -Dtest=LoginTest

# Firefox, с окном браузера, замедление 300 мс
mvn test -Dbrowser=firefox -Dheadless=false -DslowMo=300
```

## Конфигурация

Значения по умолчанию лежат в `src/main/resources/config.properties`. Любое из них можно переопределить в командной строке через `-Dkey=value`.

| Параметр    | По умолчанию                                | Описание                                 |
|-------------|---------------------------------------------|------------------------------------------|
| `baseUrl`   | `https://katalon-demo-cura.herokuapp.com`   | Тестируемое приложение                   |
| `browser`   | `chromium`                                  | `chromium`, `firefox` или `webkit`       |
| `headless`  | `true`                                      | Запуск без окна браузера                 |
| `slowMo`    | `0`                                         | Задержка между действиями, мс            |
| `timeoutMs` | `10000`                                     | Таймаут Playwright по умолчанию, мс      |
| `username`  | `John Doe`                                  | Публичный демо-аккаунт                   |
| `password`  | `ThisIsNotAPassword`                        | Публичный демо-аккаунт                   |

## Отчёты и отладка

- **Allure-отчёт:** `mvn allure:serve`
- **Скриншот при падении:** автоматически прикрепляется к Allure-отчёту.
- **Playwright trace:** для упавших тестов сохраняется в `target/traces/*.zip`. Открыть:

```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI \
  -D exec.args="show-trace target/traces/<file>.zip"
```

## Текущее покрытие

- Авторизация: валидный вход, невалидные данные (параметризованный тест), вход с главной страницы, выход.
