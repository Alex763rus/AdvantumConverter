-- # Справочник температурных режимов метро
-- #     ="select '"&A5&"', '"&C5&"', '"&B5&"' union all"
-- drop table metro_temperature_dictionary;
-- select * from metro_temperature_dictionary;
-- select count(*) from metro_temperature_dictionary;
INSERT INTO metro_temperature_dictionary (temperature_id, min_temperature, max_temperature)
VALUES ('1', 0, 2),
       ('-1', -2, 0),
       ('5', 4, 6),
       ('2', 1, 3),
       ('7', 6, 8),
       ('10', 2, 20),
       ('не требуется', -49, 49),
       ('>0', 1, 49),
       ('3', 2, 4),
       ('-18', -19, -17),
       ('-20', -30, -18),
       ('6', 5, 7),
       ('4', 3, 5),
       ('12', 11, 13),
       ('8', 7, 10),
       ('0', -1, 1),
       ('-1,5', -3, 0), -- Проблемное значение: "-1,5" с запятой
       ('16', 14, 18) ON CONFLICT (temperature_id)
DO
UPDATE SET
    min_temperature = EXCLUDED.min_temperature,
    max_temperature = EXCLUDED.max_temperature;