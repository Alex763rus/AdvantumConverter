# Справочник температурных режимов метро
-- #     ="select '"&A5&"', '"&C5&"', '"&B5&"' union all"
-- drop table metro_temperature_dictionary;
-- select * from metro_temperature_dictionary;
select count(*) from metro_temperature_dictionary;
REPLACE INTO metro_temperature_dictionary(temperature_id, min_temperature, max_temperature)
select '1', '0', '2' union all
select '-1', '-2', '0' union all
select '5', '4', '6' union all
select '2', '1', '3' union all
select '7', '6', '8' union all
select '10', '2', '20' union all
select 'не требуется', '-49', '49' union all
select '>0', '1', '49' union all
select '3', '2', '4' union all
select '-18', '-19', '-17' union all
select '-20', '-30', '-18' union all
select '6', '5', '7' union all
select '4', '3', '5' union all
select '12', '11', '13' union all
select '8', '7', '10' union all
select '0', '-1', '1' union all
select '-1,5', '-3', '0' union all
select '16', '14', '18'
;