insert into car(car_name, pallet, tonnage, temperature_min, temperature_max)
select 'РЕФ_1_ТОНН_-18', 77777, 1000, -18, -13 union all
select 'РЕФ_20_ТОНН_5_C_36П', 77777, 20000, 5, 10 union all
select 'РЕФ_20_ТОНН_16_C', 77777, 20000, 16, 21 union all
select 'РЕФ_7_ТОНН_5_7_C_ГИДРОБОРТ', 77777, 7000, 5, 10 union all
select 'РЕФ_20_ТОНН_5_7_C', 77777, 20000, 5, 10 union all
select 'РЕФ_1_5_ТОНН_ГИДРОБОРТ', 77777, 1000, 1, 6 union all
select 'РЕФ_7_ТОНН_-18', 77777, 7000, -18, -13 union all
select 'РЕФ_7_ТОНН_14_C_ГИДРОБОРТ', 77777, 7000, 14, 19 union all
select 'РЕФ_5_ТОНН_1_3_C', 77777, 5000, 1, 6 union all
select 'РЕФРЕЖИРАТОРНЫЕ', 77777, 20000, 2, 7 union all
select 'РЕФ_7_ТОНН_6_8_ГИДРОБОРТ', 77777, 7000, 6, 11 union all
select 'РЕФ_12_ТОНН_6_8_C', 77777, 12000, 6, 11 union all
select 'РЕФ_20_ТОНН_3_7_C', 77777, 20000, 3, 8 union all
select 'РЕФ_20_ТОНН_7_3_C', 77777, 20000, 7, 12 union all
select 'РЕФ_12_ТОНН_6_8_C_ГИДРОБОРТ', 77777, 20000, 6, 11
;