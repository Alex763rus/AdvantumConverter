# AdvantumConverter

Сервис конвертации Excel-файлов (выгрузок/сводных отчётов) для различных компаний-ритейлеров в целевой формат Advantum. Веб-интерфейс для загрузки и скачивания файлов, Telegram-бот, интеграция с CRM.

## Технологии

- Java 17, Spring Boot 3.2 (WebFlux, Data JPA, Validation)
- PostgreSQL + Liquibase (миграции в `src/main/resources/db/changelog`)
- Apache POI — чтение/запись Excel
- Thymeleaf — веб-интерфейс (загрузка, авторизация, админка)
- Telegram Bots API 5.6
- springdoc-openapi — Swagger UI (`/swagger-ui.html`)
- Docker (`Dockerfile`, `docker-compose.yml` для PostgreSQL 15)

## Запуск

```bash
# 1. Поднять БД
docker compose up -d db-postgres

# 2. Собрать и запустить
./mvnw spring-boot:run
```

Приложение стартует на порту `8080` (переопределяется переменной `SERVER_PORT`).

Основные переменные окружения (см. `src/main/resources/application.properties`):

| Переменная | Назначение |
|---|---|
| `DATASOURCE_URL` / `DATASOURCE_USERNAME` / `DATASOURCE_PASSWORD` | подключение к PostgreSQL |
| `DATASOURCE_DEFAULT_SCHEMA` | схема БД (по умолчанию `advantum_dev`) |
| `BOT_USERNAME` / `BOT_TOKEN` / `ADMIN_CHAT_ID` | Telegram-бот |
| `CRM_HOST` + `CRM_*_LOGIN` / `CRM_*_PASSWORD` | доступ к CRM (Sber, Lenta, Ozon, Siel, Spar, ...) |
| `INPUT_FILE_PATH` | каталог для входных файлов (по умолчанию `tmp/`) |

Деплой: `./mvnw package` → `docker-deploy.sh` (сборка образа `alex163rus/advantumconverter:latest` и пуш в Docker Hub).

## Структура

```
src/main/java/com/example/advantumconverter/
├── api/bot          — Telegram-бот
├── rest/            — REST-контроллеры
├── security/        — аутентификация и права
├── service/
│   ├── excel/
│   │   ├── converter/ — конвертеры по компаниям (rs/ — Лента, booker/, client/ ...)
│   │   └── generate/  — генерация итоговых Excel-файлов
│   ├── rest/out/      — вызовы внешних CRM
│   └── database/      — работа со справочниками
├── model/           — JPA-сущности, POJO, справочники, меню
└── constant/, enums/, exception/, aspect/
```

Конвертеры регистрируются в меню (`service/menu`) и выбираются пользователем при загрузке файла.

## Последние изменения

### `600cc0e` — доработка номер заказа и времени для возврата тары

- **`ConvertServiceImplRsLentaSpbHyperNew.java`** (Лента СПб гипер, возврат тары):
  - дата/время строки «Возврат тары» теперь формируется общим методом `prepareSdate(Date, Svod)`: используется `prepareDateStartEnd` с учётом срока сквиза (`svod.getSquiz()`) и окна `TIME_18`, вместо прежнего ручного склеивания строки времени;
  - разбор интервала времени вынесен в отдельный метод `getTime(timesFromFile, tk)` — пустые значения и `-` обрабатываются без предупреждения, ошибка парсинга добавляет warning с номером ТК;
  - `prepareTara` теперь пробрасывает `ParseException`, вызов обёрнут в try/catch.
- **`RsLentaSpbExcelNewGenerateService.java`**:
  - в ключах строк/повторов вместо `columnDdata` (тип операции «Доставка»/«Возврат тары») теперь используется `columnEdata` — номер заказа (ТК / РЦ комплектации): ключ строки — `дата_номерЗаказа_группаТоваров`, ключ повтора — `дата_номерЗаказа_повтор_имяКолонки_колонкаC`.
