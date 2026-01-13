INSERT INTO lenta_dictionary(lenta_dictionary_key, address_name, type, time_shop, time_stock, region)
SELECT lenta_dictionary_key,
       MIN(address_name),
       MIN(type),
       MIN(time_shop),
       MIN(time_stock),
       MIN(region)
FROM (
         SELECT 757                                      as lenta_dictionary_key,
                'Большой Сампсониевский пр. 66, лит. О.' as address_name,
                1                                        as type,
                '04:00'                                  as time_shop,
                '16:00'                                  as time_stock,
                'СПБ'                                    as region union all
select 8003, 'Россия, Санкт-Петербург, Запорожская улица, 12', 1, '04:00', '16:00', 'СПБ' union all
select 8023, 'Россия, Ленинградская область, Тосненский район, Тельмановское сельское поселение, посёлок Тельмана', 1, '04:00', '16:00', 'СПБ' union all
select 6006, 'г Санкт-Петербург, Таллинское ш. 159', 1, '04:00', '16:00', 'СПБ' union all
select 8052, '1 КМ СЕВЕРНЕЕ ЗЕЛЕНОВКА СНТ, 1,1', 1, '04:00', '16:00', 'Тольятти' union all
select 8092, 'ВОСТОК КОРП. 1, К.1, КОСУЛИНО, ЛОГОПАРК КОЛЬЦЕВАЯ-ВОСТОК', 1, '04:00', '16:00', 'ЕКБ' union all
select 926987, 'СТР.4, УЛ.ЩЕРБАКОВА', 1, '04:00', '16:00', 'ЕКБ' union all
select 8117, 'Россия, Московская область, городской округ Чехов, деревня Лешино', 1, '04:00', '16:00', 'МСК' union all
select 8123, 'Россия, Московская область, городской округ Подольск, деревня Валищево', 1, '04:00', '16:00', 'МСК' union all
select 6171, '164, УЛ. ЛЕЖНЕВСКАЯ', 1, '04:00', '16:00', 'МСК' union all
select 1337, 'г. Москва, Подольских Курсантов ул., д.10', 1, '04:00', '16:00', 'МСК' union all
select 1412,'г. Балашиха, Балашихинское ш., д. 10',1,'08:00', '14:00', 'МСК' union all
select 8072, 'Новосибирская область, Новосибирский район', 1, '04:00', '16:00', 'НСБ' union all
select 8032,'Россия, Ростовская область, Аксайский район, улица Логопарк, 3к1',1,'04:00', '20:00', 'РСТ'
     ) AS data
GROUP BY lenta_dictionary_key ON CONFLICT (lenta_dictionary_key)
    DO
UPDATE SET address_name = EXCLUDED.address_name,
    type = EXCLUDED.type,
    time_shop = EXCLUDED.time_shop,
    time_stock = EXCLUDED.time_stock,
    region = EXCLUDED.region;
