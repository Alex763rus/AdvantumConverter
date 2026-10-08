CREATE TABLE IF NOT EXISTS lenta_ts_city
(
    ts VARCHAR(255) PRIMARY KEY,
    city_brief VARCHAR(255)
    );

-- Опционально: индексы для улучшения производительности
CREATE INDEX IF NOT EXISTS idx_lenta_ts_city_city_brief ON lenta_ts_city(city_brief);
CREATE INDEX IF NOT EXISTS idx_lenta_ts_city_ts_lower ON lenta_ts_city(LOWER(ts));

-- Опционально: комментарии
COMMENT ON TABLE lenta_ts_city IS 'Соответствие ТС городам для Ленты';
COMMENT ON COLUMN lenta_ts_city.ts IS 'Идентификатор транспортного средства';
COMMENT ON COLUMN lenta_ts_city.city_brief IS 'Краткое название города';