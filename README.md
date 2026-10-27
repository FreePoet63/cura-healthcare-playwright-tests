# CURA Healthcare: Playwright Java Tests



![CI](https://github.com/FreePoet63/cura-healthcare-playwright-tests/actions/workflows/ci.yml/badge.svg?branch=master)



UI test automation framework for the [CURA Healthcare Service](https://katalon-demo-cura.herokuapp.com) demo web app.

**Live Allure report:** [Allure report](https://freepoet63.github.io/cura-healthcare-playwright-tests/)

**Stack:** Java 17, Playwright, JUnit 5, AssertJ, Allure, Maven, GitHub Actions.

## Project structure

```
src/
├─ main/
│  ├─ java/com/cura/
│  │  ├─ components/   # Reusable UI components (SideMenu, HistoryEntry)
│  │  ├─ config/       # TestConfig: reads config.properties, supports -D overrides
│  │  ├─ core/         # PlaywrightManager: per-thread browser lifecycle, tracing
│  │  ├─ models/       # Test data (User, Appointment, Facility, HealthcareProgram)
│  │  └─ pages/        # Page Objects (Home, Login, Appointment, Confirmation, History)
│  └─ resources/config.properties
└─ test/
   ├─ java/com/cura/
   │  ├─ base/         # BaseTest: setup / teardown, Allure browser tagging
   │  ├─ extensions/   # JUnit extensions (screenshot on failure)
   │  └─ tests/        # Tests grouped by feature: login, appointment, history
   └─ resources/allure.properties
.github/
├─ workflows/ci.yml  # CI pipeline
└─ dependabot.yml    # Dependency updates
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

## Continuous integration

GitHub Actions workflow (`.github/workflows/ci.yml`):

- **Triggers:** push and pull request to `master`, nightly schedule, manual run (browser, test filter and number of reruns can be chosen).
- **Browser matrix:** chromium, firefox and webkit in parallel (chromium only for pull requests).
- **Flaky tests:** a failed test is rerun twice (3 attempts in total); retries are visible in the report.
- **Allure report:** results of all browsers are merged into one report with history and trends and published to GitHub Pages from `master`.
- **Artifacts:** Allure results, Playwright traces of failed attempts, the generated report.
- **Dependabot:** weekly updates of GitHub Actions and Maven dependencies, grouped into one pull request per ecosystem.

## Reports and debugging

- **Allure report locally:** `mvn allure:serve`
- **Screenshot on failure:** attached to the Allure report automatically.
- **Playwright trace:** saved for failed tests to `target/traces/*.zip`. Open it with:

```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI \
  -D exec.args="show-trace target/traces/<file>.zip"
```

## Current coverage

- **Authentication:** valid login, invalid credentials (parameterized), login from the home page, logout.
- **Appointment booking:** all fields, required field only (defaults), every facility, every healthcare program, visit date validation, navigation from the confirmation page.
- **History:** empty history, booked appointment with entered data, several appointments, access without login.

---

# CURA Healthcare: автотесты на Playwright (Java)



![CI](https://github.com/FreePoet63/cura-healthcare-playwright-tests/actions/workflows/ci.yml/badge.svg?branch=master)



Фреймворк для UI-автоматизации демо-приложения [CURA Healthcare Service](https://katalon-demo-cura.herokuapp.com).

**Живой Allure-отчёт:** [Allure report](https://freepoet63.github.io/cura-healthcare-playwright-tests/)

**Стек:** Java 17, Playwright, JUnit 5, AssertJ, Allure, Maven, GitHub Actions.

## Структура проекта

```
src/
├─ main/
│  ├─ java/com/cura/
│  │  ├─ components/   # Переиспользуемые UI-компоненты (SideMenu, HistoryEntry)
│  │  ├─ config/       # TestConfig: читает config.properties, поддерживает -D
│  │  ├─ core/         # PlaywrightManager: жизненный цикл браузера на поток, трассировка
│  │  ├─ models/       # Тестовые данные (User, Appointment, Facility, HealthcareProgram)
│  │  └─ pages/        # Page Object'ы (Home, Login, Appointment, Confirmation, History)
│  └─ resources/config.properties
└─ test/
   ├─ java/com/cura/
   │  ├─ base/         # BaseTest: подготовка и завершение теста, пометка браузера в Allure
   │  ├─ extensions/   # JUnit-расширения (скриншот при падении)
   │  └─ tests/        # Тесты по функциональности: login, appointment, history
   └─ resources/allure.properties
.github/
├─ workflows/ci.yml  # CI-пайплайн
└─ dependabot.yml    # Обновление зависимостей
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

## Непрерывная интеграция

Workflow GitHub Actions (`.github/workflows/ci.yml`):

- **Запуск:** push и pull request в `master`, ночной прогон по расписанию, ручной запуск (можно выбрать браузер, фильтр тестов и число перезапусков).
- **Матрица браузеров:** chromium, firefox и webkit параллельно (для pull request только chromium).
- **Нестабильные тесты:** упавший тест перезапускается дважды (3 попытки всего), ретраи видны в отчёте.
- **Allure-отчёт:** результаты всех браузеров собираются в один отчёт с историей и трендами и публикуются на GitHub Pages из `master`.
- **Артефакты:** результаты Allure, trace упавших попыток, готовый отчёт.
- **Dependabot:** еженедельные обновления GitHub Actions и Maven-зависимостей, по одному сгруппированному pull request на экосистему.

## Отчёты и отладка

- **Allure-отчёт локально:** `mvn allure:serve`
- **Скриншот при падении:** автоматически прикрепляется к Allure-отчёту.
- **Playwright trace:** для упавших тестов сохраняется в `target/traces/*.zip`. Открыть:

```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI \
  -D exec.args="show-trace target/traces/<file>.zip"
```

## Текущее покрытие

- **Авторизация:** валидный вход, невалидные данные (параметризованный тест), вход с главной страницы, выход.
- **Запись на приём:** все поля, только обязательное поле (значения по умолчанию), каждая клиника, каждая программа, валидация даты визита, переход со страницы подтверждения.
- **История:** пустая история, записанный приём с введёнными данными, несколько записей, доступ без логина.
