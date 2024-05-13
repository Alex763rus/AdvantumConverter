# Распределительный центр метро
-- #     ="select '"&L19&"', '"&M19&"', '"&N19&"' union all"
-- drop table metro_dc_addresses_dictionary;
-- select * from metro_dc_addresses_dictionary;
select count(*) from metro_dc_addresses_dictionary;
REPLACE INTO metro_dc_addresses_dictionary(addresses_id, addresses_brief, addresses_name)
select 'UK', 'DC Kazan', 'Казань, ул. Тихорецкая, д. 4' union all
select 'VT', 'DC Tolyati', 'Тольятти, Южное ш. Южное шоссе 2а' union all
select 'UZ', 'DC Igevsk', 'Ижевск, ул. Союзная, 6' union all
select 'YY/YR/YK', 'DC Rostov', 'DC Rostov' union all
select 'YN', 'DC Krasnodar', 'DC Krasnodar' union all
select 'UE', 'DC Ekat', 'Екатеринбург, дублер Сибирского тракта дублер Сибирского тракта 21'
;
